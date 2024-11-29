package org.example.talker.service.Impl;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.example.talker.entity.Kafka.KafkaMessage;

import org.example.talker.kafka.KafkaConsumer;
import org.example.talker.kafka.KafkaProducer;
import org.example.talker.service.Kafka;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;

/**
 * 这个类用于实现Kafka的接口，并实现Kafka的功能。
 *
 * @author HumphreyLi
 * @version 1.0
 * @since 2024-11-20
 *
 */
@Service
public class KafkaImpl implements Kafka {

    private final KafkaProducer kafkaProducer;

    private final KafkaConsumer kafkaConsumer;

    @Value("${kafka.topics.messageToMysql}")
    private String messagetoMysqlTopic;
    @Value("${kafka.topics.deadLetterQueue}")
    private String deadLetterQueueTopic;
    @Value("${kafka.topics.delayedMessages}")
    private String delayedMessagesTopic;
    @Value("${kafka.consumer.group-id}")
    private String groupId;

    public KafkaImpl(KafkaProducer kafkaProducer, KafkaConsumer kafkaConsumer) {
        this.kafkaProducer = kafkaProducer;
        this.kafkaConsumer = kafkaConsumer;
    }


    @Override
    public String send(String topic, KafkaMessage message) {
       String result=kafkaProducer.send(topic, message);
       return result;
    }

    @Override
    public KafkaMessage receive(String topic,String LimiteWay) {
        if(LimiteWay.equals("bucket")){
//            kafkaConsumer.receive(topic,groupId);
//            //todo
//            // 将漏桶算法进行抽离
        }

        return null;
    }

    @Override
    public String sendToDeadLetterQueue(KafkaMessage message) {
        try {

            // 发送消息到死信队列
            kafkaProducer.send("${kafka.topics.deadLetterQueue}", message);
            // 记录日志
            String logMessage = String.format("MessageMapper sent to dead letter queue: %s", message);


            return "MessageMapper successfully sent to dead letter queue";
        } catch (Exception e) {
            // 处理发送失败的情况
            String errorMessage = String.format("Failed to send message to dead letter queue: %s. Error: %s", message, e.getMessage());


            return "Failed to send message to dead letter queue: " + e.getMessage();
        }
    }

    @Override
    public KafkaMessage getDeadLetterQueue(String topic) {
        //todo
        //  死信队列回收机制
        return null;
    }

    @Override
    public String sendToDelayedQueue(KafkaMessage message) {
        try {
            kafkaProducer.send("${kafka.topics.delayedMessages}", message);
            return "MessageMapper successfully sent to delayed queue";
        }
        catch (Exception e) {
            String errorMessage = String.format("Failed to send message to delayed queue: %s", e.getMessage());
            return "Failed to send message to delayed queue: " + e.getMessage();
        }
    }

    @Override
    public KafkaMessage getDelayedMessage(String topic) {
        //todo
        //  延迟队列回收机制
        return null;
    }

    @Override
    public String SendToSparkNormal(KafkaMessage message) {
        try {
            kafkaProducer.send("${kafka.topics.SendToSparkNormal}", message);
            return "MessageMapper successfully sent to SendToSparkNormal queue";
        }
        catch (Exception e) {
            String errorMessage = String.format("Failed to send message to SendToSparkNormal queue: %s", e.getMessage());
            return "Failed to send message to SendToSparkNormal queue: " + e.getMessage();
        }
    }

    @Override
    public String SendToSparkVip(KafkaMessage message) {
        try {
            kafkaProducer.send("${kafka.topics.SendToSparkVip}", message);
            return "MessageMapper successfully sent to SendToSparkVip queue";
        }
        catch (Exception e) {
            String errorMessage = String.format("Failed to send message to SendToSparkVip queue: %s", e.getMessage());
            return "Failed to send message to SendToSparkVip queue: " + e.getMessage();
        }
    }

//    @Override
//    public KafkaMessage getSparkVip(String topic) {
////        org.apache.kafka.clients.consumer.KafkaConsumer<String, KafkaMessage> consumer = kafkaConsumer();.......?
//        //共享消费者还是每次创建消费者
////        consumer.subscribe(Collections.singletonList(topic));
//
//        try {
////            ConsumerRecords<String, KafkaMessage> records = consumer.poll(Duration.ofSeconds(5));
//            for (ConsumerRecord<String, KafkaMessage> record : records) {
//                return record.value(); // 返回第一条消息的反序列化对象
//            }
//        } catch (Exception e) {
//            System.err.println("Failed to consume message from topic " + topic + ": " + e.getMessage());
//        } finally {
////            consumer.close();
//        }
//        return null;
//
//    }



//    @Override
//    public KafkaMessage getSparkNormal(String topic) {
//        return null;
//    }


}
