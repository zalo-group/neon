package com.zing.zalo.neon;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

class SerializableHelper {
    static void writeObject(MethodSpec.Builder builder, String argClassName, String attribute,
            boolean nullableObject) {
        if (!nullableObject) {
            builder.addStatement("$L.$L.serialize(writer)", argClassName, attribute);
        } else {
            builder.beginControlFlow("if ($L.$L != null)", argClassName, attribute)
                    .addStatement("writer.writeBool(true)");
            builder.addStatement("$L.$L.serialize(writer)", argClassName, attribute);
            builder.nextControlFlow("else")
                    .addStatement("writer.writeBool(false)")
                    .endControlFlow();
        }
    }

    static void writeObjectArray(MethodSpec.Builder builder, NeonProperty property,
            String argClassName) {
        beginArray(builder, property, argClassName, property.objectNullable());
        //==========Process with i========
        writeObject(builder, argClassName, property.propertyName() + "[i]", false);
        //================================
        endArray(builder, property.objectNullable());
    }

    static void writePrimitive(MethodSpec.Builder builder, String primitiveType,
            String argClassName, String attribute) {
        switch (primitiveType) {
            case "int":
                builder.addStatement("writer.writeInt32($L.$L)", argClassName, attribute);
                break;
            case "double":
            case "float":
                builder.addStatement("writer.writeDouble($L.$L)", argClassName, attribute);
                break;
            case "boolean":
                builder.addStatement("writer.writeBool($L.$L)", argClassName, attribute);
                break;
            case "long":
                builder.addStatement("writer.writeInt64($L.$L)", argClassName, attribute);
                break;
            case "String":
                builder.addStatement("writer.writeString($L.$L)", argClassName, attribute);
                break;
        }
    }

    static void writePrimitiveArray(MethodSpec.Builder builder, NeonProperty property,
            String argClassName) {
        beginArray(builder, property, argClassName, property.objectNullable());
        //==========Process with i========
        writePrimitive(builder, property.dataType().getValue(), argClassName,
                property.propertyName() + "[i]");
        //================================
        endArray(builder, property.objectNullable());
    }

    static void writeCustomAdapter(MethodSpec.Builder builder, NeonProperty property,
            String argClassName) {
        String propertyName = property.propertyName();
        String adapterPackage = property.dataType().getKey();
        String adapterClassName = property.dataType().getValue();
        ClassName adapterClass = ClassName.get(adapterPackage, adapterClassName);

        if (!property.objectNullable()) {
            builder.beginControlFlow("")
                    .addStatement("$T tmp__customAdapter = new $T()", adapterClass, adapterClass)
                    .addStatement("tmp__customAdapter.serialize($L.$L,writer)", argClassName,
                            propertyName);
            builder.endControlFlow("");
        } else {
            builder.beginControlFlow("if ($L.$L != null)", argClassName, propertyName)
                    .addStatement("writer.writeBool(true)")
                    .addStatement("$T tmp__customAdapter = new $T()", adapterClass, adapterClass)
                    .addStatement("tmp__customAdapter.serialize($L.$L,writer)", argClassName,
                            propertyName)
                    .nextControlFlow("else")
                    .addStatement("writer.writeBool(false)")
                    .endControlFlow();
        }
    }

    private static void beginArray(MethodSpec.Builder builder, NeonProperty property,
            String argClassName, boolean arrayNullable) {
        String propertyName = property.propertyName();
        if (arrayNullable) {
            builder.beginControlFlow("if ($L.$L != null)", argClassName, propertyName)
                    .addStatement("writer.writeBool(true)");
        }
        builder.addStatement("writer.writeInt32($L.$L.length)", argClassName, propertyName)
                .beginControlFlow("for (int i=0; i< $L.$L.length; i++)", argClassName,
                        propertyName);
        // Do something with i
    }

    private static void endArray(MethodSpec.Builder builder, boolean arrayNullable) {
        if (arrayNullable) {
            builder.endControlFlow()
                    .nextControlFlow("else")
                    .addStatement("writer.writeBool(false)")
                    .endControlFlow();
        } else {
            builder.endControlFlow();
        }
    }
}
