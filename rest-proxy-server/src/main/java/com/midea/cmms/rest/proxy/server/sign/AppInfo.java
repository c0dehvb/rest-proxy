package com.midea.cmms.rest.proxy.server.sign;

/**
 * Created by liyilin on 2017/7/25.
 */
public class AppInfo {
    private Long appId;
    private String appKey;
    private String appSecret;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "appId=" + appId +
                ", appKey='" + appKey + '\'' +
                ", appSecret='" + appSecret + '\'' +
                '}';
    }
}