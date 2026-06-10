package com.spring.demo.reg_login.task;

import java.util.Set;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.spring.demo.reg_login.mapper.ArticleMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ViewCountSyncTask {
    private final StringRedisTemplate redisTemplate;

    private final ArticleMapper articleMapper;

    @Scheduled(fixedRate = 6000*2)
    public void syncViewCount() {

        Set<String> keys = redisTemplate.keys("article:view:*");

        if (keys == null || keys.isEmpty()) {
            return;
        }

        for (String key : keys) {

            String value = redisTemplate.opsForValue().get(key);

            if (value == null) continue;

            Long count = Long.valueOf(value);

            Long articleId =Long.valueOf(key.replace("article:view:",""));

            articleMapper.addViewCountBatch(articleId, count);

            // 同步浏览量到热点排行榜
            redisTemplate.opsForZSet().incrementScore(
                "article:hot",
                articleId.toString(),
                count
            );

            redisTemplate.delete(key);
        }

        System.out.println(
            "浏览量同步完成"
        );
    }
}
