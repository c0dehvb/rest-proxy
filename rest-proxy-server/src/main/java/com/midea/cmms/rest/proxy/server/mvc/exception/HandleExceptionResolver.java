package com.midea.cmms.rest.proxy.server.mvc.exception;

import com.midea.cmms.rest.proxy.server.WebRequest;
import com.midea.cmms.rest.proxy.server.WebResponse;
import org.springframework.core.Ordered;

/**
 * Created by liyilin on 2017/7/11.
 */
public interface HandleExceptionResolver extends Ordered {
    boolean support(WebRequest webRequest, Throwable throwable);
    void handle(WebRequest webRequest, WebResponse webResponse, Throwable throwable);
}
