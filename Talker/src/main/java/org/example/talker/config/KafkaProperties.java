package org.example.talker.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {
    private String bootstrapServers;
    private String topicsMessageToMysql;
    private String topicsDeadLetterQueue;
    private String topicsDelayedMessages;
    private String consumerGroupId;

    // Getters and Setters
    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getTopicsMessageToMysql() {
        return topicsMessageToMysql;
    }

    public void setTopicsMessageToMysql(String topicsMessageToMysql) {
        this.topicsMessageToMysql = topicsMessageToMysql;
    }

    public String getTopicsDeadLetterQueue() {
        return topicsDeadLetterQueue;
    }

    public void setTopicsDeadLetterQueue(String topicsDeadLetterQueue) {
        this.topicsDeadLetterQueue = topicsDeadLetterQueue;
    }

    public String getTopicsDelayedMessages() {
        return topicsDelayedMessages;
    }

    public void setTopicsDelayedMessages(String topicsDelayedMessages) {
        this.topicsDelayedMessages = topicsDelayedMessages;
    }

    public String getConsumerGroupId() {
        return consumerGroupId;
    }

    public void setConsumerGroupId(String consumerGroupId) {
        this.consumerGroupId = consumerGroupId;
    }
}
