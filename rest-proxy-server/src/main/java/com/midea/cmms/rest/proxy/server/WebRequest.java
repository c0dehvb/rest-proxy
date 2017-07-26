package com.midea.cmms.rest.proxy.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by liyilin on 2017/7/7.
 */
public class WebRequest extends HttpServletRequestWrapper {
    private String requestMapping;
    private Map<String, String> pathVariables;

    private Gson gson;

    private static final String DEFAULT_DATE_PATTERN = "mvc.argument.datepattern";

    public WebRequest(HttpServletRequest request) {
        super(request);

        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
        Properties properties = webApplicationContext.getBean(Properties.class);
        String defaultDateFormat = properties.getProperty(DEFAULT_DATE_PATTERN);
        if (defaultDateFormat == null) defaultDateFormat = "yyyy-MM-dd HH:mm:ss";

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat(defaultDateFormat);
        gson = gsonBuilder.create();
    }

    public String getRequestMapping() {
        return requestMapping;
    }

    public void setRequestMapping(String requestMapping) {
        this.requestMapping = requestMapping;
        this.pathVariables = parsePathVariables(requestMapping, getRequestURI());
    }

    private byte[] inputStreamContent;          // 缓存InputStream内容

    /**
     * 该方法可重复调用，获取的InputStream不再是同一个
     * @return
     * @throws IOException
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (this.inputStreamContent == null) {
            ServletInputStream inputStream = super.getInputStream();
            this.inputStreamContent = StreamUtils.copyToByteArray(inputStream);
        }

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputStreamContent);
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }

    /**
     * 分析URI，获取PathVariable变量
     * @param requestPattern
     * @param requestUri
     * @return
     */
    public Map<String, String> parsePathVariables(String requestPattern, String requestUri) {
        String[] patternSplit = requestPattern.split("/");
        String[] uriSplit = requestUri.split("/");
        Map<String, String> pathVariables = new HashMap<>();

        for (int i = 0; i < patternSplit.length; i++) {
            String pattern = patternSplit[i];
            // {var}
            if (pattern.length() > 2 && pattern.startsWith("{") && pattern.endsWith("}")) {
                String varName = pattern.substring(1, pattern.length() - 1);
                pathVariables.put(varName, uriSplit[i]);
            }
        }
        return pathVariables;
    }

    public Map<String, String> getPathVariables() {
        return this.pathVariables;
    }

    public String getPathVariable(String varName) {
        return this.pathVariables.get(varName);
    }

    private JsonElement jsonBody;

    /**
     * 将表单body中的json串转为JsonObject对象返回
     * 该方法结果是有缓存的
     * @return
     */
    public JsonElement getJsonBody() throws IOException {
        if (jsonBody == null) {
            ServletInputStream inputStream = getInputStream();
            String json = StreamUtils.copyToString(inputStream, Charset.defaultCharset());
            jsonBody = gson.fromJson(json, JsonElement.class);
        }
        return jsonBody;
    }

    @Override
    public String getRequestURI() {
        String contextPath = getContextPath();
        return super.getRequestURI().substring(contextPath.length());
    }
}
