package com.zalo.zarcel.processor;

import com.zing.zalo.e.ZarcelException;
import com.zing.zalo.annotations.Zarcel;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.*;

public class ZarcelProcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;
    private Elements elements;
    private Map<String, String> activitiesWithPackage;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
        elements = processingEnvironment.getElementUtils();
        activitiesWithPackage = new HashMap<>();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        Collection<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(Zarcel.class);
        List<TypeElement> types = ElementFilter.typesIn(annotatedElements);

        for (TypeElement type : types) {
            ZarcelProcessorGenerator generator = new ZarcelProcessorGenerator();
            try {
                generator.generate(type, elements, filer);
            } catch (IOException | ZarcelException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.toString());
            }
        }

        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        TreeSet<String> result = new TreeSet<>();
        result.add(Zarcel.class.getCanonicalName());
        return result;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


}
