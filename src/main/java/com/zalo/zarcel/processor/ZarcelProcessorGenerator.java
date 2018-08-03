package com.zalo.zarcel.processor;

import com.zalo.zarcel.ZarcelClass;
import com.zalo.zarcel.ZarcelProperty;

import java.io.IOException;
import java.util.*;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.annotation.processing.Filer;

class ZarcelProcessorGenerator {

    private static final byte PRIMITIVE_ARRAY = 0;
    private static final byte OBJECT_ARRAY = 1;
    private static final byte CONDITIONAL_OBJECT_ARRAY = 2;

    private TypeElement type;
    private Elements elements;
    private String packageName;
    private String className;
    boolean isConditionalObject = false;
    private ZarcelClass.Builder classBuilder = ZarcelClass.builder();
    private ArrayList<Map.Entry<Map.Entry<String, String>, Integer>> conditionalObject;

    void init(TypeElement type, Elements elements) {
        this.type = type;
        this.elements = elements;
        this.packageName = elements.getPackageOf(type).getQualifiedName().toString();
        this.className = elements.getTypeElement(type.getQualifiedName().toString()).getSimpleName().toString();
        conditionalObject = new ArrayList<Map.Entry<Map.Entry<String, String>, Integer>>();
    }

    void generate(TypeElement type, Elements elements, Filer filer) throws IOException {
        init(type, elements);
        parseClassAnnotation();
        parseClass();
        parseProperties();
        finish(filer);
    }

    private void finish(Filer filer) throws IOException {
        classBuilder.build().generateFile(filer);
    }

    private void parseClassAnnotation() {
        Zarcel annotation = type.getAnnotation(Zarcel.class);
        boolean isParentAbstract = false;
        if (annotation != null) {
            classBuilder.setVersion(annotation.version());
            String superClass = type.getSuperclass().toString();
            Set<Modifier> modifiers = elements.getTypeElement(type.getSuperclass().toString()).getModifiers();

            for (Modifier modifier : modifiers) {
                if (modifier.toString().equals("abstract")) {
                    isParentAbstract = true;
                    break;
                }
            }
            if (superClass.equals("java.lang.Object") || isParentAbstract) {
                classBuilder.setSerializeParent(false);
            } else {
                classBuilder.setSerializeParent(annotation.serializedParent());
                if (annotation.serializedParent()) {
                    Element superClassElement = elements.getTypeElement(superClass);
                    Map.Entry<String, String> parentClass =
                            new AbstractMap.SimpleEntry<>(
                                    elements.getPackageOf(superClassElement).toString(),
                                    elements.getTypeElement(superClass).getSimpleName().toString());
                    classBuilder.setParentClass(parentClass);
                }
            }
        }
    }

    private void parseClass() {
        classBuilder.setName(className);
        classBuilder.setThisPackage(packageName);
    }

    private void parseProperties() {
        List<? extends Element> properties = elements.getAllMembers(type);
        for (Element property : properties) {
            parseProperty(property);
        }
    }

    private void parseProperty(Element property) {
        if (property.getKind() != ElementKind.FIELD) return;

        for (Modifier modifier : property.getModifiers()) {
            if (modifier.toString().equals("static") || modifier.toString().equals("private"))
                return;
        }

        if (!property.getEnclosingElement().toString().equals(type.toString())) {
            return;
        }

        if (property.asType().getKind().isPrimitive()) {
            parsePrimitiveProperty(property);
        } else if (property.asType().getKind() == TypeKind.DECLARED) {
            parseDeclaredProperty(property);
        } else if (property.asType().getKind() == TypeKind.ARRAY) {
            parseArrayProperty(property);
        }
    }

