package org.example.starksparkservice.entity;

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

    private String sid;       // 会话唯一 ID
    private int status;       // 响应状态
    private String content;   // AI的回答内容

    // 构造函数
    public sparkResponse(String sid, int status, String content) {
        this.sid = sid;
        this.status = status;
        this.content = content;
    }

    // Getter and Setter
    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // 静态方法：从 API 响应中创建 SparkResponse 对象
    public static sparkResponse fromApiResponse(Map<String, Object> apiResponse) {
        Map<String, Object> header = (Map<String, Object>) apiResponse.get("header");
        Map<String, Object> payload = (Map<String, Object>) apiResponse.get("payload");

        String sid = (String) header.get("sid"); // 提取 sid
        int status = (int) header.get("status"); // 提取 status

        // 提取 content 从 payload 里面
        List<Map<String, Object>> choices = (List<Map<String, Object>>) payload.get("choices");
        String content = null;
        if (choices != null && !choices.isEmpty()) {
            content = (String) choices.get(0).get("content");
        }

        return new sparkResponse(sid, status, content);
    }

    @Override
    public String toString() {
        return "SparkResponse{" +
                "sid='" + sid + '\'' +
                ", status=" + status +
                ", content='" + content + '\'' +
                '}';
    }
}


