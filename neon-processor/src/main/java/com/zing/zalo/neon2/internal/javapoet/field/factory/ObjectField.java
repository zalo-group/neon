package com.zing.zalo.neon2.internal.javapoet.field.factory;

import com.squareup.javapoet.MethodSpec;
import com.zing.zalo.neon2.internal.descriptor.ClassDescriptor;
import com.zing.zalo.neon2.internal.descriptor.FieldDescriptor;
import com.zing.zalo.neon2.internal.javapoet.field.NeonField;

/**
 * Created by Tien Loc Bui on 23/09/2019.
 */
class ObjectField extends BaseField {
    private ClassDescriptor fieldTypeDescriptor;

    ObjectField(FieldDescriptor descriptor) {
        super(descriptor);
        fieldTypeDescriptor = getDescriptor().getNonPrimitiveDescriptor();
    }

    @Override
    protected void onSerializeCode(MethodSpec.Builder writer, String serializeOutputVariable, String objectVariable, ClassDescriptor classDescriptor) {
        if (fieldTypeDescriptor == null)
            return;
        writer.beginControlFlow("if ($L.$L == null)", objectVariable, getDescriptor().getFieldName())
                .addStatement("$L.writeNull()", serializeOutputVariable)
                .nextControlFlow("else")
                .addStatement("$L.serialize($L, $L.$L)",
                        fieldTypeDescriptor.getGeneratedNeonClassName(),
                        serializeOutputVariable,
                        objectVariable,
                        getDescriptor().getFieldName())
                .endControlFlow();
    }

    @Override
    protected void onDeserializeCode(MethodSpec.Builder writer, String serializeInputVariable, String objectVariable, ClassDescriptor classDescriptor) {
        if (fieldTypeDescriptor == null)
            return;
        // Read Object
        writer.beginControlFlow("if (!$L.nextIsNull())", serializeInputVariable)
                .addStatement("$L.$L = $L.deserialize($L)",
                        objectVariable,
                        getDescriptor().getFieldName(),
                        fieldTypeDescriptor.getGeneratedNeonClassName(),
                        serializeInputVariable)
                .nextControlFlow("else")
                .addStatement("$L.$L = null", objectVariable, getDescriptor().getFieldName())
                .endControlFlow();
    }
}