    private void parsePrimitiveProperty(Element property) {
        // Config
        ZarcelProperty.Builder builder = ZarcelProperty.builder();
        parseInnerAnnotation(property, builder);
        builder.setType(ZarcelProperty.Type.PRIMITIVE);
        builder.setPropertyName(property.getSimpleName().toString());
        // Builder
        switch (property.asType().getKind()) {
            case FLOAT:
                builder.setDataType(new AbstractMap.SimpleEntry<>(null, "float"));
                break;
            case DOUBLE:
                builder.setDataType(new AbstractMap.SimpleEntry<>(null, "double"));
                break;
            case INT:
                builder.setDataType(new AbstractMap.SimpleEntry<>(null, "int"));
                break;
            case BOOLEAN:
                builder.setDataType(new AbstractMap.SimpleEntry<>(null, "boolean"));
                break;
        }
        classBuilder.addProperty(builder.build());
    }

    private void parseDeclaredProperty(Element property) {
        // Config
        ZarcelProperty.Builder builder = ZarcelProperty.builder();
        parseInnerAnnotation(property, builder);
        String objectPackage = elements.getPackageOf(property).toString();
        String objectName = elements.getTypeElement(property.asType().toString()).getSimpleName().toString();
        if (objectName.equals("String")) {
            builder.setType(ZarcelProperty.Type.PRIMITIVE)
                    .setDataType(new AbstractMap.SimpleEntry<>(null, "String"))
                    .setPropertyName(property.getSimpleName().toString());
        } else if (!isConditionalObject) {
            builder.setType(ZarcelProperty.Type.OBJECT)
                    .setDataType(new AbstractMap.SimpleEntry<>(objectPackage, objectName))
                    .setPropertyName(property.getSimpleName().toString());
        } else if (isConditionalObject) {
            builder.setDataType(new AbstractMap.SimpleEntry<>(objectPackage, objectName))
                    .setType(ZarcelProperty.Type.CONDITIONAL_OBJECT)
                    .setPropertyName(property.getSimpleName().toString());
            for (Map.Entry<Map.Entry<String, String>, Integer> aConditionalObject : conditionalObject) {
                builder.addConditionalProperty(aConditionalObject);
            }
            // When done
            conditionalObject.clear();
        }
        // Build
        classBuilder.addProperty(builder.build());
    }

    private void parseArrayProperty(Element property) {
        ZarcelProperty.Builder builder = ZarcelProperty.builder();
        parseInnerAnnotation(property, builder);

        byte type = checkType(property);
        switch (type) {
            case PRIMITIVE_ARRAY:
                parsePrimitiveArray(property, builder);
                break;
            case OBJECT_ARRAY:
                parseObjectArray(property, builder);
                break;
            case CONDITIONAL_OBJECT_ARRAY:
                parseConditionalObjectArray(property, builder);
                break;
        }
        classBuilder.addProperty(builder.build());
    }

    private byte checkType(Element property) {
        if (isConditionalObject)
            return CONDITIONAL_OBJECT_ARRAY;
        String type = property.asType().toString().replace("[]", "");
        if (type.contains("int") || type.contains("float") || type.contains("double") || type.contains("boolean")) {
            return PRIMITIVE_ARRAY;
        }
        return OBJECT_ARRAY;
    }

    private void parsePrimitiveArray(Element property, ZarcelProperty.Builder builder) {

        builder.setType(ZarcelProperty.Type.PRIMITIVE_ARRAY)
                .setPropertyName(property.getSimpleName().toString());

        String kind = property.asType().toString().replace("[]", "");
        switch (kind) {
            case "float":
                builder.setDataType(new AbstractMap.SimpleEntry<>(null, "float"));
                break;
            case "double":
                builder.setDataType(new AbstractMap.SimpleEntry<>(null, "double"));
                break;
            case "int":
                builder.setDataType(new AbstractMap.SimpleEntry<>(null, "int"));
                break;
            case "boolean":
                builder.setDataType(new AbstractMap.SimpleEntry<>(null, "boolean"));
                break;
        }
    }

