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
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

public class DynamicViewClass {
    public static final String CLASS_SUFFIX = "$$Builder";
    public static final String BUILDER_CLASS = "com.benny.library.dynamicview.view.DynamicViewBuilder";

    private final String classPackage;
    private final String className;
    private final TypeElement targetClass;

    private List<DynamicViewSetter> setters = new ArrayList<>();

    public DynamicViewClass(String classPackage, String className, TypeElement targetClass) {
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

        implementsInterfaces(result);
        createTargetField(result);
        createViewMethod(result);
        createSetMethod(result);
        return JavaFile.builder(classPackage, result.build())
                .addFileComment("Generated code from DynamicView. Do not modify!")
                .build();
    }

    private void implementsInterfaces(TypeSpec.Builder result) {
        List<? extends TypeMirror> interfaces = targetClass.getInterfaces();
        for (TypeMirror type : interfaces) {
            String typeName = type.toString();
            if (typeName.endsWith(".ViewType.View")) {
                implementsView(typeName, result);
            }
            else if (typeName.endsWith(".ViewType.GroupView")) {
                implementsGroupView(typeName, result);
            }
            else if (typeName.endsWith(".ViewType.AdapterView")) {
                implementsAdapterView(typeName, result);
            }
        }
    }

    private void implementsView(String typeName, TypeSpec.Builder result) {
        result.addSuperinterface(TypeVariableName.get(typeName));
    }

    private void implementsGroupView(String typeName, TypeSpec.Builder result) {
        result.addSuperinterface(TypeVariableName.get(typeName));
    }

    private void implementsAdapterView(String typeName, TypeSpec.Builder result) {
        result.addSuperinterface(TypeVariableName.get(typeName));
        MethodSpec.Builder setViewCreatorMethod = MethodSpec.methodBuilder("setInflater")
                .addModifiers(PUBLIC)
                .addParameter(TypeVariableName.get("com.benny.library.dynamicview.view.ViewInflater"), "inflater");
        result.addMethod(setViewCreatorMethod.build());

        MethodSpec.Builder setDataSourceMethod = MethodSpec.methodBuilder("setDataSource")
                .addModifiers(PUBLIC)
                .addParameter(TypeVariableName.get("org.json.JSONArray"), "dataSource");
        result.addMethod(setDataSourceMethod.build());
    }

    private void createTargetField(TypeSpec.Builder result) {
        String targetClassName = targetClass.getQualifiedName().toString();
        FieldSpec target = FieldSpec.builder(TypeVariableName.get(targetClassName), "target")
                .addModifiers(Modifier.PRIVATE)
                .build();
        result.addField(target);
    }

    private void createViewMethod(TypeSpec.Builder result) {
        String targetClassName = targetClass.getQualifiedName().toString();
        MethodSpec.Builder constructor = MethodSpec.methodBuilder("createView")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeVariableName.get("android.content.Context"), "context")
                //.addStatement("long tick = System.currentTimeMillis()", targetClassName)
                .addStatement("this.target = new $N(context)", targetClassName)
                .addStatement("this.view = this.target");
                //.addStatement("android.util.Log.i(\"DynamicViewEngine\", \"create view of $L cost \" + (System.currentTimeMillis() - tick))", className);
        result.addMethod(constructor.build());
    }

    private void createSetMethod(TypeSpec.Builder result) {
        MethodSpec.Builder setMethod = MethodSpec.methodBuilder("setProperty")
                .addAnnotation(Override.class)
                .addModifiers(PUBLIC)
                .addParameter(String.class, "key", FINAL)
                .addParameter(Object.class, "value", FINAL)
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
