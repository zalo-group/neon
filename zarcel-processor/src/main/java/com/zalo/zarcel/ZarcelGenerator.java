package com.zalo.zarcel;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.Nonnull;
import javax.annotation.processing.Filer;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static javax.lang.model.element.Modifier.STATIC;

class ZarcelGenerator {

    private static final String ZARCEL_SUFFIX = "$Zarcel";

    private String argClass, argPackage, argClassName;
    private boolean hasProperty = true;

    private void init(@Nonnull ZarcelClass data) {
        argClass = data.name().trim();
        argClassName = argClass.toLowerCase();
        if (argClass.equals(argClassName)) {
            argClassName = "_" + argClassName;
        }

        if (data.properties().size() == 0) {
            hasProperty = false;
        }

        argPackage = data.thisPackage().trim();
        if (hasProperty)
            Collections.sort(data.properties(), (o1, o2) -> o1.version() - o2.version());
    }

    public void generateFile(@Nonnull ZarcelClass data, Filer filer) throws IOException {
        init(data);
        MethodSpec serialize = generateSerializeMethod(data);
        MethodSpec deserialize = generateDeserializeMethod(data);
        TypeSpec.Builder zarcelClassBuilder = TypeSpec.classBuilder(data.name().trim() + ZARCEL_SUFFIX)
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
                .addParameter(ClassName.get("com.zing.zalo.data.serialization", "SerializedOutput"), "writer")
                .returns(void.class);

        builder.addCode("//------------ $L version------------//\n", data.name());
        builder.addStatement("writer.writeInt32($L)", data.version());
        if (data.serializeParent() && data.parentClass() != null) {
            builder.addStatement("$T.serialize($L,writer)",
                    ClassName.get(data.parentClass().getKey(), data.parentClass().getValue() + ZARCEL_SUFFIX),
                    argClassName
            );
        }

        if (data.properties().get(data.properties().size() - 1).version() > data.version()) {
            throw new IllegalArgumentException("Zarcel.Property has version larger than class version " + data.name());
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
                        if (!property.objectNullable()) {
                            builder.addStatement("$L.$L.serialize(writer)", argClassName, property.propertyName());
                        } else {
                            builder.beginControlFlow("if ($L.$L != null)", argClassName, property.propertyName())
                                    .addStatement("writer.writeBool(true)");
                            builder.addStatement("$L.$L.serialize(writer)", argClassName, property.propertyName());
                            builder.nextControlFlow("else")
                                    .addStatement("writer.writeBool(false)")
                                    .endControlFlow();
                        }
                        break;
                    case PRIMITIVE:
                        writePrimitive(builder, argClassName, property.propertyName(), property.dataType().getValue());
                        break;
                    case OBJECT_ARRAY:
                        if (property.objectNullable()) {
                            builder.beginControlFlow("if ($L.$L != null)", argClassName,
                                    property.propertyName())
                                    .addStatement("writer.writeBool(true)")
                                    .addStatement("writer.writeInt32($L.$L.length)", argClassName, property.propertyName())
                                    .beginControlFlow("for($T i : $L.$L)",
                                            ClassName.get(property.dataType().getKey(), property.dataType().getValue()), argClassName,
                                            property.propertyName())
                                    .addStatement("i.serialize(writer)")
                                    .endControlFlow()
                                    .nextControlFlow("else")
                                    .addStatement("writer.writeBool(false)")
                                    .endControlFlow();
                        } else {
                            builder
                                    .addStatement("writer.writeInt32($L.$L.length)", argClassName, property.propertyName())
                                    .beginControlFlow("for($T i : $L.$L)",
                                            ClassName.get(property.dataType().getKey(), property.dataType().getValue()), argClassName,
                                            property.propertyName())
                                    .addStatement("i.serialize(writer)")
                                    .endControlFlow();
                        }
                        break;
                    case PRIMITIVE_ARRAY:
                        if (property.arraySize() != null && property.arraySize() > 0) {
                            for (int i = 0; i < property.arraySize(); i++) {
                                writePrimitive(builder, argClassName,
                                        property.propertyName() + "[" + i + "]", property.dataType().getValue());
                            }
                        } else {
                            builder.beginControlFlow("if ($L.$L != null)", argClassName, property.propertyName())
                                    .addStatement("writer.writeBool(true)")
                                    .addStatement("writer.writeInt32($L.$L.length)", argClassName, property.propertyName())
                                    .beginControlFlow("for (int i=0; i< $L.$L.length; i++)", argClassName, property.propertyName());
                            writePrimitive(builder, argClassName,
                                    property.propertyName() + "[i]", property.dataType().getValue());
                            builder.endControlFlow()
                                    .nextControlFlow("else")
                                    .addStatement("writer.writeBool(false)")
                                    .endControlFlow();
                        }
                        break;
                    case CUSTOM_ADAPTER:

                        if (!property.objectNullable()) {
                            builder.beginControlFlow("");
                            builder.addStatement("$T tmp$$customAdapter = new $T()",
                                    ClassName.get(property.dataType().getKey(), property.dataType().getValue()),
                                    ClassName.get(property.dataType().getKey(), property.dataType().getValue()));
                            builder.addStatement("tmp$$customAdapter.serialize($L.$L,writer)",
                                    argClassName, property.propertyName());
                            builder.endControlFlow("");
                        } else {
                            builder.beginControlFlow("if ($L.$L != null)", argClassName, property.propertyName())
                                    .addStatement("writer.writeBool(true)");

                            builder.addStatement("$T tmp$$customAdapter = new $T()",
                                    ClassName.get(property.dataType().getKey(), property.dataType().getValue()),
                                    ClassName.get(property.dataType().getKey(), property.dataType().getValue()));
                            builder.addStatement("tmp$$customAdapter.serialize($L.$L,writer)",
                                    argClassName, property.propertyName());

                            builder.nextControlFlow("else")
                                    .addStatement("writer.writeBool(false)")
                                    .endControlFlow();
                        }
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
                .addParameter(ClassName.get("com.zing.zalo.data.serialization", "SerializedInput"), "reader")
                .returns(void.class);

        // Check version
        builder.addStatement("int version = reader.readInt32()");
        builder.beginControlFlow("if (version>$L)", data.version())
                .addStatement("throw new IllegalArgumentException(\"$L is outdated. Update $L to deserialize newest binary data.\")", data.name(), data.name())
                .endControlFlow();
        // Check base
        if (data.serializeParent()) {
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
                    if (propertyVersion > data.version()) continue;
                    builder.nextControlFlow("if (version>=$L)", propertyVersion);
                }
                // Deserialize with property.
                switch (property.type()) {
                    case PRIMITIVE:
                        readPrimitive(builder, argClassName, property.propertyName(), property.dataType().getValue());
                        break;
                    case OBJECT:
                        if (!property.objectNullable()) {
                            builder.addStatement("$L.$L = $T.CREATOR.createFromSerialized(reader)",
                                    argClassName, property.propertyName(),
                                    ClassName.get(property.dataType().getKey(), property.dataType().getValue()));
                        } else {
                            builder.beginControlFlow("if (reader.readBool())")
                                    .addStatement("$L.$L = $T.CREATOR.createFromSerialized(reader)",
                                            argClassName, property.propertyName(),
                                            ClassName.get(property.dataType().getKey(), property.dataType().getValue()))
                                    .endControlFlow();
                        }
                        break;
                    case PRIMITIVE_ARRAY:
                        if (property.arraySize() != null && property.arraySize() > 0) {
                            for (int iterator = 0; iterator < property.arraySize(); iterator++) {
                                readPrimitive(builder, argClassName,
                                        property.propertyName() + "[" + iterator + "]", property.dataType().getValue());
                            }
                        } else {
                            builder.beginControlFlow("if (reader.readBool())")
                                    .addStatement("int sizePrimitive = reader.readInt32()")
                                    .addStatement("$L.$L = new $L[sizePrimitive]", argClassName, property.propertyName(), property.dataType().getValue())
                                    .beginControlFlow("for (int i=0; i< sizePrimitive; i++)");
                            readPrimitive(builder, argClassName, property.propertyName() + "[i]", property.dataType().getValue());
                            builder.endControlFlow()
                                    .endControlFlow();
                        }
                        break;
                    case OBJECT_ARRAY:
                        if (!property.objectNullable()) {
                            builder.addStatement("$L.$L = $T.", property.propertyName())
                                    .addStatement("$L.$L = new $T[size$L]", argClassName, property.propertyName(),
                                            ClassName.get(property.dataType().getKey(), property.dataType().getValue()),
                                            property.propertyName())
                                    .beginControlFlow("for (int i = 0; i < size$L; i++)", property.propertyName())
                                    .addStatement("$L.$L[i] = $T.CREATOR.createFromSerialized(reader)",
                                            argClassName, property.propertyName(),
                                            ClassName.get(property.dataType().getKey(), property.dataType().getValue()))
                                    .endControlFlow();
                        } else {
                            builder.beginControlFlow("if (reader.readBool())")
                                    .addStatement("int size$L = reader.readInt32()", property.propertyName())
                                    .addStatement("$L.$L = new $T[size$L]", argClassName, property.propertyName(),
                                            ClassName.get(property.dataType().getKey(), property.dataType().getValue()),
                                            property.propertyName())
                                    .beginControlFlow("for (int i = 0; i < size$L; i++)", property.propertyName())
                                    .addStatement("$L.$L[i] = $T.CREATOR.createFromSerialized(reader)",
                                            argClassName, property.propertyName(),
                                            ClassName.get(property.dataType().getKey(), property.dataType().getValue()))
                                    .endControlFlow()
                                    .endControlFlow();
                        }
                        break;
                    case CUSTOM_ADAPTER:
                        if (!property.objectNullable()) {
                            builder.beginControlFlow("");
                            builder.addStatement("$T tmp$$customAdapter = new $T()",
                                    ClassName.get(property.dataType().getKey(), property.dataType().getValue()),
                                    ClassName.get(property.dataType().getKey(), property.dataType().getValue()));
                            builder.addStatement("$L.$L = tmp$$customAdapter.createFromSerialized(reader)",
                                    argClassName, property.propertyName());
                            builder.endControlFlow();
                        } else {
                            builder.beginControlFlow("if (reader.readBool())")
                                    .addStatement("$T tmp$$customAdapter = new $T()",
                                            ClassName.get(property.dataType().getKey(), property.dataType().getValue()),
                                            ClassName.get(property.dataType().getKey(), property.dataType().getValue()));
                            builder.addStatement("$L.$L = tmp$$customAdapter.createFromSerialized(reader)",
                                    argClassName, property.propertyName());
                            builder.endControlFlow();
                        }
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

        for (Map.Entry<String, String> exception : data.deserializeException()) {
            builder.addException(ClassName.get(exception.getKey(), exception.getValue()));
        }

        return builder.build();
    }

    private void writePrimitive(MethodSpec.Builder builder, String argClassName, String propertyName, String
            propertyDataType) {
        switch (propertyDataType) {
            case "int":
                builder.addStatement("writer.writeInt32($L.$L)", argClassName, propertyName);
                break;
            case "double":
            case "float":
                builder.addStatement("writer.writeDouble($L.$L)", argClassName, propertyName);
                break;
            case "boolean":
                builder.addStatement("writer.writeBool($L.$L)", argClassName, propertyName);
                break;
            case "long":
                builder.addStatement("writer.writeInt64($L.$L)", argClassName, propertyName);
                break;
            case "String":
                builder.addStatement("writer.writeString($L.$L)", argClassName, propertyName);
                break;
        }
    }

    private void readPrimitive(MethodSpec.Builder builder, String argClassName, String propertyName, String
            propertyDataType) {
        switch (propertyDataType) {
            case "int":
                builder.addStatement("$L.$L = reader.readInt32()", argClassName, propertyName);
                break;
            case "double":
                builder.addStatement("$L.$L = reader.readDouble()", argClassName, propertyName);
                break;
            case "float":
                builder.addStatement("$L.$L = (float)reader.readDouble()", argClassName, propertyName);
                break;
            case "boolean":
                builder.addStatement("$L.$L = reader.readBool()", argClassName, propertyName);
                break;
            case "long":
                builder.addStatement("$L.$L = reader.readInt64()", argClassName, propertyName);
                break;
            case "String":
                builder.addStatement("$L.$L = reader.readString()", argClassName, propertyName);
                break;
        }
    }
}


