package com.zing.zalo.neon2;

import com.zing.zalo.neon2.annotations.NeonObject;
import com.zing.zalo.neon2.internal.NeonGenerator;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;

public class NeonProcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
        elementUtils = processingEnvironment.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        Collection<? extends Element> annotatedElements =
            roundEnv.getElementsAnnotatedWith(NeonObject.class);
        List<TypeElement> types = ElementFilter.typesIn(annotatedElements);
        if (types.size() == 0)
            return false;
        for (TypeElement type : types) {
            NeonGenerator.processClass(filer, messager, elementUtils, type);
        }
        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> result = new HashSet<>();
        result.add(NeonObject.class.getName());
        return result;
    }
}
