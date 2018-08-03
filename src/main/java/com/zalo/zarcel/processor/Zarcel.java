package com.zalo.zarcel.processor;

import sun.util.resources.cldr.mk.TimeZoneNames_mk;

import javax.swing.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.Objects;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Zarcel {
    int version() default 0;

    boolean serializedParent() default true;

    @Target(ElementType.FIELD)
    public @interface Property {
        int sinceVersion() default 0;

        int arraySize() default 0;
    }

    @Target(ElementType.FIELD)
    public @interface Abstract {
        Class[] childClass() default {};
        int[] type() default {};
    }
}
