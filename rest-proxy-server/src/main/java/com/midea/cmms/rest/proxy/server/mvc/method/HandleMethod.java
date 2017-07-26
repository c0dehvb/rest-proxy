package com.midea.cmms.rest.proxy.server.mvc.method;

import org.springframework.web.cors.CorsConfiguration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 方法映射的基本操作单元
 */
public class HandleMethod {
//    private static final Logger LOG = Logger.getLogger(HandleMethod.class.getName());

    private String requestPattern;
    private String httpMethod;

    private boolean sign;
    private CorsConfiguration corsConfig;

    private String serviceClassName;
    private Object bean;

    private Method method;
    private HttpParameterInfo[] httpParameterInfos;

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getServiceClassName() {
        return serviceClassName;
    }

    public void setServiceClassName(String serviceClassName) {
        this.serviceClassName = serviceClassName;
    }

    public HttpParameterInfo[] getHttpParameterInfos() {
        return httpParameterInfos;
    }

    public void setHttpParameterInfos(HttpParameterInfo[] httpParameterInfos) {
        this.httpParameterInfos = httpParameterInfos;
    }

    public CorsConfiguration getCorsConfig() {
        return corsConfig;
    }

    public void setCorsConfig(CorsConfiguration corsConfig) {
        this.corsConfig = corsConfig;
    }

    public Object invoke(Object... args) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(bean, args);
    }

    public String getRequestPattern() {
        return requestPattern;
    }

    public void setRequestPattern(String requestPattern) {
        this.requestPattern = requestPattern;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public boolean isSign() {
        return sign;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return serviceClassName + "." + method.getName();
    }
}
