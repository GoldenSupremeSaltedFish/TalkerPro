package org.example.starksparkservice.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SparkPost 用于封装 Spark AI 服务的请求参数 (单次对话版本)
 *
 * @author HumphreyLi
 * @version 1.0
 * @since 2024-11-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SparkPost {

    private Header header;        // 请求头信息
    private Parameter parameter;  // 请求参数
    private Payload payload;      // 请求负载

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Header {
        private String app_id; // 应用 ID
        private String uid;    // 用户 ID
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Parameter {
        private Chat chat;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Chat {
            private String domain;      // 模型领域
            private double temperature; // 模型输出温度
            private int max_tokens;     // 最大 token 长度
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payload {
        private Message message;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Message {
            private String role;     // 角色类型（system/user/assistant）
            private String content;  // 对话内容
        }
    }
}
