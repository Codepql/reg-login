package com.spring.demo.reg_login.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
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

    // 查详情
    public Article detail(Long id) {
        String key = "article:detail:" + id;
        String json = redisTemplate.opsForValue().get(key);
        if (json != null) {
            articleMapper.addViewCount(id);
            return JSON.parseObject(json, Article.class);
        }
        Article article = articleMapper.findById(id);
        if (article == null) {
            throw new RuntimeException("文章不存在");
        }
        redisTemplate.opsForValue().set(key, JSON.toJSONString(article));
        articleMapper.addViewCount(id);
        return article;
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
        return articleMapper.hotList();
    }

}
