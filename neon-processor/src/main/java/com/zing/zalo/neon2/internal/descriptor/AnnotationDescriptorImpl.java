package com.zing.zalo.neon2.internal.descriptor;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by Tien Loc Bui on 25/09/2019.
 */
public class AnnotationDescriptorImpl extends AnnotationDescriptor {
    private Messager messager;
    private Elements elementUtils;
    private TypeElement mDeclaredElement;
    private AnnotationMirror mElement;

    AnnotationDescriptorImpl(Messager messager, Elements elementUtils, AnnotationMirror annotationMirror) {
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
}
