package com.zing.zalo.neon2.internal.descriptor;

import java.lang.annotation.Annotation;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by Tien Loc Bui on 18/09/2019.
 */
public interface AnnotationDescriptor {
    /**
     * Get simple class name.
     *
     * @return the simple class name
     */
    String getSimpleName();

    /**
     * Get full class name.
     *
     * @return the full class name
     */
    String getFullName();

    /**
     * Check equals
     *
     * @param annotation another annotation class
     * @return true if equals
     */
    boolean equals(Class<? extends Annotation> annotation);
}
