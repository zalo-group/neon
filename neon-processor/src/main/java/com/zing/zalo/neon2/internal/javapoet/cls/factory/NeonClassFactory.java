package com.zing.zalo.neon2.internal.javapoet.cls.factory;

import com.zing.zalo.neon2.annotations.NeonObject;
import com.zing.zalo.neon2.internal.descriptor.ClassDescriptor;
import com.zing.zalo.neon2.internal.descriptor.ClassDescriptorImpl;
import com.zing.zalo.neon2.internal.javapoet.cls.NeonClass;

import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by Tien Loc Bui on 30/08/2019.
 */
public final class NeonClassFactory {
    public static NeonClass createClass(Messager messager, Elements elementUtils, ClassDescriptor descriptor) {
        // NeonClass will be created only if the class is annotated by NeonObject
        if (!descriptor.containsAnnotation(NeonObject.class))
            return null;
        switch (descriptor.type()) {
            case CLASS:
                return new DefaultNeonClass(messager, elementUtils, descriptor);
            case INTERFACE:
            case ENUM:
            case NONE:
            default:
                return null;
        }
    }
}
