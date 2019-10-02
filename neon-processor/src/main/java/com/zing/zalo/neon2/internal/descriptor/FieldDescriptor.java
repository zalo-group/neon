package com.zing.zalo.neon2.internal.descriptor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;

import static javax.lang.model.element.ElementKind.FIELD;

/**
 * This class used to show basic information about field in fields of class annotated by Neon
 * <p>
 * Created by Tien Loc Bui on 12/09/2019.
 */
public abstract class FieldDescriptor {

    /**
     * Get type of field.
     *
     * @return {@link FieldType}
     */
    public abstract FieldType type();

    /**
     * Gets name of field.
     *
     * @return the field name
     */
    public abstract String getFieldName();

    /**
     * Return ClassDescriptor if type of field is not primitive
     * Return null if type is primitive
     *
     * @return {@link ClassDescriptor}
     */
    public abstract ClassDescriptor getNonPrimitiveDescriptor();

    /**
     * Parse {@link FieldDescriptor} from element received by annotations.
     *
     * @param messager     the {@link Messager} to print error, warning, etc ...
     * @param elementUtils Can get in {@link AbstractProcessor}
     * @param element      the element need to be converted to {@link FieldDescriptor}
     * @return {@link FieldDescriptor}
     */
    public static FieldDescriptor parse(Messager messager, Elements elementUtils, Element element) {
        if (element == null || element.getKind() != FIELD)
            return null;
        return new FieldDescriptorImpl(messager, elementUtils, element);
    }
}
