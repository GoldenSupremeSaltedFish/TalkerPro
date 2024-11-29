package org.example.payoff.config;

/**
 * @author HumphreyLi
 * @version 1.0
 * @brief
 * @details
 * @date 2024/11/22
 * @time 上午11:06
 */
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
@Getter
@Setter
@RefreshScope
@Configuration
@NacosConfigurationProperties(dataId = "PayOffConfig", autoRefreshed = true)
@ConfigurationProperties(prefix = "spring.datasource")
public class NacosConfig {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private Tomcat tomcat = new Tomcat();

    // Getters and setters

    public static class Tomcat {
        private int maxActive;
        private int maxIdle;
        private int minIdle;
        private int initialSize;

        // Getters and setters
    }

    // Getters and setters for DataSourceConfig fields
}

