package com.midea.cmms.rest.proxy.server.mvc.method;

/**
 * Created by liyilin on 2017/7/3.
 */
public class HttpParameterInfo {
    private int index;
    private Class<?> type;
    private String paramName;
    private boolean required = false;
    private String defaultValue = null;
    private boolean requestBody = false;
    private boolean pathVariable = false;
    private boolean appSign = false;
    private String dateFormat;

    public HttpParameterInfo(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isRequestBody() {
        return requestBody;
    }

    public void setRequestBody(boolean requestBody) {
        this.requestBody = requestBody;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public boolean isPathVariable() {
        return pathVariable;
    }

    public void setPathVariable(boolean pathVariable) {
        this.pathVariable = pathVariable;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public boolean isAppSign() {
        return appSign;
    }

    public void setAppSign(boolean appSign) {
        this.appSign = appSign;
    }
}
