package org.example.starksparkservice.entity;

import org.example.starksparkservice.utils.SparkPost;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.HashMap;
import java.util.Map;
/**
 * @author HumphreyLi
 * @version 1.0
 * @brief
 * @details
 * @date 2024/11/27
 * @time 下午11:49
 */


public class transform {

    public transform() {
    }

    /**
     * 将 KafkaMessage 转换为 SparkPost
     *
     * @param kafkaMessage Kafka 消息对象
     * @return 转换后的 SparkPost 对象
     */
    public SparkPost kafkaToSpark(KafkaMessage kafkaMessage) {
        if (kafkaMessage == null) {
            return null;
        }

        // 构造 Header
        SparkPost.Header header = new SparkPost.Header(
                kafkaMessage.getMessageId(), // 将消息唯一标识作为 app_id
                String.valueOf(kafkaMessage.getSender()) // 使用 sender 作为用户 ID
        );

        // 构造 Parameter
        SparkPost.Parameter.Chat chat = new SparkPost.Parameter.Chat(
                "generalv3.5", // 使用默认领域
                0.5,           // 默认温度
                1024           // 默认最大 token
        );
        SparkPost.Parameter parameter = new SparkPost.Parameter(chat);

        // 构造 Payload
        SparkPost.Payload.Message message = new SparkPost.Payload.Message(
                "user",                 // 默认角色为 "user"
                kafkaMessage.getContent() // 使用 Kafka 消息的内容作为对话输入
        );
        SparkPost.Payload payload = new SparkPost.Payload(message);

        return new SparkPost(header, parameter, payload);
    }

    //todo
    /**
     * 将 SparkResponse 转换为 KafkaMessage
     *
     * @param sparkResponse Spark 响应对象
     * @return 转换后的 Kafka 消息对象
     */
    public static KafkaMessage SparkToKafka(sparkResponse sparkResponse, String sender, String recipient) {
        // 从 SparkResponse 中提取数据
        String messageId = sparkResponse.getSid(); // 使用 sid 作为唯一标识
        String content = sparkResponse.getContent(); // 获取 AI 返回的内容

        long timestamp = System.currentTimeMillis(); // 当前时间戳

        // 构建 metadata
        Map<String, String> metadata = new HashMap<>();
        metadata.put("status", String.valueOf(sparkResponse.getStatus())); // 响应状态信息

        // 创建并返回 KafkaMessage
        return new KafkaMessage(messageId, content, sender, recipient, timestamp, metadata);//将接受者设置为当前服务的api代号
    }



}

