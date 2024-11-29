package org.example.talker.kafka;

import org.example.talker.entity.Kafka.KafkaMessage;

public interface KafkaProducer {
    String send(String topic, KafkaMessage message);

}
