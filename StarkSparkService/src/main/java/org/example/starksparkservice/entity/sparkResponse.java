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
@Getter
@Setter
public class sparkResponse {
    private String sender;
    private String sid;        // 会话唯一 ID
    private int status;        // 响应状态
    private String content;    // AI的回答内容
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
        Map<String, Object> header = (Map<String, Object>) apiResponse.get("header");
        Map<String, Object> payload = (Map<String, Object>) apiResponse.get("payload");

        String sid = (String) header.get("sid");  // 提取 sid
        int status = (int) header.get("status"); // 提取状态

        // 提取 content
        List<Map<String, Object>> choices = (List<Map<String, Object>>) payload.get("choices");
        StringBuilder contentBuilder = new StringBuilder();
        int tokenUsage = 0;

        if (choices != null && !choices.isEmpty()) {
            for (Map<String, Object> choice : choices) {
                String content = (String) choice.get("content");
                int seqStatus = (int) choice.get("status");

                if (seqStatus == 0) {
                    contentBuilder.append("[开始]").append(content);
                } else if (seqStatus == 1) {
                    contentBuilder.append(content);
                } else if (seqStatus == 2) {
                    contentBuilder.append("[结束]").append(content);
                }
            }
        }

        // 提取 Token 消耗信息
        Map<String, Object> usage = (Map<String, Object>) payload.get("usage");
        if (usage != null) {
            tokenUsage = (int) usage.getOrDefault("total_tokens", 0);
        }

        return new sparkResponse(sid, status, contentBuilder.toString(), tokenUsage);
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



