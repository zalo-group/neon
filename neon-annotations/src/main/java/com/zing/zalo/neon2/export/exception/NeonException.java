package com.zing.zalo.neon2.export.exception;

/**
 * Created by Tien Loc Bui on 11/09/2019.
 */
public class NeonException extends Exception {
    public NeonException() {
    }

    public NeonException(String s) {
        super(s);
    }

    public NeonException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public NeonException(Throwable throwable) {
        super(throwable);
    }
}
