package com.scalpel.entity;

/**
 * Author: Mrchen on 15/12/27.
 */
public class MethodEntity {
    public final String className;
    public String methodName;

    public boolean hookConstructor;
    public boolean showStack;
    public boolean hijack;
    public Object result;

    public MethodEntity(String className) {
        this.className = className;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodEntity entity = (MethodEntity) o;

        if (hookConstructor != entity.hookConstructor) return false;
        if (showStack != entity.showStack) return false;
        if (hijack != entity.hijack) return false;
        if (className != null ? !className.equals(entity.className) : entity.className != null)
            return false;
        if (methodName != null ? !methodName.equals(entity.methodName) : entity.methodName != null)
            return false;
        return result != null ? result.equals(entity.result) : entity.result == null;

    }

    @Override
    public int hashCode() {
        int result1 = className != null ? className.hashCode() : 0;
        result1 = 31 * result1 + (methodName != null ? methodName.hashCode() : 0);
        result1 = 31 * result1 + (hookConstructor ? 1 : 0);
        result1 = 31 * result1 + (showStack ? 1 : 0);
        result1 = 31 * result1 + (hijack ? 1 : 0);
        result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
        return result1;
    }

    @Override
    public String toString() {
        String resultString = result == null ? "null" : result.toString();
        return "MethodEntity{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", hookConstructor=" + hookConstructor +
                ", showStack=" + showStack +
                ", hijack=" + hijack +
                ", result=" + resultString +
                '}';
    }
}
