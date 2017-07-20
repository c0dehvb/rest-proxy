package com.midea.cmms.rest.proxy.server.mvc.interceptor;

import com.midea.cmms.rest.proxy.server.WebRequest;
import com.midea.cmms.rest.proxy.server.WebResponse;
import com.midea.cmms.rest.proxy.server.mvc.method.HandleMethod;
import org.springframework.core.Ordered;

import javax.servlet.ServletContext;

/**
 * Created by liyilin on 2017/7/19.
 */
public interface Interceptor extends Ordered {
    void init(ServletContext servletContext) throws Exception;
    Object handle(WebRequest request, WebResponse response, HandleMethod handler, InterceptorChain chain) throws Exception;
    void destroy(ServletContext servletContext) throws Exception;
}
