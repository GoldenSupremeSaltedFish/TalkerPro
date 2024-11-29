package org.example.starksparkservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * 这个类是Kafka消息的实体类，用于封装Kafka消息的相关信息。
 *
 * @author HumphreyLi
 * @version 1.0
 * @since 2024-11-20
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KafkaMessage {
    private String messageId;  // 消息唯一标识
    private String content;    // 消息内容
    private String sender;        // 发送者
    private int recipient;     // 接收者
    private long timestamp;    // 消息时间戳
    private Map<String, String> metadata;

    public String getcontent() {
        return content;
    }
}
