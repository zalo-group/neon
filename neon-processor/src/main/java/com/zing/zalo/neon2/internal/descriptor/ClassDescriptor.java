package com.zing.zalo.neon2.internal.descriptor;

import com.zing.zalo.neon2.internal.javapoet.field.NeonField;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

/**
 * This class used to show basic information about class that annotated by Neon
 * <p>
 * Created by Tien Loc Bui on 11/09/2019.
 */
public interface ClassDescriptor {

    /**
     * Get type of class.
     *
     * @return {@link ClassType}
     */
    ClassType type();

    /**
     * Get {@link TypeMirror} represent of {@link ClassDescriptor}
     *
     * @return the type mirror
     */
    TypeMirror typeMirror();

    /**
     * Get all fields in this class.
     *
     * @return List of {@link NeonField}
     */
    List<FieldDescriptor> getFields();

    /**
     * Get all fields in this class.
     *
     * @return List of {@link NeonField}
     */
    List<AnnotationDescriptor> getAnnotations();

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
     * Get class version
     *
     * @return version
     */
    int getVersion();

    /**
     * Get package.
     *
     * @return the package
     */
    String getPackage();


    /**
     * Check if this class annotated by specific Annotate Object.
     *
     * @param annotation the annotation
     * @return true if class annotated by annotation param.
     */
    boolean containsAnnotation(Class<? extends Annotation> annotation);

    /**
     * Generate Name for NeonClass depends to config
     *
     * @return the generated neon class name
     */
    String getGeneratedNeonClassName();
}