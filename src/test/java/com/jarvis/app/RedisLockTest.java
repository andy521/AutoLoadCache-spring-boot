package com.jarvis.app;

import java.util.concurrent.atomic.LongAdder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Test
    public void testRedisLock() {
        System.out.println("  redisConfig is null:" + (null == redisConfig));
        JedisClusterLock lock=new JedisClusterLock(redisConfig.getJedisCluster());
        String lockKey="redis.lock.test.ttttttt";
        // lock.unlock(lockKey);
        final LongAdder adder=new LongAdder();
        Thread[] threads=new Thread[10];
        for(int i=0; i < threads.length; i++) {
            threads[i]=new Thread(new Runnable() {

                @Override
                public void run() {
                    adder.add(1);
                    System.out.println(Thread.currentThread().getName() + "waiting ....");
                    synchronized(lock) {
                        try {
                            lock.wait();
                        } catch(InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(Thread.currentThread().getName() + "notifyed ....");
                    if(lock.tryLock(lockKey, 10)) {
                        System.out.println(Thread.currentThread().getName() + "---get locked");
                        try {
                            lock.unlock(lockKey);
                            System.out.println(Thread.currentThread().getName() + "---unlocked");
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println(Thread.currentThread().getName() + "---lost locked");
                    }
                    adder.add(1);
                }
            });
            threads[i].setName("Thread" + i);
            threads[i].start();
        }
        while(adder.intValue() != 10) {
            try {
                Thread.sleep(2);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        synchronized(lock) {
            lock.notifyAll();
        }
        while(adder.intValue() != 20) {
            try {
                Thread.sleep(20);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
