package com.scalpel.entity;

/**
 * Author: Mrchen on 15/12/27.
 */
public class MethodEntity {
    public final String className;
    public String methodName;

    public boolean hookConstructor;

    public MethodEntity(String className) {
        this.className = className;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodEntity entity = (MethodEntity) o;

        if (hookConstructor != entity.hookConstructor) return false;
        if (className != null ? !className.equals(entity.className) : entity.className != null)
            return false;
        return methodName != null ? methodName.equals(entity.methodName) : entity.methodName == null;
    }

    @Override
    public int hashCode() {
        int result = className != null ? className.hashCode() : 0;
        result = 31 * result + (methodName != null ? methodName.hashCode() : 0);
        result = 31 * result + (hookConstructor ? 1 : 0);
        return result;
    }
}
