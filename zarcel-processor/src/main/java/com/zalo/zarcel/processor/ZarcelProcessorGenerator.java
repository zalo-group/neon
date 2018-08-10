package com.zalo.zarcel.processor;

import com.zing.zalo.Exception.ZarcelException;
import com.zing.zalo.annotations.Zarcel;
import com.zalo.zarcel.ZarcelClass;
import com.zalo.zarcel.ZarcelProperty;

import javax.annotation.processing.Filer;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class ZarcelProcessorGenerator {

    private static final byte PRIMITIVE_ARRAY = 0;
    private static final byte OBJECT_ARRAY = 1;
    private static final byte CONDITIONAL_OBJECT_ARRAY = 2;

    private static final String ZARCEL_CUSTOM_ADAPTER_NAME = Zarcel.Custom.class.getCanonicalName();

    private TypeElement type;
    private Elements elements;
    private String packageName;
    private String className;
    private boolean isCustomProperty = false;
    private ZarcelClass.Builder classBuilder = ZarcelClass.builder();

    void init(TypeElement type, Elements elements) {
        this.type = type;
        this.elements = elements;
        this.packageName = getPackage(type);
        this.className = getClassName(type, packageName);
    }

    void generate(TypeElement type, Elements elements, Filer filer) throws IOException, ZarcelException {
        init(type, elements);
        parseClassAnnotation();
        parseClass();
        parseProperties();
        finish(filer);
    }

    private void parseThrownClass() {

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
                classBuilder.setSerializeParent(annotation.inheritanceSupported());
                if (annotation.inheritanceSupported()) {
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

    private void parseProperties() throws ZarcelException {
        List<? extends Element> properties = elements.getAllMembers(type);
        for (Element property : properties) {
            parseProperty(property);
        }
    }

    private void parseProperty(Element property) throws ZarcelException {
        if (property.getKind() != ElementKind.FIELD) return;

        for (Modifier modifier : property.getModifiers()) {
            if (modifier.toString().equals("static") ||
                    modifier.toString().equals("private") ||
                    modifier.toString().equals("transient"))
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

    private void parsePrimitiveProperty(Element property) throws ZarcelException {
        // Config
        ZarcelProperty.Builder builder = ZarcelProperty.builder();
        parseInnerAnnotation(property, builder);
        if (isCustomProperty) {
            isCustomProperty = false;
            return;
        }
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
            case LONG:
                builder.setDataType(new AbstractMap.SimpleEntry<>(null, "long"));
                break;
        }
        classBuilder.addProperty(builder.build());
    }

    private void parseDeclaredProperty(Element property) throws ZarcelException {
        // Config
        ZarcelProperty.Builder builder = ZarcelProperty.builder();
        parseInnerAnnotation(property, builder);
        if (isCustomProperty) {
            isCustomProperty = false;
            return;
        }

        String objectPackage = elements.getPackageOf(elements.getTypeElement(property.asType().toString())).toString();
        String objectName = elements.getTypeElement(property.asType().toString()).getSimpleName().toString();
        if (objectName.equals("String")) {
            builder.setType(ZarcelProperty.Type.PRIMITIVE)
                    .setDataType(new AbstractMap.SimpleEntry<>(null, "String"))
                    .setPropertyName(property.getSimpleName().toString());
        } else {
            builder.setType(ZarcelProperty.Type.OBJECT)
                    .setDataType(new AbstractMap.SimpleEntry<>(objectPackage, objectName))
                    .setPropertyName(property.getSimpleName().toString());
        }
        classBuilder.addProperty(builder.build());
    }

    private void parseArrayProperty(Element property) throws ZarcelException {
        ZarcelProperty.Builder builder = ZarcelProperty.builder();
        parseInnerAnnotation(property, builder);
        if (isCustomProperty) {
            isCustomProperty = false;
            return;
        }

        byte type = checkType(property);
        switch (type) {
            case PRIMITIVE_ARRAY:
                parsePrimitiveArray(property, builder);
                break;
            case OBJECT_ARRAY:
                parseObjectArray(property, builder);
                break;
        }
        classBuilder.addProperty(builder.build());
    }

    private byte checkType(Element property) {
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
            case "long":
                builder.setDataType(new AbstractMap.SimpleEntry<>(null, "long"));
                break;
        }
    }

    private void parseObjectArray(Element property, ZarcelProperty.Builder builder) {

        String objectPackage = elements.getPackageOf(elements.getTypeElement(property.asType().toString().replace("[]", ""))).toString();
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

    private void parseInnerAnnotation(Element property, ZarcelProperty.Builder builder) throws ZarcelException {

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

        Zarcel.Custom annotationAbstract = property.getAnnotation(Zarcel.Custom.class);
        if (annotationAbstract != null) {
            parseCustomAdapter(property, builder);
        }
    }

    private void parseCustomAdapter(Element property, ZarcelProperty.Builder builder) throws ZarcelException {
        List<? extends AnnotationMirror> annotationMirrors = property.getAnnotationMirrors();
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            if (!annotationMirror.getAnnotationType().toString().equals(ZARCEL_CUSTOM_ADAPTER_NAME))
                continue;
            Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = annotationMirror.getElementValues();
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : elementValues.entrySet()) {
                String key = entry.getKey().getSimpleName().toString();
                Object value = entry.getValue().getValue();
                if (key.equals("adapter")) {
                    TypeElement typeOfClass = elements.getTypeElement(((DeclaredType) value).asElement().toString());
                    String customAdapterPackage = getPackage(typeOfClass);
                    String customAdapterName = getClassName(typeOfClass, customAdapterPackage);
                    String simpleName = property.getSimpleName().toString();

                    // Check serialize and deserialize
                    boolean serializeChecker = false, deSerializeChecker = false;
                    List<? extends Element> valueElements = elements.getAllMembers((TypeElement) ((DeclaredType) value).asElement());
                    for (Element element : valueElements) {
                        if (element.getSimpleName().toString().equals("serialize")) {
                            serializeChecker = true;
                            List<? extends TypeMirror> thrownTypes = ((ExecutableElement) element).getThrownTypes();
                            for (TypeMirror thrownType : thrownTypes) {
                                TypeElement thrownClass = elements.getTypeElement(thrownType.toString());
                                String thrownClassPackage = elements.getPackageOf(thrownClass).toString();
                                String thrownClassName = thrownClass.getSimpleName().toString();
                                classBuilder.addSerializeException(new AbstractMap.SimpleEntry<>(thrownClassPackage, thrownClassName));
                            }
                        } else if (element.getSimpleName().toString().equals("createFromSerialized")) {
                            deSerializeChecker = true;
                            List<? extends TypeMirror> thrownTypes = ((ExecutableElement) element).getThrownTypes();
                            for (TypeMirror thrownType : thrownTypes) {
                                TypeElement thrownClass = elements.getTypeElement(thrownType.toString());
                                String thrownClassPackage = elements.getPackageOf(thrownClass).toString();
                                String thrownClassName = thrownClass.getSimpleName().toString();
                                classBuilder.addDeserializeException(new AbstractMap.SimpleEntry<>(thrownClassPackage, thrownClassName));
                            }
                        }
                    }

                    if (serializeChecker && deSerializeChecker) {

                    } else {
                        throw new ZarcelException(customAdapterName + " does not implement valid ZarcelAdapter for custom property.");
                    }

                    // Build and return
                    builder.setCustomAdapter(true);
                    isCustomProperty = true;
                    builder.setPropertyName(simpleName);
                    builder.setType(ZarcelProperty.Type.CUSTOM_ADAPTER);
                    builder.setDataType(new AbstractMap.SimpleEntry<>(customAdapterPackage, customAdapterName));
                    classBuilder.addProperty(builder.build());
                    return;
                }
            }
        }
    }

    String getPackage(TypeElement typeElement) {
        return elements.getPackageOf(typeElement).toString();
    }

    String getClassName(TypeElement typeElement, String packageName) {
        return typeElement.getQualifiedName().toString().replace(packageName + ".", "");
    }
}
