package org.example.talker.util.Impl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.net.URLCodec;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author HumphreyLi
 * @version 1.0
 * @brief 提供讯飞星火认知大模型的认证 URL 生成工具类
 * @details 该类用于生成讯飞星火认知大模型的认证 URL，包括 API 密钥、签名和其他必要的参数
 * @date 2024/11/26
 * @time 上午1:49
 */
public class StarSparkAuthUtils {


    @Value("${spark.APPID}")
    private static String AppID ;
    @Value("${spark.APISecret}")
    private static String API_SECRET ;
    @Value("${spark.APIKey}")
    private static String API_KEY ;
    @Value("${spark.host}")
    private static String HOST;
    @Value("${spark.path}")
    private static String PATH;
    @Value("${spark.method}")
    private static String METHOD;

    /**
     * 生成讯飞星火认知大模型的认证 URL
     *
     * @return 认证 URL
     * @throws Exception 如果生成过程中发生错误
     */
    public static String generateAuthUrl() throws Exception {
        // 生成 date 参数
        String date = getFormattedDate();
        // 生成 tmp 字符串
        String tmp = "host: " + HOST + "\n" + "date: " + date + "\n" + METHOD + " " + PATH + " HTTP/1.1";
        // 使用 hmac-sha256 生成签名
        String signature = generateSignature(tmp, API_SECRET);
        // 生成 authorization_origin 字符串
        String authorizationOrigin = String.format("api_key=\"%s\", algorithm=\"hmac-sha256\", headers=\"host date request-line\", signature=\"%s\"", API_KEY, signature);
        // 将 authorization_origin 进行 base64 编码
        String authorization = Base64.getEncoder().encodeToString(authorizationOrigin.getBytes(StandardCharsets.UTF_8));
        // 构建最终的 URL
        return String.format("wss://%s%s?authorization=%s&date=%s&host=%s", HOST, PATH, URLEncoder.encode(authorization, StandardCharsets.UTF_8.toString()), URLEncoder.encode(date, StandardCharsets.UTF_8.toString()), URLEncoder.encode(HOST, StandardCharsets.UTF_8.toString()));
    }

    /**
     * 获取格式化的日期字符串
     *
     * @return 格式化的日期字符串
     */
    private static String getFormattedDate() {
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
    private static String generateSignature(String data, String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(rawHmac);
    }
}
