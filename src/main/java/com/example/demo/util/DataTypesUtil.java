package com.example.demo.util;

public enum DataTypesUtil {;

    public static String sqlTypeStandardize(final String sqlType) {
        final String lowerCase = sqlType.toLowerCase();
        if (lowerCase.startsWith("int")) {
            return "INTEGER";
        }
        if (lowerCase.startsWith("varchar")) {
            return "CHARACTER VARYING";
        }
        throw new IllegalArgumentException("Unknown type: " + sqlType);
    }
}
