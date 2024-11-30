package org.example.talker.kafka.Impl;

import org.example.talker.kafka.LinkMessageRouter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    //todo 要不要引入redis（需要引入redis）（本地效率飞起，而且内存规格很高）
    private final Map<String, String> linkConnectionMap = new ConcurrentHashMap<>();


    @Override
    public void inputConnection(String connectionId, String userId) {

    }

    @Override
    public void outputConnection(String connectionId, String userId) {

    }

    @Override
    public void shutDown() {

    }

    @Override
    public void receiveMessage(String connectionId, String message) {

    }

    @Override
    public void retryReceiveMessage(String connectionId, String message) {

    }
}
