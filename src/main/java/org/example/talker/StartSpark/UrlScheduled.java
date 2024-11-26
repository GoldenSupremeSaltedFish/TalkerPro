package org.example.talker.StartSpark;

import org.example.talker.util.Impl.StarSparkAuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author HumphreyLi
 * @version 1.0
 * @brief
 * @details
 * @date 2024/11/26
 * @time 下午12:04
 */
@Component
public class UrlScheduled {

    // 定义静态的 Logger 对象
    private static final Logger logger = LoggerFactory.getLogger(UrlScheduled.class);

    @Scheduled(fixedRate = 20000) // 20秒执行一次
    public void scheduled() {
        try {
            String url = StarSparkAuthUtils.generateAuthUrl();
            // 存入redis，设置过期时间为25秒
            RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
            redisTemplate.opsForValue().set("url", url, 25, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("生成url失败", e);
        }
    }
}
