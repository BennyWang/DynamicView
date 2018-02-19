package com.benny.library.dynamicview.compiler;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.ExecutableElement;

public class DynamicViewSetter {
    private String methodName;

    public DynamicViewSetter(String methodName) {
        this.methodName = methodName;
    }

    public void generateCode(CodeBlock.Builder codeBlock) {
        String propName = methodName.substring(3, 4).toLowerCase() + (methodName.length() > 4 ? methodName.substring(4) : "");
        codeBlock.add("case $S:\n", propName).indent().addStatement("target.$L(value)", methodName).addStatement("break").unindent();
    }

    public static boolean isValidSetterMethod(ExecutableElement executableElement) {
        String methodName = executableElement.getSimpleName().toString();
        return methodName.startsWith("set") && isMethodParametersOnlyString(executableElement);
    }

    private static boolean isMethodParametersOnlyString(ExecutableElement executableElement) {
        if (executableElement.getParameters().size() != 1) {
            return false;
        }

        TypeName typeName = TypeName.get(executableElement.getParameters().get(0).asType());
        if (!"java.lang.String".equals(typeName.toString())) {
            return false;
        }
        return true;
    }
}
