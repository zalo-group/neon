package com.zalo.zarcel;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.processing.Filer;

import static javax.lang.model.element.Modifier.STATIC;

class ZarcelGenerator {

    private static final String ZARCEL_SUFFIX = "$Zarcel";

    private static String argClass, argPackage, argClassName;

    private static void init(@Nonnull ZarcelClass data) {
        argClass = data.name().trim();
        argClassName = argClass.toLowerCase();
        if (argClass.equals(argClassName)) {
            argClassName = "_" + argClassName;
        }
        argPackage = data.thisPackage().trim();
        Collections.sort(data.properties(), (o1, o2) -> o1.version() - o2.version());
    }

    static void generateFile(@Nonnull ZarcelClass data, Filer filer) throws IOException {
        if (data.properties().size() == 0) {
            throw new InvalidParameterException(MessageFormat.format("Missing properties in class {0}!", data.name()));
        }
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

    private static MethodSpec generateSerializeMethod(@Nonnull ZarcelClass data) {
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
                        builder.beginControlFlow("if ($L.$L != null && $L.$L.length > 0)", argClassName,
                                property.propertyName(), argClassName, property.propertyName())
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
                case CONDITIONAL_OBJECT:
                    if (property.conditionalProperties() != null && property.conditionalProperties().size() > 0) {
                        ZarcelArrayList<Map.Entry<Map.Entry<String, String>, Integer>> arrayList = property.conditionalProperties();
                        builder.beginControlFlow("if ($L.$L != null)", argClassName, property.propertyName())
                                .addStatement("writer.writeBool(true)")
                                .beginControlFlow("if ($L.$L instanceof $T)", argClassName, property.propertyName(),
                                        ClassName.get(arrayList.get(0).getKey().getKey(), arrayList.get(0).getKey().getValue()))
                                .addStatement("writer.writeInt32($L.$L.mType)", argClassName, property.propertyName())
                                .addStatement("(($L) $L.$L).serialize(writer)",
                                        arrayList.get(0).getKey().getValue(),
                                        argClassName, property.propertyName());
                        if (arrayList.size() == 1) {
                            builder.endControlFlow();
                        } else {
                            // Next
                            for (int i = 1; i < arrayList.size(); i++) {
                                builder.nextControlFlow("else if ($L.$L instanceof $T)", argClassName,
                                        property.propertyName(),
                                        ClassName.get(arrayList.get(i).getKey().getKey(), arrayList.get(i).getKey().getValue()))
                                        .addStatement("writer.writeInt32($L.$L.mType)", argClassName, property.propertyName())
                                        .addStatement("(($L) $L.$L).serialize(writer)", arrayList.get(i).getKey().getValue(),
                                                argClassName, property.propertyName());
                            }
                            // End
                            builder.endControlFlow(); // End if
                        }
                        builder.nextControlFlow("else")
                                .addStatement("writer.writeBool(false)")
                                .endControlFlow();
                    }
                    break;
                case CONDITIONAL_OBJECT_ARRAY:
                    if (property.conditionalProperties() != null && property.conditionalProperties().size() > 0) {
                        ZarcelArrayList<Map.Entry<Map.Entry<String, String>, Integer>> arrayList = property.conditionalProperties();
                        builder.beginControlFlow("if ($L.$L != null)", argClassName, property.propertyName())
                                .addStatement("writer.writeBool(true)")
                                .addStatement("int size$L = $L.$L.length", property.propertyName(), argClassName, property.propertyName())
                                .addStatement("writer.writeInt32(size$L)", property.propertyName())
                                .beginControlFlow("for (int i = 0; i < size$L; i++)", property.propertyName())
                                // Begin if
                                .beginControlFlow("if ($L.$L[i] instanceof $T)", argClassName, property.propertyName(),
                                        ClassName.get(arrayList.get(0).getKey().getKey(), arrayList.get(0).getKey().getValue()))
                                .addStatement("writer.writeInt32($L.$L[i].mType)", argClassName, property.propertyName())
                                .addStatement("(($L) $L.$L[i]).serialize(writer)",
                                        arrayList.get(0).getKey().getValue(),
                                        argClassName, property.propertyName());
                        if (arrayList.size() == 1) {
                            builder.endControlFlow().endControlFlow();
                        } else {
                            // Next
                            for (int i = 1; i < arrayList.size(); i++) {
                                builder.nextControlFlow("else if ($L.$L[i] instanceof $T)", argClassName,
                                        property.propertyName(),
                                        ClassName.get(arrayList.get(i).getKey().getKey(), arrayList.get(i).getKey().getValue()))
                                        .addStatement("writer.writeInt32($L.$L[i].mType)", argClassName, property.propertyName())
                                        .addStatement("(($L) $L.$L[i]).serialize(writer)", arrayList.get(i).getKey().getValue(),
                                                argClassName, property.propertyName());
                            }
                            // End
                            builder.endControlFlow() // End if
                                    .endControlFlow(); // End for
                        }
                        builder.nextControlFlow("else")
                                .addStatement("writer.writeBool(false)")
                                .endControlFlow();
                    }
                    break;
            }
        }
        builder.addCode("//===============================================================//\n");
        return builder.build();
    }

    private static MethodSpec generateDeserializeMethod(@Nonnull ZarcelClass data) {

        MethodSpec.Builder builder = MethodSpec.methodBuilder("createFromSerialized")
                .addModifiers(STATIC)
                .addParameter(ClassName.get(argPackage, argClass), argClassName)
                .addParameter(ClassName.get("com.zing.zalo.data.serialization", "SerializedInput"), "reader")
                .returns(void.class);

        // Check version
        builder.addStatement("int version = reader.readInt32()");
        builder.beginControlFlow("if (version>$L)", data.version())
                .addStatement("return")
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
        while (propertyVersion <= data.version()) {
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
                            builder.addStatement("int size$L = reader.readInt32()", property.propertyName())
                                    .addStatement("$L.$L = new $T[size$L]", argClassName, property.propertyName(),
                                            ClassName.get(property.dataType().getKey(), property.dataType().getValue()),
                                            property.propertyName())
                                    .beginControlFlow("for (int i = 0; i < size$L; i++)", property.propertyName())
                                    .addStatement("$L.$L[i] = $L.CREATOR.createFromSerialized(reader)",
                                            argClassName, property.propertyName(), property.dataType().getValue())
                                    .endControlFlow();
                        } else {
                            builder.beginControlFlow("if (reader.readBool())")
                                    .addStatement("int size$L = reader.readInt32()", property.propertyName())
                                    .addStatement("$L.$L = new $T[size$L]", argClassName, property.propertyName(),
                                            ClassName.get(property.dataType().getKey(), property.dataType().getValue()),
                                            property.propertyName())
                                    .beginControlFlow("for (int i = 0; i < size$L; i++)", property.propertyName())
                                    .addStatement("$L.$L[i] = $L.CREATOR.createFromSerialized(reader)",
                                            argClassName, property.propertyName(), property.dataType().getValue())
                                    .endControlFlow()
                                    .endControlFlow();
                        }
                        break;
                    case CONDITIONAL_OBJECT:
                        builder.beginControlFlow("if (reader.readBool())")
                                .addStatement("int type$L = reader.readInt32()", property.propertyName())
                                // FIRST
                                .beginControlFlow("if (type$L == $L)", property.propertyName(), property.conditionalProperties().get(0).getValue())
                                .addStatement("$L.$L = $L.CREATOR.createFromSerialized(reader)", argClassName,
                                        property.propertyName(), property.conditionalProperties().get(0).getKey().getValue());
                        if (data.properties().size() > 1) {
                            // NEXT
                            for (int iterator = 1; iterator < property.conditionalProperties().size(); iterator++) {
                                builder.nextControlFlow("else if(type$L == $L)", property.propertyName(),
                                        property.conditionalProperties().get(iterator).getValue())
                                        .addStatement("$L.$L = $L.CREATOR.createFromSerialized(reader)", argClassName,
                                                property.propertyName(), property.conditionalProperties().get(iterator).getKey().getValue());
                            }
                        }
                        //------------------------//
                        builder.endControlFlow()
                                .endControlFlow();
                        break;
                    case CONDITIONAL_OBJECT_ARRAY:
                        builder.beginControlFlow("if (reader.readBool())")
                                .addStatement("int size$L = reader.readInt32()", property.propertyName())
                                .addStatement("$L.$L = new $T[size$L]", argClassName, property.propertyName(),
                                        ClassName.get(property.dataType().getKey(), property.dataType().getValue()),
                                        property.propertyName())
                                .beginControlFlow("for (int i = 0; i < size$L; i++)", property.propertyName())
                                .addStatement("int type$L = reader.readInt32()", property.propertyName())

                                // FIRST
                                .beginControlFlow("if (type$L == $L)", property.propertyName(), property.conditionalProperties().get(0).getValue())
                                .addStatement("$L.$L[i] = $L.CREATOR.createFromSerialized(reader)", argClassName,
                                        property.propertyName(), property.conditionalProperties().get(0).getKey().getValue());
                        if (data.properties().size() > 1) {
                            // NEXT
                            for (int iterator = 1; iterator < property.conditionalProperties().size(); iterator++) {
                                builder.nextControlFlow("else if(type$L == $L)", property.propertyName(),
                                        property.conditionalProperties().get(iterator).getValue())
                                        .addStatement("$L.$L[i] = $L.CREATOR.createFromSerialized(reader)", argClassName,
                                                property.propertyName(), property.conditionalProperties().get(iterator).getKey().getValue());
                            }
                        }
                        //------------------------//
                        builder.endControlFlow()
                                .endControlFlow()
                                .endControlFlow();
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
        return builder.build();
    }

    private static void writePrimitive(MethodSpec.Builder builder, String argClassName, String propertyName, String propertyDataType) {
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
            case "String":
                builder.addStatement("writer.writeString($L.$L)", argClassName, propertyName);
                break;
        }
    }

    private static void readPrimitive(MethodSpec.Builder builder, String argClassName, String propertyName, String propertyDataType) {
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
            case "String":
                builder.addStatement("$L.$L = reader.readString()", argClassName, propertyName);
                break;
        }
    }
}


