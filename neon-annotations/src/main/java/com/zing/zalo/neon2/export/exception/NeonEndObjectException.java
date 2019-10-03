package com.zing.zalo.neon2.export.exception;

/**
 * Created by Tien Loc Bui on 20/09/2019.
 */
public class NeonEndObjectException extends Exception {
    public NeonEndObjectException() {
    }

    public NeonEndObjectException(String s) {
        super(s);
    }

    public NeonEndObjectException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public NeonEndObjectException(Throwable throwable) {
        super(throwable);
    }
}
