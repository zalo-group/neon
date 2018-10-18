package com.zalo.zarcel;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

public class DeserializableHelper {

    private static void readPrimitiveSupport(MethodSpec.Builder builder,
                                             String templateStatement,
                                             String argClassName,
                                             String primitiveType,
                                             String attribute,
                                             boolean debug) {
        if (debug) {
            builder.addStatement("builder.addType(\"$L\",\"$L\")", attribute, primitiveType);
        }
        builder.addStatement(templateStatement, argClassName, attribute);
    }

    static void readPrimitive(MethodSpec.Builder builder, String primitiveType, String argClassName, String attribute, boolean debug) {
        switch (primitiveType) {
            case "int":
                readPrimitiveSupport(builder, "$L.$L = reader.readInt32()", argClassName, primitiveType, attribute, debug);
                break;
            case "double":
                readPrimitiveSupport(builder, "$L.$L = reader.readDouble()", argClassName, primitiveType, attribute, debug);
                break;
            case "float":
                readPrimitiveSupport(builder, "$L.$L = (float)reader.readDouble()", argClassName, primitiveType, attribute, debug);
                break;
            case "boolean":
                readPrimitiveSupport(builder, "$L.$L = reader.readBool()", argClassName, primitiveType, attribute, debug);
                break;
            case "long":
                readPrimitiveSupport(builder, "$L.$L = reader.readInt64()", argClassName, primitiveType, attribute, debug);
                break;
            case "String":
                readPrimitiveSupport(builder, "$L.$L = reader.readString()", argClassName, primitiveType, attribute, debug);
                break;
        }
    }

    static void readPrimitiveArray(MethodSpec.Builder builder, ZarcelProperty property, String argClassName, boolean debug) {
        beginArray(builder, property, argClassName, property.objectNullable(), debug);
        //==========Process with i========
        readPrimitive(builder, property.dataType().getValue(), argClassName, property.propertyName() + "[i]", false);
        //================================
        endArray(builder, property.objectNullable());
    }

    static void readObject(MethodSpec.Builder builder, ZarcelProperty property, String argClassName, String attribute, boolean nullableObject, boolean debug) {
        ClassName object = ClassName.get(property.dataType().getKey(), property.dataType().getValue());
        if (debug)
            builder.addStatement("builder.addObject(\"$L\")", attribute);
        if (!nullableObject) {
            builder.addStatement("$L.$L = $T.CREATOR.createFromSerialized(reader,$L)", argClassName, attribute, object, debug ? "builder" : "null");
        } else {
            builder.beginControlFlow("if (reader.readBool())");
            builder.addStatement("$L.$L = $T.CREATOR.createFromSerialized(reader,$L)", argClassName, attribute, object, debug ? "builder" : "null")
                    .nextControlFlow("else");
            if (debug)
                builder.addStatement("builder.endNullObject()");
            builder.endControlFlow();
        }
    }

    static void readObjectArray(MethodSpec.Builder builder, ZarcelProperty property, String argClassName, boolean debug) {
        beginArray(builder, property, argClassName, property.objectNullable(), debug);
        //==========Process with i========
        readObject(builder, property, argClassName, property.propertyName() + "[i]", false, false);
        //================================
        endArray(builder, property.objectNullable());
    }

    static void readCustomAdapter(MethodSpec.Builder builder, ZarcelProperty property, String argClassName, boolean debug) {
        String propertyName = property.propertyName();
        String adapterPackage = property.dataType().getKey();
        String adapterClassName = property.dataType().getValue();
        ClassName adapterClass = ClassName.get(adapterPackage, adapterClassName);

        if (debug) {
            builder.addStatement("builder.addObject(\"$L\")", propertyName);
        }
        if (!property.objectNullable()) {
            builder.beginControlFlow("");
            if (debug) {
                builder.addStatement("builder.beginObject(\"$L\")", "CustomAdapter -> " + adapterClassName);
            }
            builder.addStatement("$T tmp__customAdapter = new $T()", adapterClass, adapterClass);
            builder.addStatement("$L.$L = tmp__customAdapter.createFromSerialized(reader,builder)", argClassName, propertyName);
            if (debug) {
                builder.addStatement("builder.endObject()");
            }
            builder.endControlFlow();
        } else {
            builder.beginControlFlow("if (reader.readBool())");
            if (debug) {
                builder.addStatement("builder.beginObject(\"$L\")", "CustomAdapter -> " + adapterClassName);
            }
            builder.addStatement("$T tmp_customAdapter = new $T()", adapterClass, adapterClass);
            builder.addStatement("$L.$L = tmp_customAdapter.createFromSerialized(reader,builder)", argClassName, propertyName);
            if (debug) {
                builder.addStatement("builder.endObject()");
            }
            builder.nextControlFlow("else");
            if (debug)
                builder.addStatement("builder.endNullObject()");
            builder.endControlFlow();
        }
    }

    private static void beginArray(MethodSpec.Builder builder, ZarcelProperty property, String argClassName, boolean arrayNullable, boolean debug) {
        String propertyName = property.propertyName();
        ClassName object = null;
        String objectSimpleName = "";

        if (property.type() != ZarcelProperty.Type.PRIMITIVE && property.type() != ZarcelProperty.Type.PRIMITIVE_ARRAY) {
            object = ClassName.get(property.dataType().getKey(), property.dataType().getValue());
            objectSimpleName = property.dataType().getValue();
        }

        if (debug) {
            builder.addStatement("builder.addType(\"$L\",\"$L\")", propertyName,
                    (object != null ? objectSimpleName : property.dataType().getValue()) + "[]");
        }

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
