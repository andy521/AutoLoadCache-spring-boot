package com.joy2u.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 使用@ConfigurationProperties需要指定prefix,同时bean中的属性和配置参数名保持一致。
 * @author jiayu.qiu
 */
@Component
@ConfigurationProperties(prefix="redis", locations = "classpath:redis.properties")
public class RedisConfig {

    private String ip;

    private Integer port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip=ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port=port;
    }

}
