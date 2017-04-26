package com.jarvis.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.jarvis.app.cache.RedisConfig;
import com.jarvis.cache.lock.JedisClusterLock;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedisLockTest {

    @Autowired
    private RedisConfig redisConfig;

    @Value("${spring.profiles.active}")
    private String profile;

    @Test
    public void testRedisLock() {
        System.out.println(profile + "  redisConfig is null:" + (null == redisConfig));
        JedisClusterLock lock=new JedisClusterLock(redisConfig.getJedisCluster());
        String lockKey="redis.lock.test.ttttttt";
        lock.unlock(lockKey);
        Thread[] threads=new Thread[10];
        for(int i=0; i < threads.length; i++) {
            threads[i]=new Thread(new Runnable() {

                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + "waiting ....");
                    synchronized(lock) {
                        try {
                            lock.wait();
                        } catch(InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if(lock.tryLock(lockKey, 10)) {
                        System.out.println("---get locked");
                        try {
                            Thread.sleep(30000);
                            lock.unlock(lockKey);
                        } catch(InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            threads[i].start();
        }
        try {
            Thread.sleep(4);// 这里一定要sleep一下，否则notifyAll时，有可能线程还没启来，造成无法通知到线程。
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        synchronized(lock) {
            lock.notifyAll();
        }
    }
}
