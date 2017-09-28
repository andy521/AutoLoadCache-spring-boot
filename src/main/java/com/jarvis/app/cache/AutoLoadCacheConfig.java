package com.jarvis.app.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jarvis.cache.CacheHandler;
import com.jarvis.cache.ComboCacheManager;
import com.jarvis.cache.ICacheManager;
import com.jarvis.cache.aop.aspectj.AspectjAopInterceptor;
import com.jarvis.cache.lock.JedisClusterLock;
import com.jarvis.cache.map.MapCacheManager;
import com.jarvis.cache.redis.JedisClusterCacheManager;
import com.jarvis.cache.script.AbstractScriptParser;
import com.jarvis.cache.script.OgnlParser;
import com.jarvis.cache.serializer.ISerializer;
import com.jarvis.cache.serializer.JacksonJsonSerializer;
import com.jarvis.cache.to.AutoLoadConfig;

@Configuration
public class AutoLoadCacheConfig {

    @Autowired
    private RedisConfig redisConfig;

    @Bean
    public AutoLoadConfig autoLoadConfig() {
        AutoLoadConfig autoLoadConfig=new AutoLoadConfig();
        autoLoadConfig.setNamespace("test");
        autoLoadConfig.setThreadCnt(10); // threadCnt :处理自动加载队列的线程数量,默认值为10
        autoLoadConfig.setMaxElement(2000); // 自动加载队列中允许存放的最大容量，默认值为20000
        autoLoadConfig.setPrintSlowLog(true); // 是否打印比较耗时的请求，默认值：true
        autoLoadConfig.setSlowLoadTime(500); // 当请求耗时超过此值时，记录目录（printSlowLog=true 时才有效），单位：毫秒；默认值500
        autoLoadConfig.setSortType(1); // 自动加载队列排序算法, 0：按在Map中存储的顺序（即无序）；1 ：越接近过期时间，越耗时的排在最前；2：根据请求次数，倒序排序，请求次数越多，说明使用频率越高，造成并发的可能越大
        autoLoadConfig.setCheckFromCacheBeforeLoad(true); // 加载数据之前去缓存服务器中检查，数据是否快过期，如果应用程序部署的服务器数量比较少，设置为false, 如果部署的服务器比较多，可以考虑设置为true
        autoLoadConfig.setAutoLoadPeriod(50); // 单个线程中执行自动加载的时间间隔, 此值越小，遍历自动加载队列频率起高，对CPU会越消耗CPU
        autoLoadConfig.setLoadDataTryCnt(2);//
        return autoLoadConfig;
    }

    @Bean
    public AbstractScriptParser scriptParser() {// 推荐使用：OGNL
        return new OgnlParser();
    }

    @Bean
    public ISerializer<Object> serializer() {// 推荐使用：Hessian
        //return new HessianSerializer();
        return new JacksonJsonSerializer();
    }

    @Bean(initMethod="start", destroyMethod="destroy")
    public ICacheManager localCacheManager() {
        MapCacheManager manager=new MapCacheManager(autoLoadConfig(), serializer());
        // 根据需要自行配置
        // manager.setClearAndPersistPeriod(clearAndPersistPeriod);
        // manager.setCopyValue(copyValue);
        // manager.setNeedPersist(needPersist);
        // manager.setPersistFile(persistFile);
        // manager.setUnpersistMaxSize(unpersistMaxSize);
        return manager;
    }

    @Bean
    public ICacheManager remoteCacheManager() {
        JedisClusterCacheManager manager=new JedisClusterCacheManager(serializer());
        manager.setJedisCluster(redisConfig.getJedisCluster());
        // 根据需要自行配置
        // manager.setHashExpire(hashExpire);
        // manager.setHashExpireByScript(hashExpireByScript);
        return manager;
    }

    @Bean
    public ComboCacheManager comboCacheManager() {
        return new ComboCacheManager(localCacheManager(), remoteCacheManager(), scriptParser());
    }

    @Bean(destroyMethod="destroy")
    public CacheHandler cacheHandler() {
        CacheHandler cacheHandler=new CacheHandler(remoteCacheManager(), scriptParser(), autoLoadConfig(), serializer());
        cacheHandler.setLock(new JedisClusterLock(redisConfig.getJedisCluster()));// 开启分布式锁
        return cacheHandler;
    }

    @Bean
    public AspectjAopInterceptor aspectjAopInterceptor() {
        return new AspectjAopInterceptor(cacheHandler());
    }
}
