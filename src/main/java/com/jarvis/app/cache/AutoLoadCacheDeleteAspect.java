package com.jarvis.app.cache;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.jarvis.cache.annotation.CacheDelete;
import com.jarvis.cache.aop.aspectj.AspectjAopInterceptor;

@Aspect
@Component
@Order(1000)
public class AutoLoadCacheDeleteAspect {

    private static final Logger logger=LoggerFactory.getLogger(AutoLoadCacheDeleteAspect.class);

    @Autowired
    private AspectjAopInterceptor aspectjAopInterceptor;

    /**
     * 如果@Cache不是用在接口中，可以按下面方法来配置
     */
    @Pointcut("execution(* com.joy2u.app.dao..*.*(..)) && @annotation(cacheDelete)")
    public void cacheDeleteAspect(CacheDelete cacheDelete) {
    }

    @AfterReturning(value="cacheDeleteAspect(cacheDelete)", returning="retVal")
    public void cacheDelete(JoinPoint aopProxyChain, CacheDelete cacheDelete, Object retVal) throws Throwable {
        logger.debug("start do cacheDelete ... ...");
        aspectjAopInterceptor.deleteCache(aopProxyChain, cacheDelete, retVal);
    }
}
