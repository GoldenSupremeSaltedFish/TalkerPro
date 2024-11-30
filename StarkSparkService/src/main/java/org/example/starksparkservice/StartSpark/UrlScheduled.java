package org.example.starksparkservice.StartSpark;


import org.example.starksparkservice.config.StarSparkAuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author HumphreyLi
 * @version 1.0
 * @brief
 * @details
 * @date 2024/11/26
 * @time 下午12:04
 */
@Service
public class UrlScheduled {
    private static final Logger logger = LoggerFactory.getLogger(UrlScheduled.class);

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    private StarSparkAuthUtils starSparkAuthUtils; // 使用@Autowired注解自动注入

    public UrlScheduled(@Qualifier("redisTemplateString") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Scheduled(fixedRate = 2000) // 每298秒执行一次
    public void scheduled() {
        try {
            String url = starSparkAuthUtils.generateAuthUrl();
            redisTemplate.opsForValue().set("url1", url, 30000, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("生成url失败", e);
        }
    }
}
