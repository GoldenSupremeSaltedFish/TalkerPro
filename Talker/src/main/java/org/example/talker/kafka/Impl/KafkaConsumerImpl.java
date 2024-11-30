package org.example.talker.kafka.Impl;

import jakarta.annotation.Resource;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.example.talker.dto.KafkaTRTalk;
import org.example.talker.entity.Kafka.KafkaMessage;
import org.example.talker.kafka.KafkaConsumer;
import org.example.talker.mapper.MessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

@Service
@EnableKafka
public class KafkaConsumerImpl implements KafkaConsumer {
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerImpl.class);

    // 调用工厂,实现流量控制以及业务处理，在 service 层中直接进行调用

    @Resource
    private MessageMapper messageMapper;
    // 转换接口
    private KafkaTRTalk kafkaTRTalk;

    private KafkaListenerEndpointRegistry registry;

    private static final int BUCKET_CAPACITY = 100; // 定义桶的容量
    private Queue<KafkaMessage> messageQueue = new LinkedList<>();
    private long lastProcessTime = System.currentTimeMillis();
    private static final long BUCKET_LEAK_INTERVAL = 1000; // 1 秒

    @Override
    public void receive(String topic) {
        // 实现 receive 方法逻辑
    }

    @KafkaListener(topics =  "${kafka.topics.messageToMysql}", groupId = "${kafka.consumer.group-id}")
    public void receiverBucketInner(ConsumerRecord<String, KafkaMessage> record) {
        try {
            KafkaMessage message = record.value();
            processMessageWithLeakyBucket(message);
        } catch (Exception e) {
            log.error("处理消息时发生错误", e);
        }
    }

    @Override
    @KafkaListener(topics = "${kafka.topics.deadLetterQueue}", groupId = "${kafka.consumer.group-id}")
    public KafkaMessage receiveDeathTopic() {
        return null;
    }

    @Override
    @KafkaListener(topics = "${kafka.topics.deadLetterQueue}", groupId = "${kafka.consumer.group-id}")
    public KafkaMessage receiveDelayTopic() {
        return null;
    }


    @Override
    @KafkaListener(topics = "${kafka.topics.CallbackTopic}", groupId = "${kafka.consumer.group-id}")
    public void receiveCallBackTopic(ConsumerRecord<String, KafkaMessage> record) {
        try {
            KafkaMessage message = record.value();
            
        } catch (Exception e) {
            log.error("处理消息时发生错误", e);
        }
    }

    private void processMessageWithLeakyBucket(KafkaMessage message) {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastProcess = currentTime - lastProcessTime;

        if (timeSinceLastProcess >= BUCKET_LEAK_INTERVAL) {
            // 处理消息
            processMessage(message);
            lastProcessTime = currentTime;
        } else {
            // 如果桶满了，暂停消费
            if (messageQueue.size() >= BUCKET_CAPACITY) {
                pauseConsumer();
            }
            messageQueue.offer(message);
        }

        // 尝试处理队列中的消息
        processQueuedMessages();
    }

    private void processQueuedMessages() {
        while (!messageQueue.isEmpty() && (System.currentTimeMillis() - lastProcessTime) >= BUCKET_LEAK_INTERVAL) {
            KafkaMessage message = messageQueue.poll();
            processMessage(message);
            lastProcessTime = System.currentTimeMillis();
        }

        // 如果队列不再满，恢复消费
        if (messageQueue.size() < BUCKET_CAPACITY) {
            resumeConsumer();
        }
    }

    private void pauseConsumer() {
        MessageListenerContainer container = registry.getListenerContainer("messageListener");
        if (container != null && !container.isContainerPaused()) {
            container.pause();
        }
    }

    private void resumeConsumer() {
        MessageListenerContainer container = registry.getListenerContainer("messageListener");
        if (container != null && container.isContainerPaused()) {
            container.resume();
        }
    }

    private void processMessage(KafkaMessage message) {
        messageMapper.insertMessage(kafkaTRTalk.KafkaToTalk(message));
    }
}
