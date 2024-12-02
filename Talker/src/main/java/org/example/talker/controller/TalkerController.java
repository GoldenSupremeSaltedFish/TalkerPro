package org.example.talker.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;

import jakarta.annotation.Resource;
import org.example.talker.annotation.JwtToken;
import org.example.talker.controller.http.TalkerRequest;
import org.example.talker.service.Impl.talkerserviceimpl;
import org.example.talker.entity.TalkMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * 经过检验的请求，将消息发送到kafka以及调用ai
 *
 * @author HumphreyLi
 * @version 1.0
 * @since 2024-11-16
 *
 */


@RestController
public class TalkerController {
    @Autowired
    talkerserviceimpl talkerservice;
    @Resource
    org.example.talker.mapper.MessageMapper MessageMapper;

    @JwtToken// 使用JwtToken注解，表示需要进行JWT验证
    @PostMapping("/talker")
    @Async("taskExecutor")// 异步处理请求
    public CompletableFuture<Map<String, Object>> talker(@RequestBody TalkerRequest request) throws InterruptedException {

        return getMapCompletableFuture(request);
        // 返回一个CompletableFuture对象，表示异步操作的结果
    }

    private CompletableFuture<Map<String, Object>> getMapCompletableFuture(TalkerRequest request) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String shortUuid = uuid.substring(0, 5);

        TalkMessage talkMessage = new TalkMessage(shortUuid, request.getBody(), request.getSenderId(), request.getReceiverId());
        Map<String, Object> response = new HashMap<>();

        if (!talkerservice.IsMessageRight(talkMessage)) {
            response.put("status", "error");
            response.put("code", "ERR_" + shortUuid);
            response.put("message", "消息发送失败");
            return CompletableFuture.completedFuture(response);
        }

        // 发送消息并异步接收
        return talkerservice.MessagetoKafkaAsync(talkMessage)
                .thenCompose(unused -> talkerservice.MessagefromKafkaAsync(shortUuid)) // 等待 Kafka 消息
                .thenApply(receivedMessage -> {
                    if (receivedMessage != null) {
                        response.put("status", "success");
                        response.put("code", "success_" + shortUuid);
                        response.put("message", receivedMessage.getMessage());
                    } else {
                        response.put("status", "error");
                        response.put("code", "TIMEOUT_" + shortUuid);
                        response.put("message", "消息接收超时");
                    }
                    return response;
                });
    }


    /**
     * 限流版talker
     */
    @JwtToken// 使用JwtToken注解，表示需要进行JWT验证
    @PostMapping("/talker/User")
    @Async("taskExecutor")// 异步处理请求,线程池支持

    //如果不使用注解，那么需要手动提交
    public CompletableFuture<Map<String, Object>> talkerForHotKey(@RequestBody TalkerRequest request)
    {
         //基于ip限流

        String remoteAddr = request.getSenderId();
        Entry entry=null;// 创建一个Entry对象，用于记录进入Sentinel的规则
        try {
            entry= SphU.entry("", EntryType.IN,10,remoteAddr);// 进入Sentinel的规则
            //被保护的业务逻辑
            return getMapCompletableFuture(request);

        }
        catch(Throwable ex)// 如果被限流，则抛出BlockException异常
        {
            if(!BlockException.isBlockException(ex))
            {
                Tracer.trace(ex);//业务异常进行上报降级
                return CompletableFuture.completedFuture(new HashMap<>());
            }
            if(ex instanceof DegradeException)
            {
                // 降级处理

                //直接调用mini模型的api

            }

        }
        finally {
            if(entry!=null)
            {
                if(entry!=null)
                {
                    entry.exit(1,remoteAddr);
                }
            }
        }
        return null;
    }

}
