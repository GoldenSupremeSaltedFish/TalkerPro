package org.example.talker.kafka.Impl;

import org.example.talker.controller.http.TalkerRequest;
import org.example.talker.kafka.LinkMessageRouter;

import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author HumphreyLi
 * @version 1.0
 * @brief
 * @details 可用性一般般的路由
 * @date 2024/12/1
 * @time 上午2:19
 */
@Component
public class LinkMessageRouterImpl implements LinkMessageRouter {
    //todo （需要引入redis）（本地效率飞起，而且内存规格很高）（多实例情况下需要进行扩展）（redis还有天然的过期）

    private final RedissonClient redisson;

    @Autowired
    public LinkMessageRouterImpl(RedissonClient redisson) {
        this.redisson = redisson;
    }


    @Override
    public void inputConnection(String connectionUrl, String MessageID) {
        RMapCache<String, String> mapCache = redisson.getMapCache("connectionMap");
        // 存储键值对并为每个键设置独立过期时间
        mapCache.put(MessageID, connectionUrl, 30, TimeUnit.SECONDS);
    }


    @Override
    public void outputConnection(String connectionurl, String MessageID) {
        RMapCache<String, String> mapCache = redisson.getMapCache("connectionMap");
        mapCache.remove(MessageID, connectionurl);
    }

    @Override
    public void shutDown() {

    }

    @Override
    public String getUrlByMessageId(String connectionUrl, String MessageID) {
        RMapCache<String, String> mapCache = redisson.getMapCache("connectionMap");
        if (mapCache.containsKey(MessageID)){
            String Url = mapCache.get(MessageID);
            //使用url返回消息
            return Url;
        }
        else
        {
            return null;
        }


    }

}
