package org.example.talker.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import org.example.talker.controller.http.TalkerRequest;
import org.example.talker.entity.TalkMessage;
import org.example.talker.service.Impl.talkerserviceimpl;
import org.example.talker.util.Impl.JWTUtils;
import org.example.talker.dao.Redis.BloomFilterValidator;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 经过检验的请求，将消息发送到kafka以及调用ai
 *
 * @author HumphreyLi
 * @version 1.0
 * @since 2024-11-16
 */
@RestController
public class TalkerController {

    @Autowired
    talkerserviceimpl talkerservice;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private RedissonClient redisson;

    @Autowired
    private BloomFilterValidator bloomFilterValidator;

    private final ConcurrentHashMap<String, CompletableFuture<TalkMessage>> messageContext = new ConcurrentHashMap<>();

    // ThreadLocal 用于存储请求上下文
    private static final ThreadLocal<Map<String, Object>> requestContext = ThreadLocal.withInitial(HashMap::new);

    @PostMapping("/talker")
    @Async("taskExecutor") // 异步处理请求
    public CompletableFuture<Map<String, Object>> talker(@RequestBody TalkerRequest request, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (!validateJwt(serverHttpRequest, serverHttpResponse, "user")) {
            return CompletableFuture.completedFuture(buildErrorResponse("JWT 验证失败"));
        }
        return handleRequest(request);
    }

    @PostMapping("/test")
    @Async("taskExecutor")
    public CompletableFuture<Map<String, Object>> test(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (!validateJwt(serverHttpRequest, serverHttpResponse, "user")) {
            return CompletableFuture.completedFuture(buildErrorResponse("JWT 验证失败"));
        }
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Test endpoint is working!");
        return CompletableFuture.completedFuture(response);
    }

    private boolean validateJwt(ServerHttpRequest request, ServerHttpResponse response, String requiredRole) {
        try {
            boolean entryAllowed = false;
            String token = request.getHeaders().getFirst("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                System.out.println("token is null");

            }
            token = token.substring(7);

            // 验证 JWT 并提取数据
            Map<String, String> tokenData = jwtUtils.tokentomap(token);
            String username = tokenData.get("user");

            String type = tokenData.get("type");
            System.out.println(username+type+token);

            if (!bloomFilterValidator.mightContain(username)) {
                handleUnauthorized(response, "用户不存在");
                return false;
            }

            RBucket<String> bucket = redisson.getBucket(username);
            String role = bucket.get();
            if (!isRoleValid(type, role, requiredRole)) {
                handleUnauthorized(response, "用户角色不匹配");
                return false;
            }

            // 验证通过
            return true;
        } catch (Exception e) {
            handleUnauthorized(response, e.getMessage());
            return false;
        }
    }

    private boolean isRoleValid(String type, String role, String requiredRole) {
        return requiredRole.equals(role) && requiredRole.equalsIgnoreCase(type);
    }

    private void handleUnauthorized(ServerHttpResponse response, String message) {
        response.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
        response.writeWith(Mono.just(
                DataBufferUtils.join(
                        Mono.just(response.bufferFactory().wrap(message.getBytes()))
                )
        ).block());
    }

    private Map<String, Object> buildErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", message);
        return response;
    }

    private CompletableFuture<Map<String, Object>> handleRequest(TalkerRequest request) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String shortUuid = uuid.substring(0, 5);

        // 将请求上下文（例如 user 信息）存储在 ThreadLocal 中
        Map<String, Object> context = requestContext.get();
        context.put("senderId", request.getSenderId());
        context.put("receiverId", request.getReceiverId());
        requestContext.set(context);

        TalkMessage talkMessage = new TalkMessage(shortUuid, request.getBody(), request.getSenderId(), request.getReceiverId());
        Map<String, Object> response = new HashMap<>();

        // 校验消息是否正确
        if (!talkerservice.IsMessageRight(talkMessage)) {
            response.put("status", "error");
            response.put("code", "ERR_" + shortUuid);
            response.put("message", "消息发送失败");
            return CompletableFuture.completedFuture(response);
        }

        // 创建异步任务并添加到上下文中
        CompletableFuture<TalkMessage> future = new CompletableFuture<>();
        messageContext.put(shortUuid, future);

        talkerservice.MessagetoKafkaAsync(talkMessage).thenRun(() -> {
            talkerservice.MessagefromKafkaAsync(shortUuid).thenAccept(receivedMessage -> {
                if (receivedMessage != null) {
                    future.complete(receivedMessage);
                } else {
                    future.completeExceptionally(new RuntimeException("消息接收超时"));
                }
            });
        });

        // 处理接收到的消息并构建响应
        return future.thenApply(receivedMessage -> {
            response.put("status", "success");
            response.put("code", "success_" + shortUuid);
            response.put("message", receivedMessage.getMessage());
            messageContext.remove(shortUuid); // 完成后从上下文中移除
            requestContext.remove(); // 清除当前线程的上下文
            return response;
        }).exceptionally(ex -> {
            response.put("status", "error");
            response.put("code", "ERR_" + shortUuid);
            response.put("message", ex.getMessage());
            messageContext.remove(shortUuid); // 错误后移除
            requestContext.remove(); // 清除当前线程的上下文
            return response;
        });
    }

    @PreDestroy
    public void cleanUp() {
        requestContext.remove();
    }
}
