package org.example.payoff.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author HumphreyLi
 * @version 1.0
 * @brief
 * @details
 * @date 2024/11/21
 * @time 下午11:56
 */
@Configuration
@MapperScan("org.example.payoff.Mapper")
public class MybatisConfig {
}
