package org.example.talker.util.Impl;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.example.talker.Exception.UnauthorizedException;
import org.example.talker.annotation.JwtToken;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * 这个类用于实现JWT的接口，并实现JWT的功能。
 *
 * @author HumphreyLi
 * @version 1.0
 * @since 2024-11-20
 *
 */

@Service
    public class JWTUtils {

        /**
         * 生成token  header.payload.singature
         */
        private static final String SING = "HUMPHREYLI";
        // 密钥盐


        public static String getToken(Map<String, String> map) {

            Calendar instance = Calendar.getInstance();
            // 默认7天过期
            instance.add(Calendar.DATE, 7);

            //创建jwt builder
            JWTCreator.Builder builder = JWT.create();

            // payload
            map.forEach(builder::withClaim);

            String token = builder.withExpiresAt(instance.getTime())  //指定令牌过期时间
                    .sign(Algorithm.HMAC256(SING));  // sign
            return token;
        }

        //getMessage
        @Before("@annotation(jwtToken)")
        public Map<String, String> validateTokenforUser(JoinPoint joinPoint, JwtToken jwtToken) throws Exception {
            // 1. 获取令牌
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            HttpServletRequest request = (HttpServletRequest) attributes.getRequest();
            HttpServletResponse response = (HttpServletResponse) attributes.getResponse();

            String token = extractToken(request);
            if (token == null) {
                rejectRequest(response, "No JWT token found in request headers");
                return null;
            }

            try {
                Algorithm algorithm = Algorithm.HMAC256(SING);
                DecodedJWT jwt = JWT.require(algorithm).build().verify(token);

                Map<String, String> tokenData = new HashMap<>();
                tokenData.put("email", jwt.getClaim("email").asString());
                tokenData.put("user", jwt.getClaim("user").asString());
                tokenData.put("role", jwt.getClaim("role").asString());

                return tokenData;
            } catch (JWTVerificationException exception) {
                throw new UnauthorizedException("Invalid JWT token");
            }
        }
        private String extractToken(HttpServletRequest request) {
            String bearerToken = request.getHeader("Authorization");
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7);
            }
            return null;
        }
        private void rejectRequest(HttpServletResponse response, String message) throws IOException, IOException {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");// 设置响应内容类型为JSON
            response.getWriter().write("{\"error\": \"" + message + "\"}");
            response.getWriter().flush();// 刷新输出流
        }
    public Map<String, String> tokentomap(String token) throws Exception {

        try {
            Algorithm algorithm = Algorithm.HMAC256(SING);
            DecodedJWT jwt = JWT.require(algorithm).build().verify(token);

            // 提取所有所需的 Claim 值
            Map<String, String> tokenData = new HashMap<>();
            tokenData.put("email", jwt.getClaim("email").asString());
            tokenData.put("user", jwt.getClaim("user").asString());
            tokenData.put("role", jwt.getClaim("role").asString());


            return tokenData;
        } catch (JWTVerificationException exception) {
            throw new UnauthorizedException("Invalid JWT token");
        }
    }


}

