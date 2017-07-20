package com.midea.cmms.rest.proxy.server.exception;

/**
 * Created by liyilin on 2017/7/4.
 */
public class MethodArgumentHandleException extends Exception {
    public MethodArgumentHandleException() {
    }

    public MethodArgumentHandleException(String message) {
        super(message);
    }

    public MethodArgumentHandleException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodArgumentHandleException(Throwable cause) {
        super(cause);
    }

    public MethodArgumentHandleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
