package org.example.talker.dto.Impl;

import org.example.talker.dto.KafkaTRTalk;
import org.example.talker.entity.Kafka.KafkaMessage;
import org.example.talker.entity.TalkMessage;

import java.util.HashMap;
import java.util.Map;
/**
 *
 * 这个类是kafka消息和talk消息之间的转换器，用于将kafka消息转换为talk消息，或者将talk消息转换为kafka消息。
 * @author HumphreyLi
 * @version 1.0
 * @since 2024-11-20
 *
 */
public class KafkamessageToTalkMessageImpl implements KafkaTRTalk {

    private int senderid;
    private int receiverid;
    private long timestamp;
    @Override
    public TalkMessage KafkaToTalk(KafkaMessage kafkaMessage) {

        TalkMessage talkMessage = new TalkMessage(kafkaMessage.getMessageId(),kafkaMessage.getContent(),senderid,receiverid);
        return talkMessage;
    }

    @Override
    public KafkaMessage TalkToKafka(TalkMessage talkMessage) {
        timestamp=System.currentTimeMillis();
        senderid=talkMessage.getSenderid();
        receiverid=talkMessage.getReceiverid();
        Map<String, String> metadata=new HashMap<>();
        KafkaMessage kafkaMessage=new KafkaMessage(talkMessage.getMessageid(),talkMessage.getMessage(),senderid,receiverid,timestamp,metadata);
        return kafkaMessage;
    }

    @Override
    public TalkMessage transform(Object input) {
        return null;
    }

    @Override
    public Object retransform(Object input) {
        return null;
    }


}
