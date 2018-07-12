package com.benny.library.dynamicview.compiler;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import java.util.HashSet;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

public class DynamicViewSetter {
    private ExecutableElement element;
    private String methodName;
    private HashSet<String> supportedConverts = new HashSet<>();

    public DynamicViewSetter(ExecutableElement element) {
        this.element = element;
        this.methodName = element.getSimpleName().toString();

        supportedConverts.add("Int");
        supportedConverts.add("Float");
        supportedConverts.add("Long");
        supportedConverts.add("Double");
        supportedConverts.add("Boolean");
        supportedConverts.add("String");
        supportedConverts.add("JSONObject");
        supportedConverts.add("JSONArray");
    }

    public void generateCode(CodeBlock.Builder codeBlock) {
        String propName = methodName.substring(3, 4).toLowerCase() + (methodName.length() > 4 ? methodName.substring(4) : "");
        String parameterType = getParameterType();
        String convertExpression;
        if (supportedConverts.contains(parameterType)) {
            codeBlock.add("case $S:\n", propName).indent().addStatement("target.$L(to$L(value))", methodName, parameterType).addStatement("break").unindent();
        }
        else {
            TypeMirror type = element.getParameters().get(0).asType();
            codeBlock.add("case $S:\n", propName).indent().addStatement("target.$L(($N)value)", methodName, TypeName.get(type).toString()).addStatement("break").unindent();
        }
    }

    private String getParameterType() {
        TypeMirror type = element.getParameters().get(0).asType();
        String className = TypeName.get(type).toString();

        if (type.getKind().isPrimitive()) {
            return className.substring(0, 1).toUpperCase() + className.substring(1);
        }
        return className.substring(className.lastIndexOf(".") + 1);
    }

    public static boolean isValidSetterMethod(ExecutableElement executableElement) {
        String methodName = executableElement.getSimpleName().toString();

        return executableElement.getModifiers().contains(Modifier.PUBLIC) && methodName.startsWith("set") && isMethodParametersOnlyString(executableElement);
    }

    private static boolean isMethodParametersOnlyString(ExecutableElement executableElement) {
        if (executableElement.getParameters().size() != 1) {
            return false;
        }

        TypeMirror type = executableElement.getParameters().get(0).asType();
        return !TypeName.get(type).toString().contains("com.benny.library.dynamicview");
    }


}
