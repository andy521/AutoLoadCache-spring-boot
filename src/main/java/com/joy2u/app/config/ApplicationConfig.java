package com.joy2u.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConfig {

    private @Value("${dev}") boolean dev;

    public boolean isDev() {
        return dev;
    }

    public void setDev(boolean dev) {
        this.dev=dev;
    }
}
