package com.zing.zalo.exception;

public class ZarcelException extends Exception {
    public ZarcelException(String message) {
        super(message);
    }

    public ZarcelException(String message, Throwable e) {
        super(message, e);
    }
}
