package com.jarvis.app.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

/**
 * spring boot 启动监听类 ApplicationStartedEvent：spring boot启动开始时执行的事件
 * @author jiayu.qiu
 */
public class ApplicationStartedEventListener implements ApplicationListener<ApplicationStartedEvent> {

    private static final Logger logger=LoggerFactory.getLogger(ApplicationStartedEventListener.class);

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        SpringApplication app=event.getSpringApplication();
        // app.setShowBanner(false);// 不显示banner信息
        logger.info("==" + this.getClass().getName() + "==");
    }

}
