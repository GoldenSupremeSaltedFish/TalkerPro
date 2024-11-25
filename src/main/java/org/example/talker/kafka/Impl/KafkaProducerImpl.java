package org.example.talker.kafka.Impl;

import org.example.talker.entity.Kafka.KafkaMessage;
import org.example.talker.kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerImpl implements KafkaProducer {

    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    @Autowired
    public KafkaProducerImpl(KafkaTemplate<String, KafkaMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public String send(String topic, KafkaMessage message) {
        kafkaTemplate.send(topic, message.getMessageId(), message);
        return topic+":send a file";
    }
}
