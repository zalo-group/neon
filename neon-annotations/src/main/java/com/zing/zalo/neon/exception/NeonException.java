package com.zing.zalo.neon.exception;

public class NeonException extends Exception {
    public NeonException(String message) {
        super(message);
    }

    public NeonException(String message, Throwable e) {
        super(message, e);
    }
}
