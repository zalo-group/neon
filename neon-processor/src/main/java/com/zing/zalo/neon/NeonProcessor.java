package com.zing.zalo.neon;

import com.zing.zalo.neon.annotations.Neon;
import com.zing.zalo.neon.exception.NeonException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@SupportedAnnotationTypes({
        "com.zing.zalo.neon.annotations.Neon"
})
@SupportedOptions({
        "com.zing.zalo.neon.annotations.Neon"
})
public class NeonProcessor extends AbstractProcessor {

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
        Collection<? extends Element> annotatedElements =
                roundEnv.getElementsAnnotatedWith(Neon.class);
        List<TypeElement> types = ElementFilter.typesIn(annotatedElements);
        if (types.size() == 0) {
            return false;
        }
        System.out.println("NeonProcessor: Rewrite number of files: " + types.size());
        for (TypeElement type : types) {
            NeonProcessorGenerator generator = new NeonProcessorGenerator();
            try {
                generator.generate(type, elements, filer, messager);
            } catch (IOException | NeonException e) {
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
