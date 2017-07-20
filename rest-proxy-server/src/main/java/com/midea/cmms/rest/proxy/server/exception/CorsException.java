package com.midea.cmms.rest.proxy.server.exception;

/**
 * Created by liyilin on 2017/7/19.
 */
public class CorsException extends Exception {
    public CorsException() {
    }

    public CorsException(String message) {
        super(message);
    }

    public CorsException(String message, Throwable cause) {
        super(message, cause);
    }

    public CorsException(Throwable cause) {
        super(cause);
    }

    public CorsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
