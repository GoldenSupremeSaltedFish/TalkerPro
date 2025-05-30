package org.example.talker.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.talker.annotation.JwtToken;
import org.example.talker.dao.Redis.BloomFilterValidator;
import org.example.talker.util.Impl.JWTUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

/**
 * 将 JWT token 进行解密，并验证 token 是否有效。
 * 无效则重定向到 /login
 */
@Aspect
@Component
public class JwtTokenAspect {

    private final JWTUtils jwtUtils;
    private final RedissonClient redisson;
    private final BloomFilterValidator bloomFilterValidator;

    @Autowired
    public JwtTokenAspect(RedissonClient redisson, JWTUtils jwtUtils, BloomFilterValidator bloomFilterValidator) {
        this.redisson = redisson;
        this.jwtUtils = jwtUtils;
        this.bloomFilterValidator = bloomFilterValidator;
    }

    @Before("@annotation(jwtToken)")
    public void validateToken(JoinPoint joinPoint, JwtToken jwtToken) throws Exception {
        // 获取请求上下文，如果是在异步线程中，则手动传递上下文
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new IllegalStateException("No request context available");
        }

        // 将请求上下文传递给异步线程
        RequestContextHolder.setRequestAttributes(attributes);

        HttpServletResponse response = attributes.getResponse();
        Map<String, String> tokenData = jwtUtils.validateTokenforUser(joinPoint, jwtToken);

        String username = tokenData.get("user");
        String type = jwtToken.type();

        if (!bloomFilterValidator.mightContain(username)) {
            if (response != null) {
                response.sendRedirect("/login");
            }
            return;
        }

        RBucket<String> bucket = redisson.getBucket(username);
        String role = bucket.get();

        if ("ADMIN".equals(type) && !"Admin".equals(role)) {
            handleUnauthorized(response, "该用户不是管理员");
        } else if ("USER".equals(type) && !"User".equals(role)) {
            handleUnauthorized(response, "该用户不为用户");
        } else if (!"ADMIN".equals(type) && !"USER".equals(type)) {
            handleUnauthorized(response, "token类型错误");
        }
    }

    private void handleUnauthorized(HttpServletResponse response, String message) throws Exception {
        if (response != null) {
            response.sendRedirect("/login");
        }
        throw new Exception(message);
    }
}
