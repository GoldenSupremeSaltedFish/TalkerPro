package org.example.talker.kafka;

import org.example.talker.entity.Kafka.KafkaMessage;

public interface KafkaConsumer {


    void receive(String topic);

//    int receiverBucket();

    KafkaMessage receiveDeathTopic();

    KafkaMessage receiveDelayTopic();

}
