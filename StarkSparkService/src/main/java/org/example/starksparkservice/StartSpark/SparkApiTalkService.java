package org.example.talker.StartSpark;

import jakarta.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.redisson.api.RedissonClient;
/**
 * @author HumphreyLi
 * @version 1.0
 * @brief 实现api的调用，经过ai处理返回token值
 * @details 实现api的调用，经过ai处理返回token值，如果ai判断错误，
 * @date 2024/11/26
 * @time 上午12:46
 */
public class SparkApiTalkService {
   private String url;
   //用redis获取url;
   @Resource
   private RedissonClient redissonClient;

   public String SparkApiTalkService() throws Exception{
        url=redissonClient.getBucket("url").get().toString();//从redis中获取url

        //调用api，
       //在此处消费消息

        return  url;


   }

}
