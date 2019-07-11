package com.zing.zalo.neon;

import com.squareup.javapoet.ClassName;
import com.zing.zalo.neon.annotations.Migrator;
import com.zing.zalo.neon.annotations.Neon;
import com.zing.zalo.neon.exception.NeonException;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

class NeonProcessorGenerator {

    private static final byte PRIMITIVE_ARRAY = 0;
    private static final byte OBJECT_ARRAY = 1;

    private static final String ZARCEL_CUSTOM_ADAPTER_NAME = Neon.Custom.class.getCanonicalName();
    private static final String MIGRATOR_NAME = Migrator.class.getCanonicalName();
    private TypeElement type;
    private Elements elements;
    private Messager messager;
    private String packageName;
    private String className;
    private boolean isCustomProperty = false;
    private NeonClass.Builder classBuilder = NeonClass.builder();

    void init(TypeElement type, Elements elements, Messager messager) {
        this.type = type;
        this.elements = elements;
        this.packageName = getPackage(type);
        this.className = getClassName(type, packageName);
        this.messager = messager;
    }

    void generate(TypeElement type, Elements elements, Filer filer, Messager messager)
            throws IOException,
            NeonException {
        init(type, elements, messager);
        parseClassAnnotation();
        parseClass();
        parseProperties();
        finish(filer, type);
    }

    private void finish(Filer filer, Element originElement) throws IOException {
        classBuilder.build().generateFile(filer, originElement);
    }

