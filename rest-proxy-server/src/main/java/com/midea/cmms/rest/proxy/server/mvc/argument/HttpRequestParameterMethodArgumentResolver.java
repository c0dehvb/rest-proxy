package com.midea.cmms.rest.proxy.server.mvc.argument;

import com.midea.cmms.rest.proxy.server.WebRequest;
import com.midea.cmms.rest.proxy.server.exception.MethodArgumentHandleException;
import com.midea.cmms.rest.proxy.server.mvc.method.HandleMethod;
import com.midea.cmms.rest.proxy.server.mvc.method.HttpParameterInfo;
import org.apache.log4j.Logger;
import org.springframework.validation.DataBinder;

/**
 * 从HttpRequest的Parameter中获取参数
 * Created by liyilin on 2017/7/7.
 */
public class HttpRequestParameterMethodArgumentResolver extends AbstractMethodArgumentResolver {
    private static Logger LOG = Logger.getLogger(HttpRequestParameterMethodArgumentResolver.class);

    @Override
    public boolean support(WebRequest request, HandleMethod method, HttpParameterInfo param) {
        return !param.isPathVariable() && !param.isRequestBody();
    }

    @Override
    public Object resolve(WebRequest request, HandleMethod method, HttpParameterInfo param) throws Exception {
        String paramName = param.getParamName();
        Class<?> paramType = param.getType();
        String requestParam = request.getParameter(paramName);
        DataBinder dataBinder = getDataBinder(param.getDateFormat());

        // 可能需要取默认值
        String value = requestParam != null ? requestParam : param.getDefaultValue();

        if (value != null) {
            return dataBinder.convertIfNecessary(value, paramType);
        }
        // 必传参数检测
        else if (param.isRequired()) {
            throw new MethodArgumentHandleException("参数[" + paramName + "]不能为空");
        }
        // 无法获取参数则直接返回null
        return null;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
