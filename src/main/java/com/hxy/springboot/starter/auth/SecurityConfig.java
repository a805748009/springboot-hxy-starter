package com.hxy.springboot.starter.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("starter.security")
public class SecurityConfig {

    /**
     * 过期时间毫秒值
     */
    private int sessionTimeOut = 259200;

    /**
     * 是否到期前自动刷新过期时间
     */
    private boolean autoRefresh = false;

    public boolean isAutoRefresh() {
        return autoRefresh;
    }

    public void setAutoRefresh(boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

    public int getSessionTimeOut() {
        return sessionTimeOut;
    }

    public void setSessionTimeOut(int sessionTimeOut) {
        this.sessionTimeOut = sessionTimeOut;
    }


}