    private void parseClassAnnotation() {
        Neon annotation = type.getAnnotation(Neon.class);
        boolean isParentAbstract = false;
        if (annotation != null) {
            classBuilder.setVersion(annotation.version());
            classBuilder.setCompatibleSince(annotation.compatibleSince());
            String superClass = type.getSuperclass().toString();
            Set<Modifier> modifiers =
                    elements.getTypeElement(type.getSuperclass().toString()).getModifiers();

            for (Modifier modifier : modifiers) {
                if (modifier.toString().equals("abstract")) {
                    isParentAbstract = true;
                    break;
                }
            }
            if (superClass.equals("java.lang.Object") || isParentAbstract) {
                classBuilder.setInheritanceSupported(false);
            } else {
                classBuilder.setInheritanceSupported(annotation.inheritanceSupported());
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

        // Migrate
        Migrator migratorAnnotation = type.getAnnotation(Migrator.class);
        if (migratorAnnotation != null) {
            List<? extends AnnotationMirror> annotationMirrors = type.getAnnotationMirrors();
            for (AnnotationMirror annotationMirror : annotationMirrors) {
                if (!annotationMirror.getAnnotationType().toString().equals(MIGRATOR_NAME)) {
                    continue;
                }
                Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues =
                        annotationMirror.getElementValues();
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : elementValues
                        .entrySet()) {
                    String key = entry.getKey().getSimpleName().toString();
                    Object value = entry.getValue().getValue();
                    if (key.equals("value")) {
                        TypeElement typeOfClass = elements.getTypeElement(
                                ((DeclaredType) value).asElement().toString());
                        boolean implementNeonMigrator = false;
                        List<? extends TypeMirror> interfaces = typeOfClass.getInterfaces();
                        for (TypeMirror each : interfaces) {
                            if (each.getKind() == TypeKind.DECLARED) {
                                if (((DeclaredType) each).asElement().toString().equals(
                                        NeonMigrator.class.getName())) {
                                    implementNeonMigrator = true;
                                    break;
                                }
                            }
                        }
                        if (implementNeonMigrator) {
                            classBuilder.setMigrateClass(ClassName.get(typeOfClass));
                        } else {
                            messager.printMessage(Diagnostic.Kind.ERROR,
                                    ((DeclaredType) value).asElement().getSimpleName()
                                            + " does not implement NeonMigrator ("
                                            + type.getQualifiedName()
                                            + ")");
                        }
                    }
                }
            }
        }
    }

    private void parseClass() {
        classBuilder.setName(className);
        classBuilder.setThisPackage(packageName);
    }

    private void parseProperties() throws NeonException {
        List<? extends Element> properties = elements.getAllMembers(type);
        for (Element property : properties) {
            parseProperty(property);
        }
    }

    private void parseProperty(Element property) throws NeonException {
        if (property.getKind() != ElementKind.FIELD) return;

        boolean isPrivate = false;
        for (Modifier modifier : property.getModifiers()) {
            if (modifier.toString().equals("static") || modifier.toString().equals("transient")) {
                return;
            }
            if (modifier.toString().equals("private")) {
                isPrivate = true;
            }
        }

        List<? extends AnnotationMirror> annotationMirrors = property.getAnnotationMirrors();
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            String simpleName =
                    annotationMirror.getAnnotationType().asElement().getSimpleName().toString();
            if (simpleName.equals("Ignore")) {
                return;
            }
        }

        if (isPrivate) {
            showPrivateWarning(property);
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

    private void showPrivateWarning(Element property) {

        messager.printMessage(Diagnostic.Kind.WARNING, "private property: "
                + property.getSimpleName()
                + " in "
                + property.getEnclosingElement().toString()
                + " is ignored.");
    }

    private void parsePrimitiveProperty(Element property) throws NeonException {
        // Config
        NeonProperty.Builder builder = NeonProperty.builder();
        parseInnerAnnotation(property, builder);
        if (isCustomProperty) {
            isCustomProperty = false;
            return;
        }
        builder.setType(NeonProperty.Type.PRIMITIVE);
        builder.setPropertyName(property.getSimpleName().toString());
        // Builder
        switch (property.asType().getKind()) {
            case FLOAT:
                builder.setDataType(new AbstractMap.SimpleEntry<>((String) null, "float"));
                break;
            case DOUBLE:
                builder.setDataType(new AbstractMap.SimpleEntry<>((String) null, "double"));
                break;
            case INT:
                builder.setDataType(new AbstractMap.SimpleEntry<>((String) null, "int"));
                break;
            case BOOLEAN:
                builder.setDataType(new AbstractMap.SimpleEntry<>((String) null, "boolean"));
                break;
            case LONG:
                builder.setDataType(new AbstractMap.SimpleEntry<>((String) null, "long"));
                break;
        }
        classBuilder.addProperty(builder.build());
    }

    private boolean validDeclaredProperty(Element property) {
        String propertyClassName = property.asType().toString().replace("[]", "");
        TypeElement typeElement = elements.getTypeElement(propertyClassName);
        if (typeElement.getAnnotation(Neon.class) == null) {
            messager.printMessage(Diagnostic.Kind.ERROR,
                    "In class "
                            + property.getEnclosingElement().toString()
                            +
                            ", property "
                            + property.getSimpleName()
                            + ": "
                            + propertyClassName
                            + " can not be serialized. "
                            +
                            "Add transient or @Ignore to skip serialization, or "
                            +
                            "add @Neon.Custom(adapter) to customize your own.");
            return false;
        } else {
            return true;
        }
    }

    private void parseDeclaredProperty(Element property) throws NeonException {

        // Config
        NeonProperty.Builder builder = NeonProperty.builder();
        parseInnerAnnotation(property, builder);
        if (isCustomProperty) {
            isCustomProperty = false;
            return;
        }

        String objectPackage =
                elements.getPackageOf(elements.getTypeElement(property.asType().toString()))
                        .toString();
        String objectName =
                elements.getTypeElement(property.asType().toString()).getSimpleName().toString();
        if (objectName.equals("String")) {
            builder.setType(NeonProperty.Type.PRIMITIVE)
                    .setDataType(new AbstractMap.SimpleEntry<>((String) null, "String"))
                    .setPropertyName(property.getSimpleName().toString());
        } else {
            if (!validDeclaredProperty(property)) {
                return;
            }
            builder.setType(NeonProperty.Type.OBJECT)
                    .setDataType(new AbstractMap.SimpleEntry<>(objectPackage, objectName))
                    .setPropertyName(property.getSimpleName().toString());
        }
        classBuilder.addProperty(builder.build());
    }

    private void parseArrayProperty(Element property) throws NeonException {
        NeonProperty.Builder builder = NeonProperty.builder();
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
                if (parseObjectArray(property, builder)) {
                    break;
                } else {
                    return;
                }
        }
        classBuilder.addProperty(builder.build());
    }

    private byte checkType(Element property) {
        String type = property.asType().toString().replace("[]", "");
        if (type.contains("int")
                || type.contains("float")
                || type.contains("double")
                || type.contains("boolean")) {
            return PRIMITIVE_ARRAY;
        }
        return OBJECT_ARRAY;
    }

    private void parsePrimitiveArray(Element property, NeonProperty.Builder builder) {

        builder.setType(NeonProperty.Type.PRIMITIVE_ARRAY)
                .setPropertyName(property.getSimpleName().toString());

        String kind = property.asType().toString().replace("[]", "");
        switch (kind) {
            case "float":
                builder.setDataType(new AbstractMap.SimpleEntry<>((String) null, "float"));
                break;
            case "double":
                builder.setDataType(new AbstractMap.SimpleEntry<>((String) null, "double"));
                break;
            case "int":
                builder.setDataType(new AbstractMap.SimpleEntry<>((String) null, "int"));
                break;
            case "boolean":
                builder.setDataType(new AbstractMap.SimpleEntry<>((String) null, "boolean"));
                break;
            case "long":
                builder.setDataType(new AbstractMap.SimpleEntry<>((String) null, "long"));
                break;
        }
    }

    private boolean parseObjectArray(Element property, NeonProperty.Builder builder) {

        String objectPackage = elements.getPackageOf(
                elements.getTypeElement(property.asType().toString().replace("[]", ""))).toString();
        String objectName = elements.getTypeElement(property.asType().toString().replace("[]", ""))
                .getSimpleName()
                .toString();

        if (objectName.equals("String")) {
            builder.setType(NeonProperty.Type.PRIMITIVE_ARRAY)
                    .setDataType(new AbstractMap.SimpleEntry<>((String) null, "String"))
                    .setPropertyName(property.getSimpleName().toString());
        } else {
            if (!validDeclaredProperty(property)) {
                return false;
            }
            builder.setType(NeonProperty.Type.OBJECT_ARRAY)
                    .setDataType(new AbstractMap.SimpleEntry<>(objectPackage, objectName))
                    .setPropertyName(property.getSimpleName().toString());
        }
        return true;
    }

    private void parseInnerAnnotation(Element property, NeonProperty.Builder builder) throws
            NeonException {

        //checking another annotations
        List<? extends AnnotationMirror> annotationMirrors = property.getAnnotationMirrors();
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            String simpleName =
                    annotationMirror.getAnnotationType().asElement().getSimpleName().toString();
            if (simpleName.equals("NonNull") || simpleName.equals("NotNull")) {
                builder.setObjectNullable(false);
            }
        }

        //

        Neon.Property annotationProperty = property.getAnnotation(Neon.Property.class);
        if (annotationProperty != null) {
            builder.setVersion(annotationProperty.sinceVersion());
        }

        Neon.Custom annotationAbstract = property.getAnnotation(Neon.Custom.class);
        if (annotationAbstract != null) {
            parseCustomAdapter(property, builder);
        }
    }

