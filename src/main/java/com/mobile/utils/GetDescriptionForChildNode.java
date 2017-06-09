package com.mobile.utils;

import com.annotation.Author;
import org.testng.IInvokedMethod;


public class GetDescriptionForChildNode {

    private IInvokedMethod methodName;
    private String description;
    private boolean methodNamePresent;
    private String descriptionMethodName;

    public GetDescriptionForChildNode(IInvokedMethod methodName, String description) {
        this.methodName = methodName;
        this.description = description;
    }

    public boolean isMethodNamePresent() {

        return methodNamePresent;
    }

    public String getDescriptionMethodName() {

        return descriptionMethodName;
    }

    public GetDescriptionForChildNode invoke() {

        if (description.isEmpty()) {
            descriptionMethodName = methodName.getTestMethod().getMethodName();

        } else {
            descriptionMethodName = description;
        }

        if (methodName.getTestMethod().getConstructorOrMethod()
                .getMethod().getAnnotation(Author.class) != null) {
            methodNamePresent = true;

        } else {
            methodNamePresent = false;
        }
        return this;
    }
}
