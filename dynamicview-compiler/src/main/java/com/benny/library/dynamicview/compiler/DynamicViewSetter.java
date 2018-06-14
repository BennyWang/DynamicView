package com.benny.library.dynamicview.compiler;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

public class DynamicViewSetter {
    private ExecutableElement element;
    private String methodName;

    public DynamicViewSetter(ExecutableElement element) {
        this.element = element;
        this.methodName = element.getSimpleName().toString();
    }

    public void generateCode(CodeBlock.Builder codeBlock) {
        String propName = methodName.substring(3, 4).toLowerCase() + (methodName.length() > 4 ? methodName.substring(4) : "");
        codeBlock.add("case $S:\n", propName).indent().addStatement("target.$L(to$L(value))", methodName, getReturnType()).addStatement("break").unindent();
    }

    private String getReturnType() {
        TypeMirror type = element.getParameters().get(0).asType();
        String className = TypeName.get(type).toString();

        if (type.getKind().isPrimitive()) {
            return className.substring(0, 1).toUpperCase() + className.substring(1);
        }
        return className.substring(className.lastIndexOf(".") + 1);
    }

    public static boolean isValidSetterMethod(ExecutableElement executableElement) {
        String methodName = executableElement.getSimpleName().toString();
        return methodName.startsWith("set") && isMethodParametersOnlyString(executableElement);
    }

    private static boolean isMethodParametersOnlyString(ExecutableElement executableElement) {
        if (executableElement.getParameters().size() != 1) {
            return false;
        }

        TypeMirror type = executableElement.getParameters().get(0).asType();
        return type.getKind().isPrimitive()
                || "java.lang.String".equals(TypeName.get(type).toString())
                || "org.json.JSONObject".equals(TypeName.get(type).toString())
                || "org.json.JSONArray".equals(TypeName.get(type).toString());
    }


}
