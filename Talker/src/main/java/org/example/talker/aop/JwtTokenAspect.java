package org.example.talker.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.talker.annotation.JwtToken;
import org.example.talker.dao.Redis.BloomFilterValidator;
import org.example.talker.util.Impl.AESUtilImpl;
import org.example.talker.util.Impl.JWTUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
/**
 * 将jwt token进行解密，并验证token是否有效
 *  无效则重定向到login
 * @author HumphreyLi
 * @version 1.0
 * @since 2024-11-16
 *
 */
@Aspect
@Component
public class JwtTokenAspect {

    private final JWTUtils jwtUtils;

    private Map<String, String> trueString;
    private String name;

    private final RedissonClient redisson;
    private final BloomFilterValidator bloomFilterValidator;

    @Autowired
    public JwtTokenAspect(RedissonClient redisson, JWTUtils jwtUtils,  BloomFilterValidator bloomFilterValidator) {
        this.redisson = redisson;
        this.jwtUtils = jwtUtils;

        this.bloomFilterValidator = bloomFilterValidator;
    }

    @Before("@annotation(jwtToken)")
    public void validateToken(JoinPoint joinPoint, JwtToken jwtToken) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = null;
        if (attributes != null) {
            //todo
            // 危险的强制转换
            response = (HttpServletResponse) attributes.getResponse();
        }

        // 获取并解密JWT token
        trueString = jwtUtils.validateTokenforUser(joinPoint, jwtToken);
        name = trueString.get("username");
        String type = jwtToken.type();

        // 使用布隆过滤器校验用户名是否存在
        if (!bloomFilterValidator.mightContain(name)) {
            if (response != null) {
                response.sendRedirect("/login");
            }
            return;
        }

        // 校验用户角色
        RBucket<String> bucket = redisson.getBucket(name);
        String role = bucket.get();
        if ("ADMIN".equals(type)) {
            if (!Objects.equals(role, "Admin")) {
                if (response != null) {
                    response.sendRedirect("/login");
                }
                throw new Exception("该用户不是管理员");
            }
        } else if ("USER".equals(type)) {
            if (!Objects.equals(role, "User")) {
                if (response != null) {
                    response.sendRedirect("/login");
                }
                throw new Exception("该用户不为用户");
            }
        } else {
            if (response != null) {
                response.sendRedirect("/login");
            }
            throw new Exception("token类型错误");
        }
    }
}
