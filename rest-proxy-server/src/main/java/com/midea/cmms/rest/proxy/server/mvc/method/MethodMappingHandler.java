package com.midea.cmms.rest.proxy.server.mvc.method;

import com.midea.cmms.rest.proxy.server.exception.MethodHandleMappingException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by liyilin on 2017/7/3.
 */
public interface MethodMappingHandler {
    HandleMethod handle(HttpServletRequest httpServletRequest) throws MethodHandleMappingException;
}
