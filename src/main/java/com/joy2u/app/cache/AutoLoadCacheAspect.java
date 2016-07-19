package com.joy2u.app.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jarvis.cache.aop.aspectj.AspectjAopInterceptor;


@Aspect
@Component
public class AutoLoadCacheAspect {

    @Autowired
    private AspectjAopInterceptor aspectjAopInterceptor;

    @Pointcut("execution(public !void com.joy2u.app.dao..*.*(..))")
    public void aspect(){}

    @Around("aspect()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint)throws Throwable{
        System.out.println("do cache around");
        return aspectjAopInterceptor.checkAndProceed(proceedingJoinPoint);
    }
}
