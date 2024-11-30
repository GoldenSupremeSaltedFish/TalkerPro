package org.example.starksparkservice.StartSpark;
import com.alibaba.nacos.shaded.com.google.gson.Gson;
import jakarta.annotation.Resource;
import org.example.starksparkservice.entity.DeductionRecord;
import org.example.starksparkservice.entity.KafkaMessage;
import org.example.starksparkservice.entity.sparkResponse;
import org.example.starksparkservice.mapper.DeductionRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.google.gson.reflect.TypeToken;

import static org.example.starksparkservice.entity.transform.SparkToKafka;

/**
 * @author HumphreyLi
 * @version 1.0
 * @brief 实现api的调用，经过ai处理返回token值
 * @details 实现api的调用，经过ai处理返回token值，如果ai判断错误，
 * @date 2024/11/26
 * @time 上午12:46
 */

@Service
public class SparkApiTalkService {


   private StringBuilder contentBuilder = new StringBuilder();
   private int totalTokenUsage = 0;
   @Resource
   private DeductionRecordMapper deductionRecordMapper;

   @Value("${spark.clusternum}")
   String serviced;

   @Autowired
   private StarkSparkApiService starkSparkApiService;

   @Autowired
   private KafkaTemplate<String, KafkaMessage> kafkaTemplate;


   private static final long QPS_LIMIT = 2;
   private final AtomicLong lastConsumeTime = new AtomicLong(System.nanoTime());


   @KafkaListener(
           topics = "${kafka.topics.SendToSparkNormal}",
           groupId = "${kafka.consumer.group-id}",
           containerFactory = "kafkaListenerContainerFactory"
   )
   public void consumeTopic(KafkaMessage message) {
      rateLimit();
      processMessage(message, "SendToSparkVip");
   }

   private void processMessage(KafkaMessage message, String topic) {


      invokeSparkApi(message);
      //由于业务的延迟性很高，直接在这里更新偏移量
   }



   void invokeSparkApi(KafkaMessage message) {
      String sender = message.getSender();
      String messageId = message.getMessageId();
      String messageContent = message.getContent();

      try {
         // 调用 WebSocket API，使用回调处理响应
         starkSparkApiService.callApi(messageContent, new StarkSparkApiService.WebSocketCallback() {
            @Override
            public void onResponse(String responseText) {
               handleResponse(messageId, sender, responseText);
            }

            @Override
            public void onError(Exception e) {
               System.err.println("WebSocket Error for Message ID: " + messageId);
//               e.printStackTrace();
            }
         });
      } catch (Exception e) {
         System.err.println("Error during invokeSparkApi: " + e.getMessage());
      }
   }

   private void handleResponse(String messageId, String sid, String responseText) {
      try {
         // 解析响应并生成 sparkResponse 对象

         Gson gson=new Gson();
         Map<String, Object> apiResponse = gson.fromJson(responseText, new TypeToken<Map<String, Object>>() {}.getType());
         sparkResponse response = sparkResponse.fromApiResponse(apiResponse);//

         System.out.println("Received response for Message ID: " + messageId);//
         System.out.println("Response Content: " + response.getContent());
         //调用reputekafka+sid
         KafkaMessage kafkaMessage=SparkToKafka(response, sid, serviced);// 调用方法，传入 response 对象, 并传入 sid（sender用来查找）, 并传入 当前apiid（扩展）
         // input messageid,test,token(冗余字段可以做双向对账)


         // 更新数据库
         updateDatabase(sid, response.getTokenUsage());

         sendToAcceptQueue(kafkaMessage);

      } catch (Exception e) {
         System.err.println("Failed to process WebSocket response for Message ID: " + messageId);
         e.printStackTrace();
      }
   }

   private void rateLimit() {
      long currentTime = System.nanoTime();
      long elapsedTime = currentTime - lastConsumeTime.get();

      if (TimeUnit.NANOSECONDS.toMillis(elapsedTime) < 1000 / QPS_LIMIT) {
         try {
            TimeUnit.MILLISECONDS.sleep(1000 / QPS_LIMIT - TimeUnit.NANOSECONDS.toMillis(elapsedTime));
         } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
         }
      }
      lastConsumeTime.set(System.nanoTime());
   }
   private void updateDatabase(String userID, int tokenused)
   {
      deductionRecordMapper.uodatepoint(userID,tokenused);
      DeductionRecord deductionRecord = new DeductionRecord(userID, tokenused,"${spark.clusternum}");
      deductionRecordMapper.insertDeductionRecord(deductionRecord);
   };


   //已经设置为true自动提交
//   private void updateKafkaOffset(String messageId) {
//
//      System.out.println("更新 Kafka 偏移量 for messageId: " + messageId);
//
//      KafkaMessage offsetUpdateMessage = new KafkaMessage();
//      offsetUpdateMessage.setMessageId(messageId);
//      offsetUpdateMessage.setContent("Offset updated");
//      kafkaTemplate.send("${kafka.topics.OffsetUpdate}", offsetUpdateMessage);
//   }

   private void sendToAcceptQueue(KafkaMessage kafkaMessage) {

      kafkaTemplate.send("${kafka.topics.CallbackTopic}", kafkaMessage);
   }
}
