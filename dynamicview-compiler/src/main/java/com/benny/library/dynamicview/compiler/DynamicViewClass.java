package com.benny.library.dynamicview.compiler;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

public class DynamicViewClass {
    public static final String CLASS_SUFFIX = "$$Builder";
    public static final String BUILDER_CLASS = "com.benny.library.dynamicview.view.DynamicViewBuilder";

    private final String classPackage;
    private final String className;
    private final String targetClass;

    private List<DynamicViewSetter> setters = new ArrayList<>();

    public DynamicViewClass(String classPackage, String className, String targetClass) {
        this.classPackage = classPackage;
        this.className = className;
        this.targetClass = targetClass;
    }

    public void addSetter(DynamicViewSetter setter) {
        setters.add(setter);
    }

    public JavaFile brewJava() {
        TypeSpec.Builder result = TypeSpec.classBuilder(className)
                .addModifiers(PUBLIC)
                .superclass(TypeVariableName.get(BUILDER_CLASS));

        createTargetField(result);
        createConstructor(result);
        createSetMethod(result);
        return JavaFile.builder(classPackage, result.build())
                .addFileComment("Generated code from DynamicView. Do not modify!")
                .build();
    }

    private void createTargetField(TypeSpec.Builder result) {
        FieldSpec target = FieldSpec.builder(TypeVariableName.get(targetClass), "target")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build();
        result.addField(target);
    }

    private void createConstructor(TypeSpec.Builder result) {
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeVariableName.get("android.content.Context"), "context")
                .addStatement("this.target = new $N(context)", targetClass)
                .addStatement("this.view = this.target")
                .build();
        result.addMethod(constructor);
    }

    private void createSetMethod(TypeSpec.Builder result) {
        MethodSpec.Builder setMethod = MethodSpec.methodBuilder("setProperty")
                .addAnnotation(Override.class)
                .addModifiers(PUBLIC)
                .addParameter(String.class, "key", FINAL)
                .addParameter(String.class, "value", FINAL)
                .returns(boolean.class);

        setMethod.addStatement("if (super.setProperty(key, value)) return true");

        CodeBlock.Builder switchBlock = CodeBlock.builder().beginControlFlow("switch(key)");
        for (DynamicViewSetter setter : setters) {
            setter.generateCode(switchBlock);
        }
        switchBlock.endControlFlow();
        setMethod.addCode(switchBlock.build());
        setMethod.addStatement("return true");
        result.addMethod(setMethod.build());
    }
}
