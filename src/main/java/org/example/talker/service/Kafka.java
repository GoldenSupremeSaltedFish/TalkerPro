package org.example.talker.service;

import org.example.talker.entity.Kafka.KafkaMessage;

public interface Kafka {
    //normal
    String send(String topic, KafkaMessage message);

    KafkaMessage receive(String topic,String LimiteWay);
    //dead
    String sendToDeadLetterQueue(KafkaMessage message);

    KafkaMessage getDeadLetterQueue(String topic);
    //delay
    String sendToDelayedQueue(KafkaMessage message);

    KafkaMessage getDelayedMessage(String topic);
}
