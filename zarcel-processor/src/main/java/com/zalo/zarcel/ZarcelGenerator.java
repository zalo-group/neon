package com.zalo.zarcel;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.zing.zalo.data.serialization.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.processing.Filer;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import static javax.lang.model.element.Modifier.STATIC;

class ZarcelGenerator {

    private static final String ZARCEL_SUFFIX = "__Zarcel";
    private static final String SERIALIZATION_PACKAGE = "com.zing.zalo.data.serialization";

    private String argClass, argPackage, argClassName;
    private boolean hasProperty = true;

    private void init(@Nonnull ZarcelClass data) {
        argClass = data.name().trim();
        argClassName = argClass.toLowerCase();
        if (argClassName.lastIndexOf(".") != -1)
            argClassName = argClassName.substring(argClassName.lastIndexOf(".") + 1);
        if (argClass.equals(argClassName)) {
            argClassName = "_" + argClassName;
        }

        if (data.properties().size() == 0) {
            hasProperty = false;
        }

        argPackage = data.thisPackage().trim();
        if (hasProperty)
            Collections.sort(data.properties(), new Comparator<ZarcelProperty>() {
                @Override
                public int compare(ZarcelProperty o1, ZarcelProperty o2) {
                    return o1.version() - o2.version();
                }
            });
    }

    public void generateFile(@Nonnull ZarcelClass data, Filer filer) throws IOException {
        init(data);
        MethodSpec serialize = generateSerializeMethod(data);
        MethodSpec deserialize = generateDeserializeMethod(data);
        int lastIndex = argClass.lastIndexOf(".") + 1;
        String className;
        if (lastIndex > 0)
            className = argClass.substring(lastIndex);
        else
            className = argClass;
        TypeSpec.Builder zarcelClassBuilder = TypeSpec.classBuilder(className + ZARCEL_SUFFIX)
                .addMethod(serialize)
                .addMethod(deserialize);
        TypeSpec zarcelClass = zarcelClassBuilder.build();
        JavaFile javaFile = JavaFile.builder(data.thisPackage().trim(), zarcelClass).indent("    ")
                .build();

        javaFile.writeTo(filer);
    }

    private MethodSpec generateSerializeMethod(@Nonnull ZarcelClass data) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("serialize")
                .addModifiers(STATIC)
                .addParameter(ClassName.get(argPackage, argClass), argClassName)
                .addParameter(ClassName.get(SERIALIZATION_PACKAGE, "SerializedOutput"), "writer")
                .returns(void.class);

        builder.addCode("//------------ $L version------------//\n", data.name());
        builder.addStatement("writer.writeInt32($L)", data.version());
        if (data.inheritanceSupported() && data.parentClass() != null) {
            builder.addStatement("$T.serialize($L,writer)",
                    ClassName.get(data.parentClass().getKey(), data.parentClass().getValue() + ZARCEL_SUFFIX),
                    argClassName
            );
        }

        if (hasProperty) {
            if (data.properties().get(data.properties().size() - 1).version() > data.version()) {
                throw new IllegalArgumentException("Zarcel.Property has version larger than class version " + data.name());
            }
        }

        if (hasProperty) {
            int propertyVersion = data.properties().get(0).version();
            builder.addCode("//========================== Version $L ==========================//\n", propertyVersion);
            for (ZarcelProperty property : data.properties()) {
                if (property.version() > propertyVersion) {
                    builder.addCode("//===============================================================//\n");
                    builder.addCode("//========================== Version $L ==========================//\n", property.version());
                    propertyVersion = property.version();
                }
                builder.addCode("//------------ $L ------------//\n", property.propertyName());
                switch (property.type()) {
                    case OBJECT:
                        SerializableHelper.writeObject(builder, argClassName, property.propertyName(), property.objectNullable());
                        break;
                    case PRIMITIVE:
                        String primitiveType = property.dataType().getValue();
                        SerializableHelper.writePrimitive(builder, primitiveType, argClassName, property.propertyName());
                        break;
                    case OBJECT_ARRAY:
                        SerializableHelper.writeObjectArray(builder, property, argClassName);
                        break;
                    case PRIMITIVE_ARRAY:
                        SerializableHelper.writePrimitiveArray(builder, property, argClassName);
                        break;
                    case CUSTOM_ADAPTER:
                        SerializableHelper.writeCustomAdapter(builder, property, argClassName);
                        break;
                }
            }
        }
        builder.addCode("//===============================================================//\n");

        for (Map.Entry<String, String> exception : data.serializeException()) {
            builder.addException(ClassName.get(exception.getKey(), exception.getValue()));
        }
        return builder.build();
    }

    private MethodSpec generateDeserializeMethod(@Nonnull ZarcelClass data) {

        MethodSpec.Builder builder = MethodSpec.methodBuilder("createFromSerialized")
                .addModifiers(STATIC)
                .addParameter(ClassName.get(argPackage, argClass), argClassName)
                .addParameter(ClassName.get(SERIALIZATION_PACKAGE, "SerializedInput"), "reader")
                .returns(void.class);

        // Check version
        builder.addStatement("int version = reader.readInt32()");
        builder.beginControlFlow("if (version>$L)", data.version())
                .addStatement("throw new IllegalArgumentException(\"$L is outdated. Update $L to deserialize newest binary data.\")", data.name(), data.name())
                .endControlFlow();
        builder.beginControlFlow("if (version<$L)", data.compatibleSince())
                .addStatement("throw new IllegalArgumentException(\"Binary data of $L is outdated. You must re-serialize latest data.\")", data.name())
                .endControlFlow();
        // Check base
        if (data.inheritanceSupported()) {
            builder.addStatement("$T.createFromSerialized($L,reader)",
                    ClassName.get(data.parentClass().getKey(), data.parentClass().getValue() + ZARCEL_SUFFIX),
                    argClassName
            );
        }

        int propertyVersion = 0, i = 0;
        builder.beginControlFlow("if (version>=$L)", propertyVersion);
        while (hasProperty && propertyVersion <= data.version()) {
            ZarcelProperty property;
            if (i < data.properties().size()) {
                property = data.properties().get(i);
                if (property.version() > propertyVersion) {
                    propertyVersion++;
                    if (property.version() > propertyVersion) continue;
                    builder.nextControlFlow("if (version>=$L)", propertyVersion);
                }
                switch (property.type()) {
                    case PRIMITIVE:
                        String primitiveType = property.dataType().getValue();
                        DeserializableHelper.readPrimitive(builder, primitiveType, argClassName, property.propertyName());
                        break;
                    case OBJECT:
                        DeserializableHelper.readObject(builder, property, argClassName, property.propertyName(), property.objectNullable());
                        break;
                    case PRIMITIVE_ARRAY:
                        DeserializableHelper.readPrimitiveArray(builder, property, argClassName);
                        break;
                    case OBJECT_ARRAY:
                        DeserializableHelper.readObjectArray(builder, property, argClassName);
                        break;
                    case CUSTOM_ADAPTER:
                        DeserializableHelper.readCustomAdapter(builder, property, argClassName);
                        break;
                }
                i++;
            } else {
                propertyVersion++;
                if (propertyVersion <= data.version()) {
                    builder.nextControlFlow("if (version>=$L)", propertyVersion);
                }
            }
        }
        builder.endControlFlow();
        if (data.migrateClass() != null) {
            builder.addStatement("new $T().migrate($L,version,$L)", data.migrateClass(), argClassName, data.version());
        }
        for (Map.Entry<String, String> exception : data.deserializeException()) {
            builder.addException(ClassName.get(exception.getKey(), exception.getValue()));
        }

        return builder.build();
    }
}


