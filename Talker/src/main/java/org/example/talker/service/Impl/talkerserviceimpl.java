package org.example.talker.service.Impl;

import jakarta.annotation.Resource;
import org.example.talker.dto.Impl.KafkamessageToTalkMessageImpl;
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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

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
    private final Logger log = LoggerFactory.getLogger(talkerserviceimpl.class);

    @Autowired
    private KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    @Resource
    private MessageMapper messageMapper;

    private final KafkamessageToTalkMessageImpl kafkamessageToTalkMessageImpl = new KafkamessageToTalkMessageImpl();

    @Value("${kafka.topics.SendToSparkNormal}")
    private String topic; //目前统一向普通队列传递消息

    @Value("${kafka.message.timeout}")
    private long timeout; // Kafka 消息超时

    @Value("${kafka.message.poll-interval}")
    private long pollInterval; // Kafka 消息轮询间隔

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
        return talkMessage.getMessageid(); // 返回messageid
    }

    @Override
    public TalkMessage MessagefromMysql(String messageid) {
        return messageMapper.getMessageById(messageid);
    }

    @Override
    public boolean IsMessageRight(TalkMessage talkMessage) {
        // 检查 talkMessage 是否为空
        return talkMessage != null &&
                talkMessage.getMessageid() != null && !talkMessage.getMessageid().trim().isEmpty() &&
                talkMessage.getMessage() != null && !talkMessage.getMessage().trim().isEmpty();
    }

    @Override
    public CompletableFuture<Void> MessagetoKafkaAsync(TalkMessage talkMessage) {
        return CompletableFuture.runAsync(() -> {
            try {
                if (IsMessageRight(talkMessage)) {
                    KafkaMessage kafkaMessage = kafkamessageToTalkMessageImpl.TalkToKafka(talkMessage);
                    kafkaTemplate.send(topic, talkMessage.getMessageid(), kafkaMessage);
                } else {
                    log.error("Invalid message: {}", talkMessage);
                }
            } catch (Exception e) {
                log.error("Error sending message to Kafka", e);
            }
        });
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
            log.error("Failed to store message in Redis", e);
            return "Failed to store message in Redis: " + e.getMessage();
        }
        return "";
    }

    @Override
    public CompletableFuture<TalkMessage> MessagefromKafkaAsync(String messageId) {
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            TalkMessage talkMessage = null;

            // 循环直到超时
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
    }
}
