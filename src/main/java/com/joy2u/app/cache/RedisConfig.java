package com.joy2u.app.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisCluster;

@Component
public class RedisConfig {

    private final JedisCluster jedisCluster;

    @Autowired
    public RedisConfig(RedisConnectionFactory connectionFactory) {
        this.jedisCluster=(JedisCluster)connectionFactory.getClusterConnection().getNativeConnection();
    }

    public JedisCluster getJedisCluster() {
        return jedisCluster;
    }

}
