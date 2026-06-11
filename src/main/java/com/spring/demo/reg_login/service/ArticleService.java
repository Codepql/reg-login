package com.spring.demo.reg_login.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.spring.demo.reg_login.common.PageResult;
import com.spring.demo.reg_login.entity.Article;
import com.spring.demo.reg_login.entity.User;
import com.spring.demo.reg_login.mapper.ArticleMapper;
import com.spring.demo.reg_login.mapper.UserMapper;
import com.spring.demo.reg_login.utils.FileUtil;
import com.spring.demo.reg_login.utils.ThreadLocalUtil;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleMapper articleMapper;
    private final UserMapper userMapper;

    private final StringRedisTemplate redisTemplate;

    private static final String NULL_VALUE = "NULL";

    private static final String LOCK_PREFIX = "article:lock:";

    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;
    static {
        UNLOCK_SCRIPT = new DefaultRedisScript<>();
        UNLOCK_SCRIPT.setScriptText(
                """
                if redis.call('get', KEYS[1]) == ARGV[1]
                then
                    return redis.call('del', KEYS[1])
                else
                    return 0
                end
                """
        );
        UNLOCK_SCRIPT.setResultType(Long.class);
    }

    // list
    public List<Article> list() {
        return articleMapper.findAll();
    }

    // 新增
    public void add(String title, String content, String coverImg, Long categoryId) {
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setCoverImg(coverImg);
        article.setCategoryId(categoryId);
        article.setAuthor(ThreadLocalUtil.get()); // 获取当前登录用户
        articleMapper.insert(article);
    }

    // 查详情（Redis 缓存 + 分布式锁防击穿 + 空值缓存防穿透）
    public Article detail(Long id) {

        String key = "article:detail:" + id;
        String json = redisTemplate.opsForValue().get(key);

        // 空值缓存：防止缓存穿透
        if (NULL_VALUE.equals(json)) {
            throw new RuntimeException("文章不存在");
        }

        // 缓存命中
        if (json != null) {
            redisTemplate.opsForValue().increment(
                "article:view:" + id
            );
            return JSON.parseObject(json, Article.class);
        }

        // 缓存未命中 — 加分布式锁，只有一个线程回源 MySQL
        String lockKey   = LOCK_PREFIX + id;
        String lockValue = UUID.randomUUID().toString();

        Boolean success = redisTemplate.opsForValue()
                        .setIfAbsent(lockKey, lockValue, 10, TimeUnit.SECONDS);

        if (Boolean.FALSE.equals(success)) {
            // 未拿到锁，短暂等待后递归重试（此时其他线程正在回源）
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("系统繁忙，请稍后重试");
            }
            return detail(id);
        }

        try {
            // 双重检查：拿到锁后再次检查缓存（可能前一个线程已经写入）
            json = redisTemplate.opsForValue().get(key);
            if (json != null) {
                if (NULL_VALUE.equals(json)) {
                    throw new RuntimeException("文章不存在");
                }
                redisTemplate.opsForValue().increment("article:view:" + id);
                return JSON.parseObject(json, Article.class);
            }

            Article article = articleMapper.findById(id);

            if (article == null) {
                // 缓存空值，防止缓存穿透
                redisTemplate.opsForValue().set(
                        key,
                        NULL_VALUE,
                        5,
                        TimeUnit.MINUTES
                );
                throw new RuntimeException("文章不存在");
            }

            // 缓存随机过期时间，防止缓存雪崩
            long expire = 30 + ThreadLocalRandom.current().nextInt(10);

            redisTemplate.opsForValue().set(
                    key,
                    JSON.toJSONString(article),
                    expire,
                    TimeUnit.MINUTES
            );

            redisTemplate.opsForValue().increment(
                    "article:view:" + id
            );

            return article;

        } finally {
            // 原子解锁：只有锁的持有者才能释放
            redisTemplate.execute(
                UNLOCK_SCRIPT,
                Collections.singletonList(lockKey),
                lockValue
            );
        }
    }

    // 修改
    public void update(Long id, String title, String content, String coverImg, Long categoryId, String username) {
        Article article = articleMapper.findById(id);
        if (article == null) {
            throw new RuntimeException("博客不存在");
        }
        if (!article.getAuthor().equals(username)) {
            throw new RuntimeException("只能修改自己的博客！");
        }
        // 如果换了封面，删除旧图片
        if (article.getCoverImg() != null && !article.getCoverImg().equals(coverImg)) {
            FileUtil.delete(article.getCoverImg());
        }
        article.setTitle(title);
        article.setContent(content);
        article.setCoverImg(coverImg);
        article.setCategoryId(categoryId);
        articleMapper.update(article);
        // 删除缓存
        redisTemplate.delete("article:detail:" + id);
    }

    // 删除
    public void delete(Long id) {
        Article article = articleMapper.findById(id);
        if (article == null) {
            throw new RuntimeException("博客不存在");
        }
        String username = ThreadLocalUtil.get();
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        boolean isAdmin = "admin".equals(user.getRole());
        boolean isAuthor = username.equals(article.getAuthor());
        if (!isAdmin && !isAuthor) {
            throw new RuntimeException("没有权限删除该文章");
        }
        articleMapper.deleteById(id);
        FileUtil.delete(article.getCoverImg());
        // 删除缓存
        redisTemplate.delete("article:detail:" + id);
    }

    // 分页查询
    public PageResult<Article> page(Integer pageNum, Integer pageSize,
                                    String title, String author, Long categoryId) {
        PageHelper.startPage(pageNum, pageSize);
        Page<Article> page = (Page<Article>) articleMapper.page(title, author, categoryId);
        PageResult<Article> result = new PageResult<>();
        result.setTotal(page.getTotal());
        result.setList(page.getResult());
        return result;
    }

    // 查询我的文章
    public List<Article> myList() {
        String username = ThreadLocalUtil.get();
        return articleMapper.myList(username);
    }

    // 我的博客分页
    public PageResult<Article> myPage(Integer pageNum, Integer pageSize) {
        String username = ThreadLocalUtil.get();
        PageHelper.startPage(pageNum, pageSize);
        Page<Article> page = (Page<Article>) articleMapper.myPage(username);
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    // 热门文章
    public List<Article> hotList() {
        Set<String> ids = redisTemplate.opsForZSet()
            .reverseRange("article:hot", 0, 9);

        if (ids == null || ids.isEmpty()) {
            return articleMapper.findAll(); // fallback
        }

        List<Article> list = new ArrayList<>();

        for (String id : ids) {
            Article article = articleMapper.findById(Long.valueOf(id));
            if (article != null) {
                list.add(article);
            }
        }

        return list;
    }

}
