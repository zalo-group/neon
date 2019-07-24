package com.zing.zalo.neon2.annoations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface NeonObject {
    int version() default 0;

    int compatibleSince() default 0;

    boolean inheritanceSupported() default true;
}
