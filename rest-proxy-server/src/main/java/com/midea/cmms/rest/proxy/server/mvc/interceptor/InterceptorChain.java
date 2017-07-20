package com.midea.cmms.rest.proxy.server.mvc.interceptor;

import com.midea.cmms.rest.proxy.server.WebRequest;
import com.midea.cmms.rest.proxy.server.WebResponse;
import com.midea.cmms.rest.proxy.server.mvc.method.HandleMethod;

/**
 * Created by liyilin on 2017/7/19.
 */
public interface InterceptorChain {
    Object doChain(WebRequest request, WebResponse response, HandleMethod handler) throws Exception;
}
