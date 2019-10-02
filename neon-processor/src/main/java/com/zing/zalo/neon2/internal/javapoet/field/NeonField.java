package com.zing.zalo.neon2.internal.javapoet.field;

import static com.squareup.javapoet.MethodSpec.Builder;

import com.zing.zalo.neon2.internal.descriptor.ClassDescriptor;
import com.zing.zalo.neon2.internal.descriptor.FieldDescriptor;

/**
 * Created by Tien Loc Bui on 30/08/2019.
 */
public abstract class NeonField {

    private FieldDescriptor mDescriptor;

    protected NeonField(FieldDescriptor descriptor) {
        this.mDescriptor = descriptor;
    }

    /**
     * Gets descriptor.
     *
     * @return the field descriptor
     */
    public FieldDescriptor getDescriptor() {
        return mDescriptor;
    }

    /**
     * Prepare valid data and use writer to add serialize field statement.
     *
     * @param writer                  {@link Builder} add code statement to specific method.
     * @param classDescriptor         the class descriptor that can get some class information.
     * @param serializeOutputVariable variable name of
     *                                {@link com.zing.zalo.neon2.export.SerializedOutput} defined in current method.
     * @param objectVariable          variable name of current object of {@link ClassDescriptor}
     */
    public void writeSerializeCode(Builder writer, String serializeOutputVariable, String objectVariable, ClassDescriptor classDescriptor) {
        if (!isValid(true))
            return;
        onSerializeCode(writer, serializeOutputVariable, objectVariable, classDescriptor);
    }

    /**
     * Prepare valid data and use writer to add deserialize field statement.
     *
     * @param writer                 {@link Builder} add code statement to specific method.
     * @param classDescriptor        the class descriptor that can get some class information.
     * @param serializeInputVariable variable name of
     *                               {@link com.zing.zalo.neon2.export.SerializedInput} defined in current method.
     * @param objectVariable         variable name of current object of {@link ClassDescriptor}
     */
    public void writeDeserializeCode(Builder writer, String serializeInputVariable, String objectVariable, ClassDescriptor classDescriptor) {
        if (!isValid(false))
            return;
        onDeserializeCode(writer, serializeInputVariable, objectVariable, classDescriptor);
    }

    /**
     * Return valid state of field. Field will be read only if state is valid.
     *
     * @param printWarning This flag will print something that cause state become to be invalid.
     * @return state of field
     */
    protected boolean isValid(boolean printWarning) {
        //TODO: Add some logic
        return true;
    }

    /**
     * Each field must generate code statement that can be used to serialize that field. This method
     * only call if all condition is valid.
     *
     * @param writer                  {@link Builder} add code statement to specific method.
     * @param classDescriptor         the class descriptor that can get some class information.
     * @param serializeOutputVariable variable name of
     *                                {@link com.zing.zalo.neon2.export.SerializedOutput} defined in current method.
     * @param objectVariable          variable name of current object of {@link ClassDescriptor}
     */
    protected abstract void onSerializeCode(Builder writer, String serializeOutputVariable, String objectVariable, ClassDescriptor classDescriptor);

    /**
     * Each field must generate code statement that can be used to deserialize that field. This method
     * only call if all condition is valid.
     *
     * @param writer                 {@link Builder} add code statement to specific method.
     * @param classDescriptor        the class descriptor that can get some class information.
     * @param serializeInputVariable variable name of
     *                               {@link com.zing.zalo.neon2.export.SerializedInput} defined in current method.
     * @param objectVariable         variable name of current object of {@link ClassDescriptor}
     */
    protected abstract void onDeserializeCode(Builder writer, String serializeInputVariable, String objectVariable, ClassDescriptor classDescriptor);
}
