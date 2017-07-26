package com.midea.cmms.rest.proxy.server.mvc.argument;

import com.midea.cmms.rest.proxy.server.WebRequest;
import com.midea.cmms.rest.proxy.server.exception.MethodArgumentHandleException;
import com.midea.cmms.rest.proxy.server.mvc.method.HandleMethod;
import com.midea.cmms.rest.proxy.server.mvc.method.HttpParameterInfo;
import org.springframework.core.Ordered;
import org.springframework.validation.DataBinder;

/**
 * 从REST风格的URL中获取参数
 * Created by liyilin on 2017/7/7.
 */
public class PathVariableMethodArgumentResolver extends AbstractMethodArgumentResolver {
    @Override
    public boolean support(WebRequest request, HandleMethod method, HttpParameterInfo param) {
        return param.isPathVariable();
    }

    @Override
    public Object resolve(WebRequest request, HandleMethod method, HttpParameterInfo param) throws Exception {
        // 从REST风格的URL中获取字符串参数
        String pathVariable = request.getPathVariable(param.getParamName());
        DataBinder dataBinder = getDataBinder();

        if (pathVariable != null) {
            // 转化参数类型
            return dataBinder.convertIfNecessary(pathVariable, param.getType());
        } else {
            throw new MethodArgumentHandleException("不能从URL中提取参数[" + param.getParamName() + "]");
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE-2;
    }
}
