package com.zing.zalo.neon2.internal.javapoet.field.factory;

import com.squareup.javapoet.MethodSpec;
import com.zing.zalo.neon2.internal.descriptor.ClassDescriptor;
import com.zing.zalo.neon2.internal.descriptor.FieldDescriptor;
import com.zing.zalo.neon2.internal.javapoet.field.NeonField;

/**
 * Created by Tien Loc Bui on 11/09/2019.
 */
class BooleanArrayField extends BaseField {

    BooleanArrayField(FieldDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    protected void onSerializeCode(MethodSpec.Builder writer, String serializeOutputVariable, String objectVariable, ClassDescriptor classDescriptor) {
        writer.addStatement("$L.writeBooleanArray($L.$L)",
                serializeOutputVariable,
                objectVariable,
                getDescriptor().getFieldName());
    }

    @Override
    protected void onDeserializeCode(MethodSpec.Builder writer, String serializeInputVariable, String objectVariable, ClassDescriptor classDescriptor) {
        writer.addStatement("$L.$L = $L.readBooleanArray()",
                objectVariable,
                getDescriptor().getFieldName(),
                serializeInputVariable);
    }
}