package org.example.talker.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.example.talker.controller.http.Loginpost;
import org.example.talker.controller.http.Loginrequest;
import org.example.talker.dao.Redis.BloomFilterValidator;
import org.example.talker.service.Login;
import org.example.talker.util.Impl.AESUtil;
import org.example.talker.util.Impl.AESUtilImpl;
import org.example.talker.util.Impl.JWTUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 这个类负责处理用户登录相关的请求。
 * 它包含了登录验证、令牌生成等功能。
 * jwt不包含敏感信息
 * 校验成功的数据将出现在redis中，并且使用布隆过滤器进行过滤
 * @author Humphrey Li
 * @version 1.2
 * @since 2024-11-23
 */

@RestController
public class loginController {
    private static final Logger log = LoggerFactory.getLogger(loginController.class);
    @Autowired
    private Login login;
    private final RedissonClient redissonClient;

    private final BloomFilterValidator bloomFilterValidator;

    public loginController(RedissonClient redissonClient, BloomFilterValidator bloomFilterValidator) {
        this.redissonClient = redissonClient;
        this.bloomFilterValidator = bloomFilterValidator;
    }

    @PostMapping("/login")
    @SentinelResource(value = "login", blockHandler = "handleBlockException", fallback = "handleFallback")
    public CompletableFuture<Loginrequest> login(@RequestBody Loginpost request) {

        return CompletableFuture.supplyAsync(() -> {
            String token = "emm";
            String username = null;
            try {
                token = "empty";
                username = request.getName();
                String Fakepassword = request.getPassword();
                String email = request.getEmail();
                String role = null;
                Map<String, String> JWter = new HashMap<>();
//todo 加密
//                String encryptedPassword = aesUtil.decrypt(Fakepassword);
                String encryptedPassword = Fakepassword;
                if (email == null && username != null) {
                    role = login.loginforpassword(username, Fakepassword);
                }
                if (email != null && username == null) {
                    role = login.loginforEmail(email, Fakepassword);
                }
                if (role != null) {
                    JWter.put("email", email);
                    JWter.put("user", username);
                    JWter.put("role", role);
                    token = JWTUtils.getToken(JWter);
                    SetKeyInRedis(username, role);
                } else {
                    return new Loginrequest("error", "no username or password");
                }
                return new Loginrequest(token, username);
            } catch (Exception e) {
                log.error("login error", e);
                e.printStackTrace();
                return new Loginrequest(token, username);
            } finally {

                return new Loginrequest(token, username);
            }
        });
    }

    private void SetKeyInRedis(String Namekey, String Rolevalue) {
        //将username作为key，role作为value存入到redis中
        RBucket<String> bucket = redissonClient.getBucket(Namekey);
        bucket.set(Rolevalue);
        //将name存入布隆过滤器
        bloomFilterValidator.addToBloomFilter(Namekey);
    }

    public CompletableFuture<Loginrequest> handleBlockException(@RequestBody Loginpost request, BlockException exception) {
        return CompletableFuture.completedFuture(new Loginrequest("errorForBlock", "error"));
        //处理限流或降级后的逻辑
    }

    public CompletableFuture<Loginrequest> handleFallback(@RequestBody Loginpost request, BlockException exception) {
        return CompletableFuture.completedFuture(new Loginrequest("errorForFallback", "error"));
    }
}
