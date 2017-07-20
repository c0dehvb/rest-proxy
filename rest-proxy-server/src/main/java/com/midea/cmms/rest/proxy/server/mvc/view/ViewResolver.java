package com.midea.cmms.rest.proxy.server.mvc.view;

import com.midea.cmms.rest.proxy.server.WebRequest;
import com.midea.cmms.rest.proxy.server.WebResponse;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;

/**
 * Created by liyilin on 2017/7/4.
 */
public interface ViewResolver extends Ordered {
    boolean support(WebRequest request, Method method, Object result);
    void render(WebRequest request, WebResponse response, Method method, Object result) throws Exception;
}
