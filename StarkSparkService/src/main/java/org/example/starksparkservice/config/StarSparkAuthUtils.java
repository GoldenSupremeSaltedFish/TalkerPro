package org.example.starksparkservice.config;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author HumphreyLi
 * @version 1.0
 * @brief 提供讯飞星火认知大模型的认证 URL 生成工具类
 * @details 该类用于生成讯飞星火认知大模型的认证 URL，包括 API 密钥、签名和其他必要的参数
 * @date 2024/11/26
 * @time 上午1:49
 */
@Service
public class StarSparkAuthUtils {

    // 将静态字段改为实例字段
    @Value("${spring.spark.APPID}")
    private String AppID;
    @Value("${spring.spark.APISecret}")
    private String API_SECRET;
    @Value("${spring.spark.APIKey}")
    private String API_KEY;
    @Value("${spring.spark.host}")
    private String HOST;
    @Value("${spring.spark.path}")
    private String PATH;
    @Value("${spring.spark.method}")
    private String METHOD;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(StarSparkAuthUtils.class);

    /**
     * 生成讯飞星火认知大模型的认证 URL
     *
     * @return 认证 URL
     * @throws Exception 如果生成过程中发生错误
     */
    public String generateAuthUrl() throws Exception {
//        logger.info("开始生成认证 URL");

        // 生成日期字符串
        String date = getFormattedDate();

        // 拼接 tmp 字符串（用于生成签名）
        String tmp = "host: " + HOST + "\n" + "date: " + date + "\n" + METHOD + " " + PATH + " HTTP/1.1";
//        logger.debug("tmp: " + tmp); // 打印拼接的 tmp 字符串

        // 生成签名
        String signature = generateSignature(tmp, API_SECRET);

        // 拼接 authorization_origin 字符串
        String authorizationOrigin = String.format("api_key=\"%s\", algorithm=\"hmac-sha256\", headers=\"host date request-line\", signature=\"%s\"", API_KEY, signature);
//        logger.debug("authorization_origin: " + authorizationOrigin); // 打印 authorization_origin

        // 对 authorization_origin 进行 Base64 编码
        String authorization = Base64.getEncoder().encodeToString(authorizationOrigin.getBytes(StandardCharsets.UTF_8));
//        logger.debug("authorization (Base64): " + authorization); // 打印编码后的 authorization

        // 构建最终的认证 URL
        return String.format("%s?authorization=%s&date=%s&host=%s", HOST,
                URLEncoder.encode(authorization, StandardCharsets.UTF_8.toString()),
                URLEncoder.encode(date, StandardCharsets.UTF_8.toString()),
                URLEncoder.encode(HOST, StandardCharsets.UTF_8.toString()));
    }

    /**
     * 获取格式化的日期字符串
     *
     * @return 格式化的日期字符串
     */
    private String getFormattedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(new Date());
    }

    /**
     * 生成 HMAC-SHA256 签名
     *
     * @param data 要签名的数据
     * @param secret 密钥
     * @return 签名
     * @throws Exception 如果签名过程中发生错误
     */
    private String generateSignature(String data, String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(rawHmac);
    }
}
