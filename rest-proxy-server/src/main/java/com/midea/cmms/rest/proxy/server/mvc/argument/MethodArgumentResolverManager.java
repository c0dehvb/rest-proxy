package com.midea.cmms.rest.proxy.server.mvc.argument;

import com.midea.cmms.rest.proxy.server.WebRequest;
import com.midea.cmms.rest.proxy.server.exception.MethodArgumentHandleException;
import com.midea.cmms.rest.proxy.server.mvc.method.HandleMethod;
import com.midea.cmms.rest.proxy.server.mvc.method.HttpParameterInfo;

import java.util.List;

/**
 * Created by liyilin on 2017/7/4.
 */
public class MethodArgumentResolverManager {
    List<MethodArgumentResolver> methodArgumentResolvers;

    public List<MethodArgumentResolver> getMethodArgumentResolvers() {
        return methodArgumentResolvers;
    }

    public void setMethodArgumentResolvers(List<MethodArgumentResolver> methodArgumentResolvers) {
        this.methodArgumentResolvers = methodArgumentResolvers;
    }

    public Object resolve(WebRequest request, HandleMethod method, HttpParameterInfo param) throws MethodArgumentHandleException {
        for (MethodArgumentResolver methodArgumentResolver : methodArgumentResolvers) {
            // 查询支持该类型参数转换的处理器
            if (methodArgumentResolver.support(request, method, param)) {
                try {
                    // 进行参数转换
                    return methodArgumentResolver.resolve(request, method, param);
                } catch (Exception e) {
                    throw new MethodArgumentHandleException("参数[" + param.getParamName() + "]转换异常: " + e.getMessage() ,e);
                }
            }
        }
        return null;
    }
}
