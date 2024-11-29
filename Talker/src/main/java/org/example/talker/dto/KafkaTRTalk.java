package org.example.talker.dto;

import org.example.talker.dto.abstractInterface.Transformer;
import org.example.talker.entity.Kafka.KafkaMessage;
import org.example.talker.entity.TalkMessage;
/**
 * kafka消息转换为TalkMessage
 *
 * @author HumphreyLi
 * @version 1.0
 * @since 2024-11-20
 *
 */
public interface KafkaTRTalk<T> extends Transformer<T>  {



    TalkMessage KafkaToTalk(KafkaMessage kafkaMessage);

    KafkaMessage TalkToKafka(TalkMessage talkMessage);

}
