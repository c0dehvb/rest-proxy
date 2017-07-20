package com.midea.cmms.rest.proxy.server.mvc.exception;

import com.midea.cmms.rest.proxy.server.WebRequest;
import com.midea.cmms.rest.proxy.server.WebResponse;

import java.util.List;

/**
 * Created by liyilin on 2017/7/11.
 */
public class HandleExceptionResolverManager {
    private List<HandleExceptionResolver> handleExceptionResolvers;

    public void setHandleExceptionResolvers(List<HandleExceptionResolver> handleExceptionResolvers) {
        this.handleExceptionResolvers = handleExceptionResolvers;
    }

    public void handle(WebRequest webRequest, WebResponse webResponse, Throwable throwable) {
        for (HandleExceptionResolver resolver : handleExceptionResolvers) {
            if (resolver.support(webRequest, throwable)) {
                resolver.handle(webRequest, webResponse, throwable);
            }
        }
    }
}
