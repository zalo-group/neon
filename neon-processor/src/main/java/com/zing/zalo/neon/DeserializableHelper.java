package com.zing.zalo.neon;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import java.nio.charset.StandardCharsets;

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
        if (primitiveType.equals("String") && debug) {
            builder.beginControlFlow("if ($L.$L != null)", argClassName, attribute);
            builder.addStatement("builder.addByte($L.$L.getBytes($T.UTF_8).length)",
                    argClassName, attribute, StandardCharsets.class);
            builder.endControlFlow();
        }
    }

    static void readPrimitive(MethodSpec.Builder builder, String primitiveType, String argClassName,
            String attribute, boolean debug) {
        switch (primitiveType) {
            case "int":
                readPrimitiveSupport(builder, "$L.$L = reader.readInt32()", argClassName,
                        primitiveType, attribute, debug);
                break;
            case "double":
                readPrimitiveSupport(builder, "$L.$L = reader.readDouble()", argClassName,
                        primitiveType, attribute, debug);
                break;
            case "float":
                readPrimitiveSupport(builder, "$L.$L = (float)reader.readDouble()", argClassName,
                        primitiveType, attribute, debug);
                break;
            case "boolean":
                readPrimitiveSupport(builder, "$L.$L = reader.readBool()", argClassName,
                        primitiveType, attribute, debug);
                break;
            case "long":
                readPrimitiveSupport(builder, "$L.$L = reader.readInt64()", argClassName,
                        primitiveType, attribute, debug);
                break;
            case "String":
                readPrimitiveSupport(builder, "$L.$L = reader.readString()", argClassName,
                        primitiveType, attribute, debug);
                break;
        }
    }

    static void readPrimitiveArray(MethodSpec.Builder builder, NeonProperty property,
            String argClassName, boolean debug) {
        beginArray(builder, property, argClassName, property.objectNullable(), debug);
        //==========Process with i========
        readPrimitive(builder, property.dataType().getValue(), argClassName,
                property.propertyName() + "[i]", false);
        //================================
        endArray(builder, property.objectNullable(), debug, false);
    }

    static void readObject(MethodSpec.Builder builder, NeonProperty property, String argClassName,
            String attribute, boolean nullableObject, boolean debug, boolean isArray) {
        ClassName object =
                ClassName.get(property.dataType().getKey(), property.dataType().getValue());
        if (debug && !isArray) {
            builder.addStatement("builder.addObjectAttrName(\"$L\")", attribute);
        }
        if (!nullableObject || isArray) {
            if (isArray && debug) {
                builder.addStatement("builder.addObjectAttrName(\"[\"+i+\"]\")");
            }
            builder.addStatement("$L.$L = $T.CREATOR.createFromSerialized(reader,$L)", argClassName,
                    isArray ? attribute + "[i]" : attribute, object, debug ? "builder" : "null");
        } else {
            builder.beginControlFlow("if (reader.readBool())");
            builder.addStatement("$L.$L = $T.CREATOR.createFromSerialized(reader,$L)", argClassName,
                    attribute, object, debug ? "builder" : "null")
                    .nextControlFlow("else");
            if (debug) {
                builder.addStatement("builder.endNullObject()");
            }
            builder.endControlFlow();
        }
    }

    static void readObjectArray(MethodSpec.Builder builder, NeonProperty property,
            String argClassName, boolean debug) {
        beginArray(builder, property, argClassName, property.objectNullable(), debug);
        //==========Process with i========
        readObject(builder, property, argClassName, property.propertyName(), false, debug, true);
        //================================
        endArray(builder, property.objectNullable(), debug, true);
    }

    static void readCustomAdapter(MethodSpec.Builder builder, NeonProperty property,
            String argClassName, boolean debug) {
        String propertyName = property.propertyName();
        String adapterPackage = property.dataType().getKey();
        String adapterClassName = property.dataType().getValue();
        ClassName adapterClass = ClassName.get(adapterPackage, adapterClassName);

        if (debug) {
            builder.addStatement("builder.addObjectAttrName(\"$L\")", propertyName);
        }
        if (!property.objectNullable()) {
            builder.beginControlFlow("");
            if (debug) {
                builder.addStatement("builder.beginObject(\"$L\")",
                        "CustomAdapter -> " + adapterClassName);
            }
            builder.addStatement("$T tmp__customAdapter = new $T()", adapterClass, adapterClass);
            builder.addStatement("$L.$L = tmp__customAdapter.createFromSerialized(reader,builder)",
                    argClassName, propertyName);
            if (debug) {
                builder.addStatement("builder.endObject()");
            }
            builder.endControlFlow();
        } else {
            builder.beginControlFlow("if (reader.readBool())");
            if (debug) {
                builder.addStatement("builder.beginObject(\"$L\")",
                        "CustomAdapter -> " + adapterClassName);
            }
            builder.addStatement("$T tmp_customAdapter = new $T()", adapterClass, adapterClass);
            builder.addStatement("$L.$L = tmp_customAdapter.createFromSerialized(reader,builder)",
                    argClassName, propertyName);
            if (debug) {
                builder.addStatement("builder.endObject()");
            }
            builder.nextControlFlow("else");
            if (debug) {
                builder.addStatement("builder.endNullObject()");
            }
            builder.endControlFlow();
        }
    }

    private static void beginArray(MethodSpec.Builder builder, NeonProperty property,
            String argClassName, boolean arrayNullable, boolean debug) {
        String propertyName = property.propertyName();
        ClassName object = null;
        String objectSimpleName = "";

        if (property.type() != NeonProperty.Type.PRIMITIVE
                && property.type() != NeonProperty.Type.PRIMITIVE_ARRAY) {
            object = ClassName.get(property.dataType().getKey(), property.dataType().getValue());
            objectSimpleName = property.dataType().getValue();
        }

        if (debug) {
            builder.addStatement("builder.addObjectAttrName(\"$L\")", propertyName);
        }

        if (arrayNullable) {
            builder.beginControlFlow("if (reader.readBool())");
        } else {
            builder.beginControlFlow("");
        }
        builder.addStatement("int size = reader.readInt32()");
        // Primitive or object
        if (object != null) {
            builder.addStatement("$L.$L = new $T[size]", argClassName, propertyName, object);
            if (debug) {
                builder.addStatement("builder.beginObjectArray(\"$L\")", objectSimpleName);
            }
        } else {
            builder.addStatement("$L.$L = new $L[size]", argClassName, propertyName,
                    property.dataType().getValue());
            if (debug) {
                builder.addStatement("builder.endPrimitiveArray(\"$L\",size)",
                        property.dataType().getValue());
            }
        }

        builder.beginControlFlow("for (int i=0; i< size; i++)");
    }

    private static void endArray(MethodSpec.Builder builder, boolean arrayNullable, boolean debug,
            boolean isObject) {
        builder.endControlFlow();
        if (debug && isObject) {
            builder.addStatement("builder.endObjectArray()");
        }
        if (arrayNullable) {
            builder.nextControlFlow("else");
            if (debug) {
                builder.addStatement("builder.endNullObject()");
            }
        }
        builder.endControlFlow();
    }
}
