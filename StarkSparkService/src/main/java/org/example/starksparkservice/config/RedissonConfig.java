package org.example.starksparkservice.config;



import org.example.starksparkservice.entity.TalkMessage;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 这个类是redis的配置类，用于配置redis的连接信息，包括连接地址、密码等。
 *
 * @author HumphreyLi
 * @version 1.0
 * @since 2024-11-20
 *
 */
@Configuration
@EnableRedisRepositories
public class RedissonConfig {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, TalkMessage> redisTemplate() {
        RedisTemplate<String, TalkMessage> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        return Redisson.create(config);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplateString() {
        RedisTemplate<String, String> template = new RedisTemplate<>();

        // 设置连接工厂
        template.setConnectionFactory(redisConnectionFactory());

        // 设置键和值的序列化器为 StringRedisSerializer
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        // 设置哈希键和值的序列化器（如果需要操作哈希类型）
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());

        // 初始化配置
        template.afterPropertiesSet();

        return template;
    }


}


