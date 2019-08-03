package com.blogEngine;

import javax.validation.constraints.Size;

public class Utilies {

    public static int getSizePropertyForFieldAnnotation(String field, String property, Class<?> reflectedClass)
            throws NoSuchFieldException {
        Size annotation = reflectedClass.getDeclaredField(field).getAnnotation(Size.class);
        return property.equals("max") ? annotation.max() : annotation.min();
    }

    public static String getStringWithLength(int maxTitleLength) {
        StringBuilder title = new StringBuilder();
        for (int i = 0; i < maxTitleLength; i++) {
            title.append("0");
        }

        return String.valueOf(title);
    }

}
