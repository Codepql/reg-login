package com.spring.demo.reg_login.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.spring.demo.reg_login.utils.ThreadLocalUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final StringRedisTemplate redisTemplate;

    // 点赞
    public void like(Long articleId) {

        String username =
        ThreadLocalUtil.get();

        redisTemplate.opsForSet().add(
            "article:" + articleId + ":like",
            username
        );

    }

    // 取消点赞
    public void unlike(Long articleId) {

        String username = ThreadLocalUtil.get();
        
        redisTemplate.opsForSet().remove(
            "article:" + articleId + ":like",
            username
        );

    }

    // 获取点赞数量
    public Long count(Long articleId) {
        return redisTemplate.opsForSet().size(
            "article:" + articleId + ":like"
        );
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

