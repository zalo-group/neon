package com.zalo.zarcel;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.zing.zalo.data.serialization.DebugBuilder;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

import javax.annotation.Nonnull;
import javax.annotation.processing.Filer;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import static javax.lang.model.element.Modifier.STATIC;

class ZarcelGenerator {

    private static final String ZARCEL_SUFFIX = "__Zarcel";
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
                .addParameter(ClassName.get(SerializedOutput.class), "writer")
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

            int i = 0;
            for (int propertyVersion = 0; propertyVersion <= data.version(); propertyVersion++) {
                builder.addCode("//========================== Version $L ==========================//\n", propertyVersion);
                while (i < data.properties().size()) {
                    ZarcelProperty property = data.properties().get(i);
                    if (property.version() > propertyVersion) {
                        break;
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
                    i++;
                }
                builder.addCode("//===============================================================//\n");
            }
        }

        for (Map.Entry<String, String> exception : data.serializeException()) {
            builder.addException(ClassName.get(exception.getKey(), exception.getValue()));
        }
        return builder.build();
    }

    private MethodSpec generateDeserializeMethod(@Nonnull ZarcelClass data) {

        MethodSpec.Builder builder = MethodSpec.methodBuilder("createFromSerialized")
                .addModifiers(STATIC)
                .addParameter(ClassName.get(argPackage, argClass), argClassName)
                .addParameter(ClassName.get(SerializedInput.class), "reader")
                .addParameter(ClassName.get(DebugBuilder.class), "builder")
                .returns(void.class);

        builder.addStatement("int version");
        builder.beginControlFlow("if (builder != null)");
        generateDeserializeWithFlag(builder, data, true);
        builder.nextControlFlow("else");
        generateDeserializeWithFlag(builder, data, false);
        builder.endControlFlow();

        if (data.migrateClass() != null) {
            builder.addStatement("new $T().migrate($L,version,$L)", data.migrateClass(), argClassName, data.version());
        }
        for (Map.Entry<String, String> exception : data.deserializeException()) {
            builder.addException(ClassName.get(exception.getKey(), exception.getValue()));
        }

        return builder.build();
    }

    private void generateDeserializeWithFlag(MethodSpec.Builder builder, @Nonnull ZarcelClass data, boolean debug) {
        // Check version
        if (debug) {
            builder.addStatement("builder.beginObject(\"$L\")", data.name());
        }
        builder.addStatement("version = reader.readInt32()");
        if (debug) {
            builder.addStatement("builder.addType(\"$L\",$L)", "version", "String.valueOf(version)");
        }
        builder.beginControlFlow("if (version>$L)", data.version())
                .addStatement("throw new IllegalArgumentException(\"$L is outdated. Update $L to deserialize newest binary data.\")", data.name(), data.name())
                .endControlFlow();
        builder.beginControlFlow("if (version<$L)", data.compatibleSince())
                .addStatement("throw new IllegalArgumentException(\"Binary data of $L is outdated. You must re-serialize latest data.\")", data.name())
                .endControlFlow();
        // Check base
        if (data.inheritanceSupported()) {
            if (debug) {
                builder.addStatement("builder.addObject(\"parentSerialize\")");
            }
            builder.addStatement("$T.createFromSerialized($L,reader,builder)",
                    ClassName.get(data.parentClass().getKey(), data.parentClass().getValue() + ZARCEL_SUFFIX),
                    argClassName
            );
        }

        int i = 0;
        for (int propertyVersion = 0; propertyVersion <= data.version(); propertyVersion++) {
            // Check version
            builder.beginControlFlow("if (version>=$L)", propertyVersion);
            // iterator on this
            while (i < data.properties().size()) {
                ZarcelProperty property = data.properties().get(i);
                if (property.version() > propertyVersion) {
                    break;
                }
                switch (property.type()) {
                    case PRIMITIVE:
                        String primitiveType = property.dataType().getValue();
                        DeserializableHelper.readPrimitive(builder, primitiveType, argClassName, property.propertyName(), debug);
                        break;
                    case OBJECT:
                        DeserializableHelper.readObject(builder, property, argClassName, property.propertyName(), property.objectNullable(), debug);
                        break;
                    case PRIMITIVE_ARRAY:
                        DeserializableHelper.readPrimitiveArray(builder, property, argClassName, debug);
                        break;
                    case OBJECT_ARRAY:
                        DeserializableHelper.readObjectArray(builder, property, argClassName, debug);
                        break;
                    case CUSTOM_ADAPTER:
                        DeserializableHelper.readCustomAdapter(builder, property, argClassName, debug);
                        break;
                }
                i++;
            }
            // End check version
            builder.endControlFlow();
        }
        if (debug) {
            builder.addStatement("builder.endObject()");
        }
    }
}


