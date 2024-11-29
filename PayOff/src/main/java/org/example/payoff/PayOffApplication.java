package org.example.payoff;

import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@SpringBootApplication
public class PayOffApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayOffApplication.class, args);
    }
}