    private void parseCustomAdapter(Element property, NeonProperty.Builder builder) throws
            NeonException {
        List<? extends AnnotationMirror> annotationMirrors = property.getAnnotationMirrors();
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            if (!annotationMirror.getAnnotationType()
                    .toString()
                    .equals(ZARCEL_CUSTOM_ADAPTER_NAME)) {
                continue;
            }
            Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues =
                    annotationMirror.getElementValues();
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : elementValues
                    .entrySet()) {
                String key = entry.getKey().getSimpleName().toString();
                Object value = entry.getValue().getValue();
                if (key.equals("adapter")) {
                    TypeElement typeOfClass =
                            elements.getTypeElement(((DeclaredType) value).asElement().toString());
                    String customAdapterPackage = getPackage(typeOfClass);
                    String customAdapterName = getClassName(typeOfClass, customAdapterPackage);
                    String simpleName = property.getSimpleName().toString();

                    // Check serialize and deserialize
                    boolean serializeChecker = false, deSerializeChecker = false;
                    List<? extends Element> valueElements = elements.getAllMembers(
                            (TypeElement) ((DeclaredType) value).asElement());
                    for (Element element : valueElements) {
                        if (element.getSimpleName().toString().equals("serialize")) {
                            serializeChecker = true;
                            List<? extends TypeMirror> thrownTypes =
                                    ((ExecutableElement) element).getThrownTypes();
                            for (TypeMirror thrownType : thrownTypes) {
                                TypeElement thrownClass =
                                        elements.getTypeElement(thrownType.toString());
                                String thrownClassPackage =
                                        elements.getPackageOf(thrownClass).toString();
                                String thrownClassName = thrownClass.getSimpleName().toString();
                                classBuilder.addSerializeException(
                                        new AbstractMap.SimpleEntry<>(thrownClassPackage,
                                                thrownClassName));
                            }
                        } else if (element.getSimpleName()
                                .toString()
                                .equals("createFromSerialized")) {
                            deSerializeChecker = true;
                            List<? extends TypeMirror> thrownTypes =
                                    ((ExecutableElement) element).getThrownTypes();
                            for (TypeMirror thrownType : thrownTypes) {
                                TypeElement thrownClass =
                                        elements.getTypeElement(thrownType.toString());
                                String thrownClassPackage =
                                        elements.getPackageOf(thrownClass).toString();
                                String thrownClassName = thrownClass.getSimpleName().toString();
                                classBuilder.addDeserializeException(
                                        new AbstractMap.SimpleEntry<>(thrownClassPackage,
                                                thrownClassName));
                            }
                        }
                    }

                    if (serializeChecker && deSerializeChecker) {

                    } else {
                        throw new NeonException(customAdapterName
                                + " does not implement valid NeonAdapter for custom property.");
                    }

                    // Build and return
                    //builder.setCustomAdapter(true);
                    isCustomProperty = true;
                    builder.setPropertyName(simpleName);
                    builder.setType(NeonProperty.Type.CUSTOM_ADAPTER);
                    builder.setDataType(
                            new AbstractMap.SimpleEntry<>(customAdapterPackage, customAdapterName));
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
