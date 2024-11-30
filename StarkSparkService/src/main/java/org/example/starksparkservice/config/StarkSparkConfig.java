package org.example.starksparkservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author HumphreyLi
 * @version 1.0
 * @brief
 * @details
 * @date 2024/11/27
 * @time 下午1:48
 */
@Configuration
@Getter
@Setter
public class StarkSparkConfig {
    @Value("${spring.spark.APPID}")
    private String AppID;
    @Value("${spring.spark.APISecret}")
    private String API_SECRET;
    @Value("${spring.spark.APIKey}")
    private String API_KEY;
    @Value("${spring.spark.host}")
    private String HOST;
    @Value("${spring.spark.path}")
    private String PATH;
    @Value("${spring.spark.method}")
    private String METHOD;

}
