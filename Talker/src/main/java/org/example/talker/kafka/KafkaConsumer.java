package org.example.talker.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.talker.entity.Kafka.KafkaMessage;

public interface KafkaConsumer {


    void receive(String topic);

//    int receiverBucket();

    KafkaMessage receiveDeathTopic();

    KafkaMessage receiveDelayTopic();

    void receiveCallBackTopic(ConsumerRecord<String, KafkaMessage> record);
}
