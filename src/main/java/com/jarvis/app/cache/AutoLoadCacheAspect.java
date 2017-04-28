package com.jarvis.app.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jarvis.cache.annotation.Cache;
import com.jarvis.cache.aop.aspectj.AspectjAopInterceptor;

@Aspect
@Component
public class AutoLoadCacheAspect {

    private static final Logger logger=LoggerFactory.getLogger(AutoLoadCacheAspect.class);

    @Autowired
    private AspectjAopInterceptor aspectjAopInterceptor;

    /**
     * 如果@Cache不是用在接口中，可以按下面方法来配置
     */
    @Pointcut("execution(public !void com.jarvis.app.dao..*.*(..)) && @annotation(cache)")
    public void aspect(Cache cache) {
    }

    @Around("aspect(cache)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, Cache cache) throws Throwable {
        logger.debug("do cache around");
        return aspectjAopInterceptor.proceed(proceedingJoinPoint, cache);
    }

}