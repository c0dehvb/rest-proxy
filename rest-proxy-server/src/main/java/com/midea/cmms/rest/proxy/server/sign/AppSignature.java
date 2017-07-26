package com.midea.cmms.rest.proxy.server.sign;

/**
 * 向Dubbo接口传递平台签名验证信息的数据类型
 */
public class AppSignature {
    private Long appId;
    private String appKey;
    private String signature;
    private Long tm;

    public AppSignature() {
    }

    public AppSignature(Long appId, String appKey, String signature, Long tm) {
        this.appId = appId;
        this.appKey = appKey;
        this.signature = signature;
        this.tm = tm;
    }

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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Long getTm() {
        return tm;
    }

    public void setTm(Long tm) {
        this.tm = tm;
    }

    @Override
    public String toString() {
        return "AppSignature{" +
                "appId=" + appId +
                ", appKey='" + appKey + '\'' +
                ", signature='" + signature + '\'' +
                ", tm=" + tm +
                '}';
    }
}
