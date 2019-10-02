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
public abstract class AnnotationDescriptor {
    /**
     * Get simple class name.
     *
     * @return the simple class name
     */
    public abstract String getSimpleName();

    /**
     * Get full class name.
     *
     * @return the full class name
     */
    public abstract String getFullName();

    /**
     * Check equals
     *
     * @param annotation another annotation class
     * @return true if equals
     */
    public abstract boolean equals(Class<? extends Annotation> annotation);

    /**
     * Parse {@link AnnotationDescriptor} from element type
     *
     * @param messager         the {@link Messager} to print error, warning, etc ...
     * @param elementUtils     Can get in {@link AbstractProcessor}
     * @param annotationMirror the element need to be converted to {@link AnnotationDescriptor}
     * @return {@link AnnotationDescriptor} or null if type is not annotation.
     */
    public static AnnotationDescriptor parse(Messager messager, Elements elementUtils, AnnotationMirror annotationMirror) {
        if (annotationMirror == null || annotationMirror.getAnnotationType().asElement().getKind() != ElementKind.ANNOTATION_TYPE)
            return null;
        if (!(annotationMirror.getAnnotationType().asElement() instanceof TypeElement))
            return null;
        return new AnnotationDescriptorImpl(messager, elementUtils, annotationMirror);
    }
}
