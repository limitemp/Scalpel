package com.scalpel.log;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author: Mrchen on 15/12/31.
 */
public class StrUtil {

    @SuppressWarnings({"unchecked", "RawUseOfParameterizedType"})
    public static String toString(Object object) {
        if (object == null) {
            return "null";
        }

        if (object instanceof Object[]) {
            return toStr((Object[])object);
        }

        if (object instanceof List<?>) {
            return toStr((List<Object>) object);
        }

        if (object instanceof Map<?, ?>) {
            return toStr((Map<Object, Object>) object);
        }

        return object.toString();
    }

    private static String toStr(Object[] objArray) {
        StringBuilder builder = new StringBuilder();
        builder.append("<Object[]> ");
        builder.append(Arrays.toString(objArray));
        return builder.toString();
    }

    private static String toStr(List<Object> objList) {
        StringBuilder builder = new StringBuilder();
        builder.append("<List> ");
        for (Object o : objList) {
            if (o == null) {
                builder.append("null");
            }else {
                builder.append(o.toString());
            }
            builder.append(" | ");
        }

        return builder.toString();
    }

    private static String toStr(Set<Object> objList) {
        StringBuilder builder = new StringBuilder();
        builder.append("<Set> ");
        for (Object o : objList) {
            if (o == null) {
                builder.append("null");
            }else {
                builder.append(o.toString());
            }
            builder.append(" | ");
        }
        return builder.toString();
    }

    private static String toStr(Map<Object, Object> objMap) {
        StringBuilder builder = new StringBuilder();
        builder.append("-------------<Map>: ")
                .append(objMap.size())
                .append(" -------\n");
        for (Map.Entry<Object, Object> entry : objMap.entrySet()) {
            builder.append(entry.getKey().toString());
            builder.append(" ==== ");
            builder.append(entry.getValue() == null ? "null" : entry.getValue().toString());
            builder.append("\n");
        }
        return builder.toString();
    }
}
