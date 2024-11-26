package org.example.talker;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableAsync
@SpringBootApplication
@EnableKafka
@EnableWebFlux
@EnableDiscoveryClient
@EnableScheduling
public class TalkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TalkerApplication.class, args);
    }

}
