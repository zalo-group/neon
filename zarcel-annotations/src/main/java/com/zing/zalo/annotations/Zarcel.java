package com.zing.zalo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Zarcel {
    int version() default 0;

    boolean inheritanceSupported() default true;

    @Target(ElementType.FIELD)
    @interface Property {
        int sinceVersion() default 0;

        int arraySize() default 0;
    }

    @Target(ElementType.FIELD)
    @interface Custom {
        Class adapter();
    }
}
