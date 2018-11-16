package com.zalo.zarcel;

import com.zing.zalo.annotations.Zarcel;
import com.zing.zalo.exception.ZarcelException;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes({
        "com.zing.zalo.annotations.Zarcel"
})
@SupportedOptions({
        "com.zing.zalo.annotations.Zarcel"
})
public class ZarcelProcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;
    private Elements elements;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
        elements = processingEnvironment.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        Collection<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(Zarcel.class);
        List<TypeElement> types = ElementFilter.typesIn(annotatedElements);
        if (types.size() == 0)
            return false;
        System.out.println("ZarcelProcessor: Rewrite number of files: " + types.size());
        for (TypeElement type : types) {
            ZarcelProcessorGenerator generator = new ZarcelProcessorGenerator();
            try {
                generator.generate(type, elements, filer, messager);
            } catch (IOException | ZarcelException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.toString());
            }
        }
        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }


}
