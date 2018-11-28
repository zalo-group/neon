package com.zalo.zing.log;

import com.zing.zalo.zarcel.helper.DebugBuilder;

public class Logger implements DebugBuilder.Logger {
    private static Logger sInstance;

    private Logger() {
    }

    public static Logger getInstance() {
        if (sInstance != null)
            return sInstance;
        sInstance = new Logger();
        return sInstance;
    }

    @Override
    public void log(DebugBuilder builder) {
        System.out.println(builder.getLog());
    }
}