    private void parseObjectArray(Element property, ZarcelProperty.Builder builder) {

        String objectPackage = elements.getPackageOf(property).toString();
        String objectName = elements.getTypeElement(property.asType().toString().replace("[]", ""))
                .getSimpleName()
                .toString();

        if (objectName.equals("String")) {
            builder.setType(ZarcelProperty.Type.PRIMITIVE_ARRAY)
                    .setDataType(new AbstractMap.SimpleEntry<>(null, "String"))
                    .setPropertyName(property.getSimpleName().toString());
        } else {
            builder.setType(ZarcelProperty.Type.OBJECT_ARRAY)
                    .setDataType(new AbstractMap.SimpleEntry<>(objectPackage, objectName))
                    .setPropertyName(property.getSimpleName().toString());
        }
    }

    private void parseConditionalObjectArray(Element property, ZarcelProperty.Builder builder) {
        String objectPackage = elements.getPackageOf(property).toString();
        String objectName = elements.getTypeElement(property.asType().toString().replace("[]", ""))
                .getSimpleName()
                .toString();

        builder.setDataType(new AbstractMap.SimpleEntry<>(objectPackage, objectName))
                .setType(ZarcelProperty.Type.CONDITIONAL_OBJECT_ARRAY)
                .setPropertyName(property.getSimpleName().toString());

        for (Map.Entry<Map.Entry<String, String>, Integer> aConditionalObject : conditionalObject) {
            builder.addConditionalProperty(aConditionalObject);
        }
        // When done
        conditionalObject.clear();
    }

    private void parseInnerAnnotation(Element property, ZarcelProperty.Builder builder) {

        //checking another annotations
        List<? extends AnnotationMirror> annotationMirrors = property.getAnnotationMirrors();
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            String simpleName = annotationMirror.getAnnotationType().asElement().getSimpleName().toString();
            if (simpleName.equals("NonNull")) {
                builder.setObjectNullable(false);
            }
        }

        //

        Zarcel.Property annotationProperty = property.getAnnotation(Zarcel.Property.class);
        if (annotationProperty != null) {
            builder.setVersion(annotationProperty.sinceVersion());
            if (annotationProperty.arraySize() > 0) {
                builder.setArraySize(annotationProperty.arraySize());
            }
        }

        Zarcel.Abstract annotationAbstract = property.getAnnotation(Zarcel.Abstract.class);
        if (annotationAbstract != null) {
            isConditionalObject = true;
            parseAnnotationConditionalObject(property);
        }
    }

    private void parseAnnotationConditionalObject(Element property) {
        TypeMirror abstractType = elements.getTypeElement("com.zalo.zarcel.processor.Zarcel.Abstract").asType();
        List<? extends AnnotationMirror> annotationMirrors = property.getAnnotationMirrors();
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = annotationMirror.getElementValues();
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : elementValues.entrySet()) {
                String key = entry.getKey().getSimpleName().toString();
                Object value = entry.getValue().getValue();
                int i = 0;
                if (key.equals("childClass")) {
                    List<? extends AnnotationValue> typeMirrors = (List<? extends AnnotationValue>) value;
                    for (AnnotationValue typeMirror : typeMirrors) {
                        TypeMirror childClass = ((TypeMirror) typeMirror.getValue());
                        String fullName = childClass.toString();
                        String className = elements.getTypeElement(fullName).getSimpleName().toString();
                        String packageClass = elements.getPackageOf(elements.getTypeElement(fullName)).toString();
                        conditionalObject.add(
                                new AbstractMap.SimpleEntry<>(new AbstractMap.SimpleEntry<>(packageClass, className), i++));
                    }
                } else if (key.equals("type")) {
                    List<? extends AnnotationValue> typeMirrors = (List<? extends AnnotationValue>) value;
                    for (AnnotationValue typeMirror : typeMirrors) {
                        Integer childType = ((Integer) typeMirror.getValue());
                        conditionalObject.get(i++).setValue(childType);
                    }
                }
            }
        }
    }
}
