package com.zing.zalo.neon2.internal.javapoet.field.factory;

import com.squareup.javapoet.MethodSpec;
import com.zing.zalo.neon2.internal.descriptor.ClassDescriptor;
import com.zing.zalo.neon2.internal.descriptor.FieldDescriptor;
import com.zing.zalo.neon2.internal.javapoet.field.NeonField;

import javax.annotation.processing.Messager;

/**
 * Created by Tien Loc Bui on 03/10/2019.
 */
public abstract class BaseField implements NeonField {
    private FieldDescriptor mDescriptor;
    private boolean flagPrintWarningCheck = true;

    protected BaseField(FieldDescriptor descriptor) {
        this.mDescriptor = descriptor;
    }

    @Override
    public FieldDescriptor getDescriptor() {
        return mDescriptor;
    }

    @Override
    public void writeSerializeCode(Messager messager, MethodSpec.Builder writer, String serializeOutputVariable, String objectVariable, ClassDescriptor classDescriptor) {
        if (isValid(messager, flagPrintWarningCheck)) {
            flagPrintWarningCheck = false;
            onSerializeCode(writer, serializeOutputVariable, objectVariable, classDescriptor);
        }
    }

    @Override
    public void writeDeserializeCode(Messager messager, MethodSpec.Builder writer, String serializeInputVariable, String objectVariable, ClassDescriptor classDescriptor) {
        if (isValid(messager, flagPrintWarningCheck)) {
            flagPrintWarningCheck = false;
            onDeserializeCode(writer, serializeInputVariable, objectVariable, classDescriptor);
        }
    }

    /**
     * Return valid state of field. Field will be read only if state is valid.
     *
     * @param messager     use to print warning or error.
     * @param printWarning This flag will print something that cause state become to be invalid.
     * @return state of field
     */
    protected boolean isValid(Messager messager, boolean printWarning) {
        return true;
    }

    /**
     * Each field must generate code statement that can be used to serialize that field. This method
     * only call if all condition is valid.
     *
     * @param writer                  {@link MethodSpec.Builder} add code statement to specific method.
     * @param classDescriptor         the class descriptor that can get some class information.
     * @param serializeOutputVariable variable name of
     *                                {@link com.zing.zalo.neon2.export.SerializedOutput} defined in current method.
     * @param objectVariable          variable name of current object of {@link ClassDescriptor}
     */
    protected abstract void onSerializeCode(MethodSpec.Builder writer, String serializeOutputVariable, String objectVariable, ClassDescriptor classDescriptor);

    /**
     * Each field must generate code statement that can be used to deserialize that field. This method
     * only call if all condition is valid.
     *
     * @param writer                 {@link MethodSpec.Builder} add code statement to specific method.
     * @param classDescriptor        the class descriptor that can get some class information.
     * @param serializeInputVariable variable name of
     *                               {@link com.zing.zalo.neon2.export.SerializedInput} defined in current method.
     * @param objectVariable         variable name of current object of {@link ClassDescriptor}
     */
    protected abstract void onDeserializeCode(MethodSpec.Builder writer, String serializeInputVariable, String objectVariable, ClassDescriptor classDescriptor);
}
