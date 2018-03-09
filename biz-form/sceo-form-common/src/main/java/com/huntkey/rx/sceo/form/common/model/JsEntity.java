package com.huntkey.rx.sceo.form.common.model;

import java.util.Map;

public class JsEntity {
    private String className;
    private String methodName;
    private Map<String, Object> paramSet;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Map<String, Object> getParamSet(){
        return paramSet;
    }

    public void setParamSet(Map<String, Object> paramSet) {
        this.paramSet = paramSet;
    }
}

