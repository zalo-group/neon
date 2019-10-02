package com.zing.zalo.neon2.internal.descriptor;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/**
 * Created by Tien Loc Bui on 11/09/2019.
 */
public enum FieldType {
    NONE, INT, FLOAT, DOUBLE, CHAR, BOOLEAN, BYTE, LONG, SHORT, INT_ARRAY, FLOAT_ARRAY, DOUBLE_ARRAY,
    CHAR_ARRAY, BOOLEAN_ARRAY, BYTE_ARRAY, LONG_ARRAY, SHORT_ARRAY, OBJECT, OBJECT_ARRAY;

    public static FieldType parse(Element element) {
        if (element.getKind() != ElementKind.FIELD)
            return NONE;
        // Parse field
        TypeKind typeKind = element.asType().getKind();
        switch (typeKind) {
            case INT:
                return INT;
            case DOUBLE:
                return DOUBLE;
            case FLOAT:
                return FLOAT;
            case CHAR:
                return CHAR;
            case BYTE:
                return BYTE;
            case LONG:
                return LONG;
            case BOOLEAN:
                return BOOLEAN;
            case SHORT:
                return SHORT;
            case DECLARED:
                return OBJECT;
            case ARRAY:
                return parseArray(element);
            default:
                return NONE;
        }
    }

    private static FieldType parseArray(Element element) {
        if (element == null || element.asType().getKind() != TypeKind.ARRAY)
            return NONE;
        // element is array
        TypeMirror componentType = ((ArrayType) element.asType()).getComponentType();
        TypeKind typeKind = componentType.getKind();
        switch (typeKind) {
            case INT:
                return INT_ARRAY;
            case DOUBLE:
                return DOUBLE_ARRAY;
            case FLOAT:
                return FLOAT_ARRAY;
            case CHAR:
                return CHAR_ARRAY;
            case BYTE:
                return BYTE_ARRAY;
            case LONG:
                return LONG_ARRAY;
            case BOOLEAN:
                return BOOLEAN_ARRAY;
            case SHORT:
                return SHORT_ARRAY;
            case DECLARED:
                return OBJECT_ARRAY;
            default:
                return NONE;
        }
    }
}
