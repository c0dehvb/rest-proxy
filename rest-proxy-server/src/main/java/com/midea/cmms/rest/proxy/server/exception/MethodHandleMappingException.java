package com.midea.cmms.rest.proxy.server.exception;

/**
 * Created by liyilin on 2017/7/4.
 */
public class MethodHandleMappingException extends Exception {
    public MethodHandleMappingException(String message) {
        super(message);
    }

    public MethodHandleMappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodHandleMappingException(Throwable cause) {
        super(cause);
    }

    public MethodHandleMappingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MethodHandleMappingException() {
    }
}
