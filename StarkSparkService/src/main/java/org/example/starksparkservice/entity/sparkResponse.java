package org.example.starksparkservice.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author HumphreyLi
 * @version 1.0
 * @brief
 * @details
 * @date 2024/11/27
 * @time 下午11:59
 */

public class sparkResponse {
    @Getter
    @Setter
    private String sender;
    @Getter
    @Setter
    private String sid;
    @Getter
    @Setter// 会话唯一 ID
    private int status;
    @Getter
    @Setter// 响应状态
    private String content;
    @Getter
    @Setter// AI的回答内容
    private int tokenUsage;    // Token 消耗数量

    // 构造函数
    public sparkResponse(String sid, int status, String content, int tokenUsage) {
        this.sid = sid;
        this.status = status;
        this.content = content;
        this.tokenUsage = tokenUsage;
        this.sender = sender;
    }



    // 静态方法：从 API 响应中创建 sparkResponse 对象
    public static sparkResponse fromApiResponse(Map<String, Object> apiResponse) {
        try {
            // 提取 header 和 payload
            Map<String, Object> header = safeGetAsMap(apiResponse, "header");
            Map<String, Object> payload = safeGetAsMap(apiResponse, "payload");

            // 获取 sid 和 status
            String sid = safeGetAsString(header, "sid");
            int status = safeGetAsInt(header, "status");

            // 提取 content 文本拼接
            StringBuilder contentBuilder = new StringBuilder();
            Map<String, Object> choices = safeGetAsMap(payload, "choices");
            if (choices != null) {
                int choiceStatus = safeGetAsInt(choices, "status");
                List<?> textList = safeGetAsList(choices, "text");

                if (textList != null && !textList.isEmpty()) {
                    Object firstItem = textList.get(0);
                    if (firstItem instanceof Map) {
                        Map<?, ?> textMap = (Map<?, ?>) firstItem;
                        String content = safeGetAsString(textMap, "content");

                        if (choiceStatus == 0) {
                            contentBuilder.append("[开始]").append(content);
                        } else if (choiceStatus == 1) {
                            contentBuilder.append(content);
                        } else if (choiceStatus == 2) {
                            contentBuilder.append("[结束]").append(content);
                        }
                    }
                }
            }

            // 提取 Token 使用情况
            int tokenUsage = 0;
            if (status == 2 && payload.containsKey("usage")) {
                Map<String, Object> usage = safeGetAsMap(payload, "usage");
                Map<String, Object> textUsage = safeGetAsMap(usage, "text");
                tokenUsage = safeGetAsInt(textUsage, "total_tokens");
            }

            // 创建并返回 sparkResponse 实例
            return new sparkResponse(sid, status, contentBuilder.toString(), tokenUsage);
        } catch (Exception e) {
            throw new IllegalArgumentException("API response format is invalid", e);
        }
    }

    // 辅助方法：安全类型转换
    private static Map<String, Object> safeGetAsMap(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return (value instanceof Map) ? (Map<String, Object>) value : null;
    }

    private static List<?> safeGetAsList(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return (value instanceof List) ? (List<?>) value : null;
    }

    private static String safeGetAsString(Map<?, ?> map, String key) {
        Object value = map.get(key);
        return (value instanceof String) ? (String) value : null;
    }

    private static int safeGetAsInt(Map<?, ?> map, String key) {
        Object value = map.get(key);
        return (value instanceof Number) ? ((Number) value).intValue() : 0;
    }



    @Override
    public String toString() {
        return "sparkResponse{" +
                "sid='" + sid + '\'' +
                ", status=" + status +
                ", content='" + content + '\'' +
                ", tokenUsage=" + tokenUsage +
                '}';
    }
}



