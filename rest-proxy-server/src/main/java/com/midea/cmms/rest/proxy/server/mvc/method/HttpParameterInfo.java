package com.midea.cmms.rest.proxy.server.mvc.method;

/**
 * 描述方法参数与Http参数之间的转换关系
 */
public class HttpParameterInfo {
    /**
     * 参数位置下标(从0开始)
     */
    private int index;

    /**
     * 参数类型
     */
    private Class<?> type;

    /**
     * 表单参数名
     */
    private String paramName;

    /**
     * 是否必填
     */
    private boolean required = false;

    /**
     * 默认值
     */
    private String defaultValue = null;

    /**
     * 该参数是否以请求体Json串形式转换来获取
     */
    private boolean requestBody = false;

    /**
     * 通过Rest URL参数形式获取
     */
    private boolean pathVariable = false;

    /**
     * 该参数记录签名验证信息
     * 如果为true, 参数类型只允许为字符串类型，该参数将会得到签名信息的Json串
     */
    private boolean appSign = false;

    /**
     * 参数自定义日期格式
     */
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
