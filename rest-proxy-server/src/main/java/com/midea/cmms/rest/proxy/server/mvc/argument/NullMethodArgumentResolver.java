package com.midea.cmms.rest.proxy.server.mvc.argument;

import com.midea.cmms.rest.proxy.server.WebRequest;
import com.midea.cmms.rest.proxy.server.mvc.method.HandleMethod;
import com.midea.cmms.rest.proxy.server.mvc.method.HttpParameterInfo;

/**
 * 只返回NULL值的参数处理器
 * 当所有参数处理器都不支持时，最后轮到该参数处理器进行处理。
 * Created by liyilin on 2017/7/7.
 */
public class NullMethodArgumentResolver implements MethodArgumentResolver {
    @Override
    public boolean support(WebRequest request, HandleMethod method, HttpParameterInfo param) {
        return true;
    }

    @Override
    public Object resolve(WebRequest request, HandleMethod method, HttpParameterInfo param) throws Exception {
        return null;
    }

    @Override
    public int getOrder() {
        // 最低优先级
        return LOWEST_PRECEDENCE;
    }
}
