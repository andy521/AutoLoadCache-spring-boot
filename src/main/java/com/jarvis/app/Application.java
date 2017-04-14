package com.jarvis.app;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.jarvis.app.listener.ApplicationEnvironmentPreparedEventListener;
import com.jarvis.app.listener.ApplicationFailedEventListener;
import com.jarvis.app.listener.ApplicationPreparedEventListener;
import com.jarvis.app.listener.ApplicationStartedEventListener;

// @Profile("development")
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return configureApplication(builder);
    }

    public static void main(String[] args) {
        configureApplication(new SpringApplicationBuilder()).run(args);
    }

    private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {
        ApplicationListener<?> listeners[]=new ApplicationListener[]{new ApplicationStartedEventListener(), new ApplicationEnvironmentPreparedEventListener(), new ApplicationFailedEventListener(),
            new ApplicationPreparedEventListener(),};
        return builder.sources(Application.class).bannerMode(Banner.Mode.OFF).listeners(listeners);
    }
}
