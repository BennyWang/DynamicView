package com.benny.library.dynamicview.compiler;

import com.benny.library.dynamicview.annotations.DynamicView;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

public class DynamicViewProcessor extends AbstractProcessor {
    private Elements elementUtils;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        elementUtils = processingEnvironment.getElementUtils();
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Map<TypeElement, DynamicViewClass> targetClassMap = new LinkedHashMap<>();

        for (Element element : roundEnvironment.getElementsAnnotatedWith(DynamicView.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                parsePropertySetter(element, targetClassMap);
            }
        }

        for (Map.Entry<TypeElement, DynamicViewClass> entry : targetClassMap.entrySet()) {
            TypeElement typeElement = entry.getKey();
            DynamicViewClass dynamicViewClass = entry.getValue();

            try {
                dynamicViewClass.brewJava().writeTo(filer);
            } catch (IOException e) {
                error(typeElement, "Unable to write view binder for type %s: %s", typeElement, e.getMessage());
            }
        }
        return true;
    }

    private void parsePropertySetter(Element element, Map<TypeElement, DynamicViewClass> targetClassMap) {
        DynamicViewClass dynamicViewClass = getOrCreateDynamicViewClass(targetClassMap, (TypeElement) element);
        for (Element member : element.getEnclosedElements()) {
            if (member.getKind() != ElementKind.METHOD || !DynamicViewSetter.isValidSetterMethod((ExecutableElement) member)) {
                continue;
            }
            dynamicViewClass.addSetter(new DynamicViewSetter(member.getSimpleName().toString()));
        }
    }

    private DynamicViewClass getOrCreateDynamicViewClass(Map<TypeElement, DynamicViewClass> targetClassMap, TypeElement enclosingElement) {
        DynamicViewClass dynamicViewClass = targetClassMap.get(enclosingElement);
        if (dynamicViewClass == null) {
            String targetType = enclosingElement.getQualifiedName().toString();
            String classPackage = getPackageName(enclosingElement);
            String className = getClassName(enclosingElement) + DynamicViewClass.CLASS_SUFFIX;

            dynamicViewClass = new DynamicViewClass(classPackage, className, enclosingElement);
            targetClassMap.put(enclosingElement, dynamicViewClass);
        }
        return dynamicViewClass;
    }

    private String getPackageName(TypeElement type) {
        return "com.benny.library.dynamicview.builder";
    }

    private static String getClassName(TypeElement type) {
        String fullName = type.getQualifiedName().toString();

        return fullName.substring(fullName.lastIndexOf(".") + 1);
    }



    private void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<String>() {{
            add(DynamicView.class.getCanonicalName());
        }};
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
