package org.example.talker.service.Impl;

import jakarta.annotation.Resource;
import org.example.talker.mapper.MessageMapper;
import org.example.talker.service.talkservice;
import org.example.talker.entity.TalkMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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
    private static final Logger log = LoggerFactory.getLogger(talkerserviceimpl.class);
    @Autowired
    private RedisTemplate<String, TalkMessage> redisTemplate;
    @Resource
    private MessageMapper messageMapper;

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
        // 检查 senderid 和 receiverid 是否为正数
        if (talkMessage.getSenderid() !=null || talkMessage.getReceiverid() !=null) {
            return false;
        }
        // 所有检查都通过，实例合法
        return true;
    }

    @Override
    public String MessagetoKafka(TalkMessage talkMessage) {
        if (!IsMessageRight(talkMessage)) {
            log.error("Invalid message");
            return "Invalid message";
        }

        try {
            // 使用 messageid 作为 key
            String key = "message:" + talkMessage.getMessageid();

            // 将 TalkMessage 对象存储到 Redis
            redisTemplate.opsForValue().set(key, talkMessage);

            // 设置过期时间（可选，这里设置为24小时）
            redisTemplate.expire(key, 24, TimeUnit.HOURS);

            return "MessageMapper successfully stored in Redis";
        } catch (Exception e) {
            // 记录异常
            e.printStackTrace();
            return "Failed to store message in Redis: " + e.getMessage();
        }

    }

    @Override
    public TalkMessage MessagefromKafka(String messageid) {
        return null;
    }

}
