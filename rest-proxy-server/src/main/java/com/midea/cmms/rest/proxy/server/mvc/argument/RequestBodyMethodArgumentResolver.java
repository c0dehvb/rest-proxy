package com.midea.cmms.rest.proxy.server.mvc.argument;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.midea.cmms.rest.proxy.server.WebRequest;
import com.midea.cmms.rest.proxy.server.exception.MethodArgumentHandleException;
import com.midea.cmms.rest.proxy.server.mvc.method.HandleMethod;
import com.midea.cmms.rest.proxy.server.mvc.method.HttpParameterInfo;
import org.springframework.core.Ordered;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletInputStream;
import java.nio.charset.Charset;

/**
 * 对请求体中的Json字符串进行解析
 * Created by liyilin on 2017/7/7.
 */
public class RequestBodyMethodArgumentResolver extends AbstractMethodArgumentResolver {

    @Override
    public boolean support(WebRequest request, HandleMethod method, HttpParameterInfo param) {
        return param.isRequestBody();
    }

    @Override
    public Object resolve(WebRequest request, HandleMethod method, HttpParameterInfo param) throws Exception {
        ServletInputStream inputStream = request.getInputStream();
        String json = StreamUtils.copyToString(inputStream, Charset.forName("UTF-8"));
        Gson gson = getGson(param.getDateFormat());
        try {
            return gson.fromJson(json, param.getType());
        } catch (JsonSyntaxException e) {
            throw new MethodArgumentHandleException("请求体Json数据解析失败[" + param.getParamName() + "]");
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE-2;
    }
}
