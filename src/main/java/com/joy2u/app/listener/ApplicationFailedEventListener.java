package com.joy2u.app.listener;

import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;


/**
 * ApplicationFailedEvent:spring boot启动异常时执行事件 
 * @author jiayu.qiu
 *
 */
public class ApplicationFailedEventListener implements ApplicationListener<ApplicationFailedEvent> {

    @Override
    public void onApplicationEvent(ApplicationFailedEvent event) {
        Throwable throwable = event.getException();
        handleThrowable(throwable);
    }
    /*处理异常*/
    private void handleThrowable(Throwable throwable) {
    }
}
