package com.zing.zalo.neon2.internal.descriptor;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by Tien Loc Bui on 25/09/2019.
 */
public class AnnotationDescriptorImpl implements AnnotationDescriptor {
    private Messager messager;
    private Elements elementUtils;
    private TypeElement mDeclaredElement;
    private AnnotationMirror mElement;

    private AnnotationDescriptorImpl(Messager messager, Elements elementUtils, AnnotationMirror annotationMirror) {
        this.messager = messager;
        this.elementUtils = elementUtils;
        this.mElement = annotationMirror;
        if (mElement.getAnnotationType().asElement() instanceof TypeElement)
            mDeclaredElement = (TypeElement) mElement.getAnnotationType().asElement();
    }

    @Override
    public boolean equals(Class<? extends Annotation> annotation) {
        return getFullName().equals(annotation.getCanonicalName());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof AnnotationDescriptor) {
            return getFullName().equals(((AnnotationDescriptor) o).getFullName());
        }
        return super.equals(o);
    }

    @Override
    public String getSimpleName() {
        return mDeclaredElement.getSimpleName().toString();
    }

    @Override
    public String getFullName() {
        return mDeclaredElement.getQualifiedName().toString();
    }

    /**
     * Parse {@link AnnotationDescriptor} from element type
     *
     * @param messager         the {@link Messager} to print error, warning, etc ...
     * @param elementUtils     Can get in {@link AbstractProcessor}
     * @param annotationMirror the element need to be converted to {@link AnnotationDescriptor}
     * @return {@link AnnotationDescriptor} or null if type is not annotation.
     */
    public static AnnotationDescriptor get(Messager messager, Elements elementUtils, AnnotationMirror annotationMirror) {
        if (annotationMirror == null || annotationMirror.getAnnotationType().asElement().getKind() != ElementKind.ANNOTATION_TYPE)
            return null;
        if (!(annotationMirror.getAnnotationType().asElement() instanceof TypeElement))
            return null;
        return new AnnotationDescriptorImpl(messager, elementUtils, annotationMirror);
    }
}
