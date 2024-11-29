package org.example.starksparkservice;

import org.example.starksparkservice.StartSpark.SparkApiTalkService;
import org.example.starksparkservice.StartSpark.StarkSparkApiService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = "org.example.starksparkservice")
public class StarkSparkServiceApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(StarkSparkServiceApplication.class, args);

    }

}
