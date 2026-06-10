package com.spring.demo.reg_login.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.spring.demo.reg_login.mapper.ArticleMapper;
import com.spring.demo.reg_login.utils.ThreadLocalUtil;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final StringRedisTemplate redisTemplate;
    private final ArticleMapper articleMapper;

    // 点赞
    public void like(Long articleId) {
        String username = ThreadLocalUtil.get();
        String key = "article:" + articleId + ":like";
        Boolean liked = redisTemplate.opsForSet().isMember(key, username);
        if (Boolean.TRUE.equals(liked)) {
            throw new RuntimeException("已经点过赞了");
        }
        redisTemplate.opsForSet().add(key, username);
        articleMapper.addLikeCount(articleId);
    }

    // 取消点赞
    public void unlike(Long articleId) {
        String username = ThreadLocalUtil.get();
        String key = "article:" + articleId + ":like";
        Boolean liked = redisTemplate.opsForSet().isMember(key, username);
        if (!Boolean.TRUE.equals(liked)) {
            throw new RuntimeException("尚未点赞");
        }
        redisTemplate.opsForSet().remove(key, username);
        articleMapper.subLikeCount(articleId);
    }

    // 获取点赞数量
    public Long count(Long articleId) {
        return redisTemplate.opsForSet().size("article:" + articleId + ":like");
    }

    // 判断是否点赞
    public boolean hasLiked(Long articleId) {
        String username = ThreadLocalUtil.get();
        return Boolean.TRUE.equals(
                redisTemplate.opsForSet().isMember(
                        "article:" + articleId + ":like",
                        username
                )
        );
    }

}
