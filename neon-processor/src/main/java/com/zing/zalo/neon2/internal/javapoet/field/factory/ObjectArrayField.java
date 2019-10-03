package com.zing.zalo.neon2.internal.javapoet.field.factory;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.zing.zalo.neon2.internal.descriptor.ClassDescriptor;
import com.zing.zalo.neon2.internal.descriptor.ClassType;
import com.zing.zalo.neon2.internal.descriptor.FieldDescriptor;
import com.zing.zalo.neon2.internal.javapoet.field.NeonField;

/**
 * Created by Tien Loc Bui on 23/09/2019.
 */
class ObjectArrayField extends BaseField {
    private static final String ITERATOR_VAR = "$i$";
    private static final String ARRAY_LENGTH_VAR = "$len$";

    private ClassDescriptor fieldTypeDescriptor;

    ObjectArrayField(FieldDescriptor descriptor) {
        super(descriptor);
        fieldTypeDescriptor = getDescriptor().getNonPrimitiveDescriptor();
    }

    @Override
    protected void onSerializeCode(MethodSpec.Builder writer, String serializeOutputVariable, String objectVariable, ClassDescriptor classDescriptor) {
        if (fieldTypeDescriptor == null)
            return;
        writer.beginControlFlow("if ($L.$L == null)", objectVariable, getFieldName())
                .addStatement("$L.writeNull()", serializeOutputVariable)
                .nextControlFlow("else")
                .addStatement("int $L = $L.$L.length",
                        ARRAY_LENGTH_VAR,
                        objectVariable,
                        getFieldName())
                .addStatement("$L.writeInt($L)",
                        serializeOutputVariable,
                        ARRAY_LENGTH_VAR)
                .beginControlFlow("for (int $L = 0; $L < $L; $L++)",
                        ITERATOR_VAR,
                        ITERATOR_VAR,
                        ARRAY_LENGTH_VAR,
                        ITERATOR_VAR)
                .beginControlFlow("if ($L.$L[$L] != null)",
                        objectVariable,
                        getFieldName(),
                        ITERATOR_VAR)
                .addStatement("$L.serialize($L, $L.$L[$L])",
                        fieldTypeDescriptor.getGeneratedNeonClassName(),
                        serializeOutputVariable,
                        objectVariable,
                        getFieldName(),
                        ITERATOR_VAR)
                .nextControlFlow("else")
                .addStatement("$L.writeNull()", serializeOutputVariable)
                .endControlFlow()
                .endControlFlow()
                .endControlFlow();
    }

    @Override
    protected void onDeserializeCode(MethodSpec.Builder writer, String serializeInputVariable, String objectVariable, ClassDescriptor classDescriptor) {
        if (fieldTypeDescriptor == null)
            return;
        writer.beginControlFlow("if (!$L.nextIsNull())", serializeInputVariable)
                .addStatement("int $L = $L.readInt()", ARRAY_LENGTH_VAR, serializeInputVariable)
                .addStatement("$L.$L = new $T[$L]",
                        objectVariable,
                        getFieldName(),
                        TypeName.get(fieldTypeDescriptor.typeMirror()),
                        ARRAY_LENGTH_VAR)
                .beginControlFlow("for (int $L = 0; $L < $L; $L++)",
                        ITERATOR_VAR,
                        ITERATOR_VAR,
                        ARRAY_LENGTH_VAR,
                        ITERATOR_VAR)
                .beginControlFlow("if (!$L.nextIsNull())", serializeInputVariable)
                .addStatement("$L.$L[$L] = $L.deserialize($L)",
                        objectVariable,
                        getFieldName(),
                        ITERATOR_VAR,
                        fieldTypeDescriptor.getGeneratedNeonClassName(),
                        serializeInputVariable)
                .nextControlFlow("else")
                .addStatement("$L.$L[$L] = null",
                        objectVariable,
                        getFieldName(),
                        ITERATOR_VAR)
                .endControlFlow()
                .endControlFlow()
                .nextControlFlow("else")
                .addStatement("$L.$L = null", objectVariable, getFieldName())
                .endControlFlow();
    }

    private String getFieldName() {
        return getDescriptor().getFieldName();
    }
}
