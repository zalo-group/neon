package com.zing.zalo.neon2.internal.descriptor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

import static javax.lang.model.element.ElementKind.FIELD;

/**
 * Created by Tien Loc Bui on 12/09/2019.
 */
public class FieldDescriptorImpl implements FieldDescriptor {
    private Messager messager;
    private Elements elementUtils;
    private Element mElement;
    private FieldType mType;

    private FieldDescriptorImpl(Messager messager, Elements elementUtils, Element element) {
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
                return ClassDescriptorImpl.get(messager, elementUtils, objectElement);

            default:
                return null;
        }
    }

    /**
     * Parse {@link FieldDescriptor} from element received by annotations.
     *
     * @param messager     the {@link Messager} to print error, warning, etc ...
     * @param elementUtils Can get in {@link AbstractProcessor}
     * @param element      the element need to be converted to {@link FieldDescriptor}
     * @return {@link FieldDescriptor}
     */
    public static FieldDescriptor get(Messager messager, Elements elementUtils, Element element) {
        if (element == null || element.getKind() != FIELD)
            return null;
        return new FieldDescriptorImpl(messager, elementUtils, element);
    }
}
