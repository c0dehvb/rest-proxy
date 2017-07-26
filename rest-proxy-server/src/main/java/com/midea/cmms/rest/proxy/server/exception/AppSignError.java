package com.midea.cmms.rest.proxy.server.exception;

/**
 * App签名认证错误
 */
public class AppSignError extends Error {
    public AppSignError() {
    }

    public AppSignError(String s) {
        super(s);
    }

    public AppSignError(String message, Throwable cause) {
        super(message, cause);
    }

    public AppSignError(Throwable cause) {
        super(cause);
    }
}
