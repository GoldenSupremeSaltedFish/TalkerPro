package org.example.talker.controller.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HumphreyLi
 * @version 1.0
 * @brief 构建星火大模型请求体
 * @details 该类用于构建星火大模型的请求体
 * @date 2024/11/26
 */
public class StarSparkPostBody {

    /**
     * 构建星火大模型请求体
     *
     * @param appId 应用ID
     * @param uid 用户ID
     * @param userMessage 用户消息
     * @return 请求体JSON字符串
     * @throws Exception 异常
     */

    public static String buildRequest(String appId, String uid, String userMessage) throws Exception {
        // 构建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("header", Map.of("app_id", appId));
        requestBody.put("parameter", Map.of("chat", Map.of("domain", "4.0Ultra", "temperature", 0.5, "max_tokens", 1024)));
        requestBody.put("payload", Map.of("message", new Object[]{
                Map.of("role", "user", "content", userMessage)
        }));

        // 将请求体转换为 JSON
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(requestBody);
    }
}
