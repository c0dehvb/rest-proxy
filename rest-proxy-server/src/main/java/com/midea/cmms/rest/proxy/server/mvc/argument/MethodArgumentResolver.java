package com.midea.cmms.rest.proxy.server.mvc.argument;

import com.midea.cmms.rest.proxy.server.WebRequest;
import com.midea.cmms.rest.proxy.server.mvc.method.HandleMethod;
import com.midea.cmms.rest.proxy.server.mvc.method.HttpParameterInfo;
import org.springframework.core.Ordered;

/**
 * 方法参数处理类
 *
 * <p>用于将HTTP请求参数转换为Java方法可以接受类型的参数值</p>
 *
 * <p>
 *     应用程序中可以包含多个MethodParameterResolver，它们可能都支持对某个参数的处理。
 *     这时，可以利用Order接口确认它们的优先级，优先级高的才能对本次参数进行处理。
 * </p>
 *
 * Created by liyilin on 2017/7/1.
 */
public interface MethodArgumentResolver extends Ordered {
    /**
     *
     * @param param
     * @return
     */
    boolean support(WebRequest request, HandleMethod method, HttpParameterInfo param);

    /**
     *
     * @param request
     * @param param
     * @return
     * @throws Exception
     */
    Object resolve(WebRequest request, HandleMethod method, HttpParameterInfo param) throws Exception;
}
