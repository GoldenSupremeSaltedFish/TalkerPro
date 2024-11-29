package org.example.talker.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.example.talker.entity.Kafka.KafkaMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.topics.messageToMysql}")
    private String messageToMysqlTopic;

    @Value("${kafka.topics.deadLetterQueue}")
    private String deadLetterQueueTopic;

    @Value("${kafka.topics.deadLetterQueue}")
    private String delayedMessagesTopic;

    @Value("${kafka.consumer.group-id}")
    private String groupId;

    @Value("${kafka.topics.SendToSparkNormal}")
    private String sendToSparkNormalTopic;

    @Value("${kafka.topics.SendToSparkVip}")
    private String sendToSparkVipTopic;

    @Bean
    public NewTopic myTopic() {
        return TopicBuilder.name(messageToMysqlTopic)
                .partitions(1)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic delayedTopic() {
        return TopicBuilder.name(deadLetterQueueTopic)
                .partitions(1)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic deadLetterTopic() {
        return TopicBuilder.name(delayedMessagesTopic)
                .partitions(1)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic SendToSparkNormal()
    {
        return TopicBuilder.name(sendToSparkNormalTopic)
               .partitions(1)
               .replicas(2)
               .build();
    }

    @Bean
    public NewTopic SendToSparkVip()
    {
        return TopicBuilder.name(sendToSparkVipTopic)
                .partitions(1)
                .replicas(2)
                .build();
    }

    @Bean
    public ProducerFactory<String, KafkaMessage> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, KafkaMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }

    @Bean
    public ConsumerFactory<String, KafkaMessage> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KafkaMessage> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, KafkaMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public org.apache.kafka.clients.consumer.KafkaConsumer<String, KafkaMessage> kafkaConsumer() {
        return new org.apache.kafka.clients.consumer.KafkaConsumer<>(consumerConfigs());
    }
}
