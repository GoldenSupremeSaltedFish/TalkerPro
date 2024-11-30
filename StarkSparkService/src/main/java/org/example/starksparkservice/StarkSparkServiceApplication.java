package org.example.starksparkservice;

import org.example.starksparkservice.StartSpark.SparkApiTalkService;
import org.example.starksparkservice.StartSpark.StarkSparkApiService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableDiscoveryClient
@EnableScheduling
@SpringBootApplication
public class StarkSparkServiceApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(StarkSparkServiceApplication.class, args);
    }

}
