package com.zing.zalo.neon2.internal.descriptor;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

/**
 * Created by Tien Loc Bui on 12/09/2019.
 */
public class FieldDescriptorImpl extends FieldDescriptor {
    private Messager messager;
    private Elements elementUtils;
    private Element mElement;
    private FieldType mType;

    FieldDescriptorImpl(Messager messager, Elements elementUtils, Element element) {
        mElement = element;
        this.messager = messager;
        this.elementUtils = elementUtils;
        mType = FieldType.parse(element);
    }

    @Override
    public FieldType type() {
        return mType;
    }

    @Override
    public String getFieldName() {
        return mElement.getSimpleName().toString();
    }

    @Override
    public ClassDescriptor getNonPrimitiveDescriptor() {
        TypeElement objectElement;
        TypeMirror typeFromElement = mElement.asType();
        switch (type()) {
            case OBJECT_ARRAY:
                if (!(typeFromElement instanceof ArrayType))
                    return null;
                typeFromElement = ((ArrayType) typeFromElement).getComponentType();
                // Object will handle it after remove array
            case OBJECT:
                if (!(typeFromElement instanceof DeclaredType))
                    return null;
                objectElement = (TypeElement) ((DeclaredType) typeFromElement).asElement();
                return ClassDescriptor.parse(messager, elementUtils, objectElement);

            default:
                return null;
        }
    }
}
