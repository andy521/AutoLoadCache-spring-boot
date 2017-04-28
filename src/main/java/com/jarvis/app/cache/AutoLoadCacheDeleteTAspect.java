package com.jarvis.app.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jarvis.cache.annotation.CacheDeleteTransactional;
import com.jarvis.cache.aop.aspectj.AspectjAopInterceptor;

@Aspect
@Component
public class AutoLoadCacheDeleteTAspect {

    private static final Logger logger=LoggerFactory.getLogger(AutoLoadCacheDeleteTAspect.class);

    @Autowired
    private AspectjAopInterceptor aspectjAopInterceptor;

    /**
     * 拦截带@CacheDeleteTransactional注解的方法
     */
    @Pointcut("execution(* com.jarvis.app..*.*(..)) && @annotation(cacheDeleteTransactional)")
    public void aspect(CacheDeleteTransactional cacheDeleteTransactional) {
    }

    @Around("aspect(cacheDeleteTransactional)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, CacheDeleteTransactional cacheDeleteTransactional) throws Throwable {
        logger.debug("do cache around");
        return aspectjAopInterceptor.deleteCacheTransactional(proceedingJoinPoint, cacheDeleteTransactional);
    }

}