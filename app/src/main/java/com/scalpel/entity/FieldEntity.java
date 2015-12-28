package com.scalpel.entity;

/**
 * Author: Mrchen on 15/12/27.
 */
public class FieldEntity {
    public final String className;
    public final String fieldName;
    public boolean isStatic;

    public FieldEntity(String className, String fieldName) {
        this.className = className;
        this.fieldName = fieldName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldEntity entity = (FieldEntity) o;

        if (isStatic != entity.isStatic) return false;
        if (className != null ? !className.equals(entity.className) : entity.className != null)
            return false;
        return fieldName != null ? fieldName.equals(entity.fieldName) : entity.fieldName == null;

    }

    @Override
    public int hashCode() {
        int result = className != null ? className.hashCode() : 0;
        result = 31 * result + (fieldName != null ? fieldName.hashCode() : 0);
        result = 31 * result + (isStatic ? 1 : 0);
        return result;
    }
}