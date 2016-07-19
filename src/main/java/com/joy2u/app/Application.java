package com.joy2u.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.joy2u.app.listener.ApplicationEnvironmentPreparedEventListener;
import com.joy2u.app.listener.ApplicationFailedEventListener;
import com.joy2u.app.listener.ApplicationPreparedEventListener;
import com.joy2u.app.listener.ApplicationStartedEventListener;

//@Profile("development")
@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        ApplicationListener<?> listeners[]=new ApplicationListener[]{
            new ApplicationStartedEventListener(),
            new ApplicationEnvironmentPreparedEventListener(),
            new ApplicationFailedEventListener(),
            new ApplicationPreparedEventListener(),
        };
        app.addListeners(listeners);
        app.run(args);
    }
}
