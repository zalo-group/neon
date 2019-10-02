package com.zing.zalo.neon2.internal.javapoet.cls.factory;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.zing.zalo.neon2.export.SerializedInput;
import com.zing.zalo.neon2.export.SerializedOutput;
import com.zing.zalo.neon2.export.exception.NeonEndObjectException;
import com.zing.zalo.neon2.export.exception.NeonException;
import com.zing.zalo.neon2.internal.NeonConfig;
import com.zing.zalo.neon2.internal.descriptor.ClassDescriptor;
import com.zing.zalo.neon2.internal.descriptor.FieldDescriptor;
import com.zing.zalo.neon2.internal.javapoet.cls.NeonClass;
import com.zing.zalo.neon2.internal.javapoet.field.NeonField;
import com.zing.zalo.neon2.internal.javapoet.field.factory.NeonFieldFactory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;

/**
 * Created by Tien Loc Bui on 11/09/2019.
 */
@SuppressWarnings("FieldCanBeLocal")
class DefaultNeonClass implements NeonClass {
    private ClassDescriptor mDescriptor;
    private Messager messager;
    private Elements elementUtils;
    private List<NeonField> mFields;

    // ------ Java Poet Field ----------- //

    private MethodSpec mSerializeMethod = null;
    private MethodSpec mDeserializeMethod = null;
    private TypeSpec mClass = null;

    // ------ Constants ---------------- //
    private static String SERIALIZE_METHOD_NAME = "serialize";
    private static String DESERIALIZE_METHOD_NAME = "deserialize";
    private static String SERIALIZED_OUTPUT_VARIABLE = "output$$";
    private static String SERIALIZED_INPUT_VARIABLE = "input$$";
    private static String OBJECT_VARIABLE = "obj$$";
    private static String VERSION_VARIABLE = "ver$$";

    DefaultNeonClass(Messager messager, Elements elementUtils, ClassDescriptor classDescriptor) {
        this.messager = messager;
        this.elementUtils = elementUtils;
        mDescriptor = classDescriptor;
    }

    @Override
    public ClassDescriptor getDescriptor() {
        return mDescriptor;
    }

    @Override
    public List<NeonField> getFields() {
        if (mFields != null)
            return mFields;
        mFields = new LinkedList<>();
        List<FieldDescriptor> fieldDescriptors = mDescriptor.getFields();
        for (FieldDescriptor fieldDescriptor : fieldDescriptors) {
            NeonField field = NeonFieldFactory.createField(fieldDescriptor);
            if (field != null)
                mFields.add(field);
        }
        return mFields;
    }

    @Override
    public void writeToFile(Filer filer) {
        // Write serialize
        writeSerialize();
        // Write deserialize
        writeDeserialize();
        // Write class
        writeClass();
        // Write to file
        try {
            JavaFile.builder(getPackage(), mClass)
                    .indent(NeonConfig.INDENT_SPACE)
                    .build()
                    .writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeSerialize() {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(SERIALIZE_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(SerializedOutput.class, SERIALIZED_OUTPUT_VARIABLE, Modifier.FINAL)
                .addParameter(TypeName.get(mDescriptor.typeMirror()), OBJECT_VARIABLE, Modifier.FINAL)
                .addException(NeonException.class)
                .returns(void.class);
        // ----- Begin Object with version ----- //
        methodBuilder.addStatement("$L.writeObjectStart($L)", SERIALIZED_OUTPUT_VARIABLE, mDescriptor.getVersion());
        // Serialize field
        List<NeonField> fields = getFields();
        for (NeonField field : fields) {
            field.writeSerializeCode(methodBuilder, SERIALIZED_OUTPUT_VARIABLE, OBJECT_VARIABLE, mDescriptor);
        }
        // ----- End Object ----- //
        methodBuilder.addStatement("$L.writeObjectEnd()", SERIALIZED_OUTPUT_VARIABLE);
        // ----- Build Method ----- //
        mSerializeMethod = methodBuilder.build();
    }

    private void writeDeserialize() {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(DESERIALIZE_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(SerializedInput.class, SERIALIZED_INPUT_VARIABLE, Modifier.FINAL)
                .addException(NeonException.class)
                .returns(TypeName.get(mDescriptor.typeMirror()));
        // ----- Create object ------------------------ //
        methodBuilder.addStatement("$T $L = new $T()",
                TypeName.get(mDescriptor.typeMirror()),
                OBJECT_VARIABLE,
                TypeName.get(mDescriptor.typeMirror()));
        // ----- Read Object version with version ----- //
//        methodBuilder.addStatement("$L.writeObjectStart($L)", SERIALIZED_OUTPUT_VARIABLE, mDescriptor.getVersion());
        methodBuilder.beginControlFlow("try");
        // Read version
        methodBuilder.addStatement("int $L = $L.readObjectStart($L)",
                VERSION_VARIABLE,
                SERIALIZED_INPUT_VARIABLE,
                mDescriptor.getVersion());
        // Serialize field
        List<NeonField> fields = getFields();
        for (NeonField field : fields) {
            field.writeDeserializeCode(methodBuilder, SERIALIZED_INPUT_VARIABLE, OBJECT_VARIABLE, mDescriptor);
        }
        // ----- End Object ----- //
        methodBuilder.nextControlFlow("catch ($T ignored)", NeonEndObjectException.class)
                .nextControlFlow("finally")
                .addStatement("$L.readObjectEnd()", SERIALIZED_INPUT_VARIABLE)
                .endControlFlow();
        // Return object
        methodBuilder.addStatement("return $L", OBJECT_VARIABLE);
        // ----- Build Method ----- //
        mDeserializeMethod = methodBuilder.build();
    }

    private void writeClass() {
        mClass = TypeSpec.classBuilder(mDescriptor.getGeneratedNeonClassName())
                .addModifiers(Modifier.PUBLIC)
                .addMethod(mSerializeMethod)
                .addMethod(mDeserializeMethod)
                .build();
    }

    // Private control

    private String getPackage() {
        return getDescriptor().getPackage();
    }
}
