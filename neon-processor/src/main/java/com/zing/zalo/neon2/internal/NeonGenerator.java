package com.zing.zalo.neon2.internal;

import com.zing.zalo.neon2.internal.descriptor.ClassDescriptor;
import com.zing.zalo.neon2.internal.descriptor.ClassDescriptorImpl;
import com.zing.zalo.neon2.internal.javapoet.cls.NeonClass;
import com.zing.zalo.neon2.internal.javapoet.cls.factory.NeonClassFactory;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by Tien Loc Bui on 30/08/2019.
 */
public class NeonGenerator {
    public static void processClass(Filer filer, Messager messager, Elements elementUtils, TypeElement typeElement) {
        // Create Class Descriptor
        ClassDescriptor classDescriptor = ClassDescriptorImpl.parse(messager, elementUtils, typeElement);
        if (classDescriptor == null)
            return;
        NeonClass neonClass = NeonClassFactory.createClass(messager, elementUtils, classDescriptor);
        if (neonClass == null)
            return;
        neonClass.writeToFile(filer);
    }

    public static void generateWrapper(Filer filer, Messager messager, Elements elementUtils, TypeElement typeElement) {
        // Do something
    }
}
