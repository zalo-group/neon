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
public abstract class ClassDescriptor {

    /**
     * Get type of class.
     *
     * @return {@link ClassType}
     */
    public abstract ClassType type();

    /**
     * Get {@link TypeMirror} represent of {@link ClassDescriptor}
     *
     * @return the type mirror
     */
    public abstract TypeMirror typeMirror();

    /**
     * Get all fields in this class.
     *
     * @return List of {@link NeonField}
     */
    public abstract List<FieldDescriptor> getFields();

    /**
     * Get all fields in this class.
     *
     * @return List of {@link NeonField}
     */
    public abstract List<AnnotationDescriptor> getAnnotations();

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
     * Get class version
     *
     * @return version
     */
    public abstract int getVersion();

    /**
     * Get package.
     *
     * @return the package
     */
    public abstract String getPackage();


    /**
     * Check if this class annotated by specific Annotate Object.
     *
     * @param annotation the annotation
     * @return true if class annotated by annotation param.
     */
    public abstract boolean containsAnnotation(Class<? extends Annotation> annotation);

    /**
     * Generate Name for NeonClass depends to config
     *
     * @return the generated neon class name
     */
    public abstract String getGeneratedNeonClassName();

    /**
     * Parse {@link ClassDescriptor} from element received by annotations.
     *
     * @param messager     the {@link Messager} to print error, warning, etc ...
     * @param elementUtils Can get in {@link AbstractProcessor}
     * @param element      the element need to be converted to {@link ClassDescriptor}
     * @return {@link ClassDescriptor}
     */
    public static ClassDescriptor parse(Messager messager, Elements elementUtils, TypeElement element) {
        if (element == null || element.getKind() != ElementKind.CLASS)
            return null;
        return new ClassDescriptorImpl(messager, elementUtils, element);
    }
}