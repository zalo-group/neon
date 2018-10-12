package com.zalo.zarcel;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

public class DeserializableHelper {
    static void readPrimitive(MethodSpec.Builder builder, String primitiveType, String argClassName, String attribute) {
        switch (primitiveType) {
            case "int":
                builder.addStatement("$L.$L = reader.readInt32()", argClassName, attribute);
                break;
            case "double":
                builder.addStatement("$L.$L = reader.readDouble()", argClassName, attribute);
                break;
            case "float":
                builder.addStatement("$L.$L = (float)reader.readDouble()", argClassName, attribute);
                break;
            case "boolean":
                builder.addStatement("$L.$L = reader.readBool()", argClassName, attribute);
                break;
            case "long":
                builder.addStatement("$L.$L = reader.readInt64()", argClassName, attribute);
                break;
            case "String":
                builder.addStatement("$L.$L = reader.readString()", argClassName, attribute);
                break;
        }
    }

    static void readPrimitiveArray(MethodSpec.Builder builder, ZarcelProperty property, String argClassName) {
        beginArray(builder, property, argClassName, property.objectNullable());
        //==========Process with i========
        readPrimitive(builder, property.dataType().getValue(), argClassName, property.propertyName() + "[i]");
        //================================
        endArray(builder, property.objectNullable());
    }

    static void readObject(MethodSpec.Builder builder, ZarcelProperty property, String argClassName, String attribute, boolean nullableObject) {
        ClassName object = ClassName.get(property.dataType().getKey(), property.dataType().getValue());
        if (!nullableObject) {
            builder.addStatement("$L.$L = $T.CREATOR.createFromSerialized(reader)", argClassName, attribute, object);
        } else {
            builder.beginControlFlow("if (reader.readBool())")
                    .addStatement("$L.$L = $T.CREATOR.createFromSerialized(reader)", argClassName, attribute, object)
                    .endControlFlow();
        }
    }

    static void readObjectArray(MethodSpec.Builder builder, ZarcelProperty property, String argClassName) {
        beginArray(builder, property, argClassName, property.objectNullable());
        //==========Process with i========
        readObject(builder, property, argClassName, property.propertyName() + "[i]", false);
        //================================
        endArray(builder, property.objectNullable());
    }

    static void readCustomAdapter(MethodSpec.Builder builder, ZarcelProperty property, String argClassName) {
        String propertyName = property.propertyName();
        String adapterPackage = property.dataType().getKey();
        String adapterClassName = property.dataType().getValue();
        ClassName adapterClass = ClassName.get(adapterPackage, adapterClassName);

        if (!property.objectNullable()) {
            builder.beginControlFlow("");
            builder.addStatement("$T tmp__customAdapter = new $T()", adapterClass, adapterClass);
            builder.addStatement("$L.$L = tmp__customAdapter.createFromSerialized(reader)", argClassName, propertyName);
            builder.endControlFlow();
        } else {
            builder.beginControlFlow("if (reader.readBool())")
                    .addStatement("$T tmp_customAdapter = new $T()", adapterClass, adapterClass);
            builder.addStatement("$L.$L = tmp_customAdapter.createFromSerialized(reader)", argClassName, propertyName);
            builder.endControlFlow();
        }
    }

    private static void beginArray(MethodSpec.Builder builder, ZarcelProperty property, String argClassName, boolean arrayNullable) {
        String propertyName = property.propertyName();
        ClassName object = null;
        if (property.type() != ZarcelProperty.Type.PRIMITIVE && property.type() != ZarcelProperty.Type.PRIMITIVE_ARRAY)
            object = ClassName.get(property.dataType().getKey(), property.dataType().getValue());

        if (arrayNullable) {
            builder.beginControlFlow("if (reader.readBool())")
                    .addStatement("int size = reader.readInt32()");
        }
        // Primitive or object
        if (object != null) {
            builder.addStatement("$L.$L = new $T[size]", argClassName, propertyName, object);
        } else {
            builder.addStatement("$L.$L = new $L[size]", argClassName, propertyName, property.dataType().getValue());
        }

        builder.beginControlFlow("for (int i=0; i< size; i++)");
    }

    private static void endArray(MethodSpec.Builder builder, boolean arrayNullable) {
        builder.endControlFlow();
        if (arrayNullable) {
            // end readBool()
            builder.endControlFlow();
        }
    }
}
