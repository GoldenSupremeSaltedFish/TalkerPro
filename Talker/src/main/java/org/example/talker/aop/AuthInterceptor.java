package org.example.talker.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.example.talker.annotation.Authcheck;
@Component
@Aspect
public class AuthInterceptor {
    @Around("@annotation(authcheck)")
    public Object check(ProceedingJoinPoint joinPoint,Authcheck authcheck) throws Throwable {
        return joinPoint.proceed();
    }
}
