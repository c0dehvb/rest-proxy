package com.midea.cmms.rest.proxy.server.exception;

/**
 * Created by liyilin on 2017/7/7.
 */
public class ViewResolveException extends Exception {
    public ViewResolveException() {
    }

    public ViewResolveException(String message) {
        super(message);
    }

    public ViewResolveException(String message, Throwable cause) {
        super(message, cause);
    }

    public ViewResolveException(Throwable cause) {
        super(cause);
    }

    public ViewResolveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
