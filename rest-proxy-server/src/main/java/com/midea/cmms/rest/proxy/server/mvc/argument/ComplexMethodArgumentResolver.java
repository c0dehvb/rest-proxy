package com.midea.cmms.rest.proxy.server.mvc.argument;

import com.midea.cmms.rest.proxy.server.WebRequest;
import com.midea.cmms.rest.proxy.server.mvc.method.HandleMethod;
import com.midea.cmms.rest.proxy.server.mvc.method.HttpParameterInfo;

/**
 * Created by liyilin on 2017/7/14.
 */
public class ComplexMethodArgumentResolver implements MethodArgumentResolver {
    private HttpRequestParameterMethodArgumentResolver httpRequestParameterMethodArgumentResolver;
    private JsonBodyMethodArgumentResolver jsonBodyMethodArgumentResolver;

    public ComplexMethodArgumentResolver() {
        httpRequestParameterMethodArgumentResolver = new HttpRequestParameterMethodArgumentResolver();
        jsonBodyMethodArgumentResolver = new JsonBodyMethodArgumentResolver();
    }

    @Override
    public boolean support(WebRequest webRequest, HandleMethod method, HttpParameterInfo param) {
        if (httpRequestParameterMethodArgumentResolver != null
                && httpRequestParameterMethodArgumentResolver.support(webRequest, method, param))
            return true;

        if (jsonBodyMethodArgumentResolver != null
                && jsonBodyMethodArgumentResolver.support(webRequest, method, param))
            return true;

        return false;
    }

    @Override
    public Object resolve(WebRequest request, HandleMethod method, HttpParameterInfo param) throws Exception {
        Object value = null;
        if (httpRequestParameterMethodArgumentResolver != null
                && httpRequestParameterMethodArgumentResolver.support(request, method, param))
            value = httpRequestParameterMethodArgumentResolver.resolve(request, method, param);

        if (jsonBodyMethodArgumentResolver != null
                && jsonBodyMethodArgumentResolver.support(request, method, param))
            value = jsonBodyMethodArgumentResolver.resolve(request, method, param);

        return value;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public HttpRequestParameterMethodArgumentResolver getHttpRequestParameterMethodArgumentResolver() {
        return httpRequestParameterMethodArgumentResolver;
    }

    public void setHttpRequestParameterMethodArgumentResolver(HttpRequestParameterMethodArgumentResolver httpRequestParameterMethodArgumentResolver) {
        this.httpRequestParameterMethodArgumentResolver = httpRequestParameterMethodArgumentResolver;
    }

    public JsonBodyMethodArgumentResolver getJsonBodyMethodArgumentResolver() {
        return jsonBodyMethodArgumentResolver;
    }

    public void setJsonBodyMethodArgumentResolver(JsonBodyMethodArgumentResolver jsonBodyMethodArgumentResolver) {
        this.jsonBodyMethodArgumentResolver = jsonBodyMethodArgumentResolver;
    }
}
