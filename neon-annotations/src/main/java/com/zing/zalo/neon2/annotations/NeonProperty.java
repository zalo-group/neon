package com.zing.zalo.neon2.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) public @interface NeonProperty {
    int sinceVersion() default 0;

    Class adapter();
}
