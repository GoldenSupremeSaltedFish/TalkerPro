package org.example.starksparkservice.StartSpark;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.example.starksparkservice.entity.sparkResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
@Component
public class StarkSparkApiService extends WebSocketListener {
    public static final String domain = "4.0Ultra";
    public static final String appid = "366419e5";

    public interface WebSocketCallback {
        void onResponse(String responseText);
        void onError(Exception e);
    }
    @Autowired
    private RedisTemplate<String, String> redisTemplateString;


    public void callApi(String messageContent, WebSocketCallback callback) {
        String url = getAuthUrl();
        if (url.startsWith("https://")) {
            url = url.replace("https://", "wss://");
        }

        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(url).build();
        WebSocket webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onMessage(WebSocket webSocket, String text) {
                try {
                    if (callback != null) {
                        callback.onResponse(text); // 将响应传递给回调
                    }
                } catch (Exception e) {
                    if (callback != null) {
                        callback.onError(e);
                    }
                }
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                if (callback != null) {
                    callback.onError(new Exception(t));
                }
            }
        });

        sendMessageToServer(webSocket, messageContent);

        new Thread(() -> {
            try {
                Thread.sleep(30000); // 超时 30 秒
                webSocket.close(1000, "Timeout");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private String getAuthUrl() {
        try {
            String redisValue = redisTemplateString.opsForValue().get("url1");
            if (redisValue == null) {
                throw new RuntimeException("从 Redis 获取的 URL 值为空！");
            }
            return redisValue;
        } catch (Exception e) {
            throw new RuntimeException("从 Redis 获取 URL 失败", e);
        }
    }

    public void sendMessageToServer(WebSocket webSocket, String messageContent) {
        if (messageContent == null || messageContent.trim().isEmpty()) {
            System.err.println("消息内容为空，无法发送到服务器！");
            return;
        }

        JSONObject requestJson = new JSONObject();
        JSONObject header = new JSONObject();
        header.put("app_id", appid);
//        header.put("uid", messageId);

        JSONObject parameter = new JSONObject();
        JSONObject chat = new JSONObject();
        chat.put("domain", domain);
        chat.put("temperature", 0.5);
        chat.put("max_tokens", 1024);
        parameter.put("chat", chat);

        JSONObject payload = new JSONObject();
        JSONObject message = new JSONObject();

        JSONArray textArray = new JSONArray();
        JSONObject textEntry = new JSONObject();
        textEntry.put("role", "user");
        textEntry.put("content", messageContent);
        textArray.add(textEntry);

        message.put("text", textArray);
        payload.put("message", message);

        requestJson.put("header", header);
        requestJson.put("parameter", parameter);
        requestJson.put("payload", payload);

        webSocket.send(requestJson.toString());
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        System.out.println(getFormattedDate() + " 大模型响应：" + text);

        try {
            Map<String, Object> apiResponse = JSONObject.parseObject(text, Map.class);
            sparkResponse response = sparkResponse.fromApiResponse(apiResponse);

            System.out.println("拼接消息：" + response.getContent());
            System.out.println("Token 消耗：" + response.getTokenUsage());
        } catch (Exception e) {
            System.err.println("解析响应失败：" + e.getMessage());
        }
    }

    private static @NotNull String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}

