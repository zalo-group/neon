package com.zing.zalo.neon2.internal.descriptor;

import com.zing.zalo.neon2.annotations.NeonVersion;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

import static com.zing.zalo.neon2.internal.NeonConfig.NEON_CLASS_POSTFIX;
import static com.zing.zalo.neon2.internal.NeonConfig.NEON_CLASS_PREFIX;


/**
 * Created by Tien Loc Bui on 12/09/2019.
 */
public class ClassDescriptorImpl implements ClassDescriptor {
    private Messager messager;
    private Elements elementUtils;
    private TypeElement mElement;
    private ClassType mType;
    private List<FieldDescriptor> mFields = null;
    private List<AnnotationDescriptor> mAnnotations = null;

    private ClassDescriptorImpl(Messager messager, Elements elementUtils, TypeElement element) {
        mElement = element;
        this.messager = messager;
        this.elementUtils = elementUtils;
        mType = ClassType.parse(element);
    }

    @Override
    public ClassType type() {
        return mType;
    }

    @Override
    public TypeMirror typeMirror() {
        return mElement.asType();
    }

    @Override
    public String getSimpleName() {
        return mElement.getSimpleName().toString();
    }

    @Override
    public String getFullName() {
        return mElement.getQualifiedName().toString();
    }

    @Override
    public String getPackage() {
        return elementUtils.getPackageOf(mElement).getQualifiedName().toString();
    }

    @Override
    public int getVersion() {
        return mElement.getAnnotation(NeonVersion.class) == null ? 0 :
                mElement.getAnnotation(NeonVersion.class).value();
    }

    @Override
    public boolean containsAnnotation(Class<? extends Annotation> annotation) {
        List<AnnotationDescriptor> annotationDescriptors = getAnnotations();
        for (AnnotationDescriptor annotationDescriptor : annotationDescriptors) {
            if (annotationDescriptor.equals(annotation))
                return true;
        }
        return false;
    }

    @Override
    public List<FieldDescriptor> getFields() {
        if (mFields != null || type() == ClassType.NONE) {
            mFields = mFields != null ? mFields : new ArrayList<FieldDescriptor>();
            return mFields;
        }

        List<? extends Element> elements = mElement.getEnclosedElements();
        mFields = new ArrayList<>();
        for (Element element : elements) {
            FieldDescriptor field = FieldDescriptorImpl.get(messager, elementUtils, element);
            if (field != null && field.type() != FieldType.NONE)
                mFields.add(field);
        }
        return mFields;
    }

    @Override
    public List<AnnotationDescriptor> getAnnotations() {
        if (mAnnotations != null || type() == ClassType.NONE) {
            mAnnotations = mAnnotations != null ? mAnnotations : new ArrayList<AnnotationDescriptor>();
            return mAnnotations;
        }

        mAnnotations = new ArrayList<>();
        List<? extends AnnotationMirror> annotationMirrors = mElement.getAnnotationMirrors();
        for (AnnotationMirror annotation : annotationMirrors) {
            AnnotationDescriptor aDescriptor = AnnotationDescriptorImpl.get(messager, elementUtils, annotation);
            if (aDescriptor != null)
                mAnnotations.add(aDescriptor);
        }
        return mAnnotations;
    }

    @Override
    public String getGeneratedNeonClassName() {
        return NEON_CLASS_PREFIX + getSimpleName() + NEON_CLASS_POSTFIX;
    }

    /**
     * Parse {@link ClassDescriptor} from element received by annotations.
     *
     * @param messager     the {@link Messager} to print error, warning, etc ...
     * @param elementUtils Can get in {@link AbstractProcessor}
     * @param element      the element need to be converted to {@link ClassDescriptor}
     * @return {@link ClassDescriptor}
     */
    public static ClassDescriptor get(Messager messager, Elements elementUtils, TypeElement element) {
        if (element == null || element.getKind() != ElementKind.CLASS)
            return null;
        return new ClassDescriptorImpl(messager, elementUtils, element);
    }
}
