package com.midea.cmms.rest.proxy.server.exception;

/**
 * Created by liyilin on 2017/7/4.
 */
public class RequestPathConflictException extends RuntimeException {
    public RequestPathConflictException(String urlPattern) {
        super("请求路径路径冲突：" + urlPattern);
    }
}
