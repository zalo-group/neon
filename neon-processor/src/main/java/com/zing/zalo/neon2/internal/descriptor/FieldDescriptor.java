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
public interface FieldDescriptor {

    /**
     * Get type of field.
     *
     * @return {@link FieldType}
     */
    FieldType type();

    /**
     * Gets name of field.
     *
     * @return the field name
     */
    String getFieldName();

    /**
     * Return ClassDescriptor if type of field is not primitive
     * Return null if type is primitive
     *
     * @return {@link ClassDescriptor}
     */
    ClassDescriptor getNonPrimitiveDescriptor();
}
