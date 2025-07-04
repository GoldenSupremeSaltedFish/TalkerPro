package org.example.payoff;

import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@SpringBootApplication
@Configuration
public class PayOffApplication {

    @Bean
    public KubernetesClient kubernetesClient() {
        Config config = new ConfigBuilder().build();
        return new DefaultKubernetesClient(config);
    }

    public static void main(String[] args) {
        SpringApplication.run(PayOffApplication.class, args);
    }
}



