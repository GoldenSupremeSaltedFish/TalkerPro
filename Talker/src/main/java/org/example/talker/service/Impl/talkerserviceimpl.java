package org.example.talker.service.Impl;

import jakarta.annotation.Resource;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.example.talker.dto.Impl.KafkamessageToTalkMessageImpl;
import org.example.talker.dto.KafkaTRTalk;
import org.example.talker.entity.Kafka.KafkaMessage;
import org.example.talker.mapper.MessageMapper;
import org.example.talker.service.talkservice;
import org.example.talker.entity.TalkMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;


/**
 * 将客户端消息发送给kafka
 * 消息完整性验证，前端也做防止空消息
 *
 * @author Humphrey Li
 * @version 1.0
 * @since 2024/11/16
 */


@Service
public class talkerserviceimpl implements talkservice {
    private  final Logger log = LoggerFactory.getLogger(talkerserviceimpl.class);
    @Autowired
    KafkaTemplate<String, KafkaMessage> kafkaTemplate;
    @Autowired
    KafkaConsumer<String,KafkaMessage> kafkaConsumer;
    @Resource
    private MessageMapper messageMapper;


    private KafkamessageToTalkMessageImpl kafkamessageToTalkMessageImpl=new KafkamessageToTalkMessageImpl();

    @Value("${kafka.topics.SendToSparkNormal}")
    String topic;//目前统一向普通队列传递消息


    @Value("${kafka.message.timeout}")
    private long timeout;

    @Value("${kafka.message.poll-interval}")
    private long pollInterval;

    private final ConcurrentHashMap<String, TalkMessage> messageCache = new ConcurrentHashMap<>();

    @KafkaListener(topics = "your_callback_topic", groupId = "your_group_id")
    public void consumeMessage(KafkaMessage kafkaMessage) {
        // 将 KafkaMessage 转换为 TalkMessage 并存入缓存
        TalkMessage talkMessage = new TalkMessage(
                kafkaMessage.getMessageId(),
                kafkaMessage.getContent(),
                kafkaMessage.getSender(),
                kafkaMessage.getRecipient()
        );
        messageCache.put(kafkaMessage.getMessageId(), talkMessage);
    }



    @Override
    public String MessagetoMysql(TalkMessage talkMessage) {
       messageMapper.insertMessage(talkMessage);
       return talkMessage.getMessageid();//返回messageid
    }

    @Override
    public TalkMessage MessagefromMysql(String messageid) {
        TalkMessage talkMessage = messageMapper.getMessageById(messageid);
        return talkMessage;
    }


    @Override
    public boolean IsMessageRight(TalkMessage talkMessage) {
        // 检查 talkMessage 是否为空
        if (talkMessage == null) {
            return false;
        }
        // 检查 messageid 是否为空或空字符串
        if (talkMessage.getMessageid() == null || talkMessage.getMessageid().trim().isEmpty()) {
            return false;
        }
        // 检查 message 是否为空或空字符串
        if (talkMessage.getMessage() == null || talkMessage.getMessage().trim().isEmpty()) {
            return false;
        }

        // 所有检查都通过，实例合法
        return true;
    }

    @Override
    public CompletableFuture<Void> MessagetoKafkaAsync(TalkMessage talkMessage) {
        return null;
    }

    @Override
    public String MessagetoKafka(TalkMessage talkMessage) {
        if (!IsMessageRight(talkMessage)) {
            log.error("Invalid message");
            return "Invalid message";
        }
        try {
            KafkaMessage kafkaMessage = kafkamessageToTalkMessageImpl.TalkToKafka(talkMessage);
            kafkaTemplate.send(topic, talkMessage.getMessageid(), kafkaMessage);
        } catch (Exception e) {
            // 记录异常
            e.printStackTrace();
            return "Failed to store message in Redis: " + e.getMessage();
        }
        return "";
    }


    public CompletableFuture<TalkMessage> MessagefromKafkaAsync(String messageId) {
        return CompletableFuture.supplyAsync(() -> {
            TalkMessage talkMessage = null;
            long timeout = 10000L; // 超时时间 10 秒
            long pollInterval = 1000L; // 每次轮询间隔 1 秒
            long startTime = System.currentTimeMillis();

            while (System.currentTimeMillis() - startTime < timeout) {
                talkMessage = messageCache.get(messageId);
                if (talkMessage != null) {
                    log.info("Message retrieved from Kafka cache: {}", talkMessage);
                    return talkMessage;
                }
                try {
                    Thread.sleep(pollInterval); // 暂停轮询
                } catch (InterruptedException e) {
                    log.error("Thread interrupted while waiting for Kafka message", e);
                    Thread.currentThread().interrupt();
                    return null;
                }
            }

            log.warn("Message with ID {} not found within timeout period.", messageId);
            return null;
        });


}}
