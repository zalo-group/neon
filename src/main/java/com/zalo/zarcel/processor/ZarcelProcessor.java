package com.zalo.zarcel.processor;

import com.zalo.zarcel.Exception.ZarcelException;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.*;

@SupportedAnnotationTypes("com.zalo.zarcel.processor.Zarcel")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
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
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


}
