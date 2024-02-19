package io.github.aherreraz.util.helper;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RequiredArgsConstructor
@Builder
public class Accessor {
    private final Object object;

    public Method getGetterMethodForField(String fieldName) throws NoSuchMethodException {
        Class<?> clazz = object.getClass();
        return clazz.getMethod("get" + capitalize(fieldName));
    }

    @SuppressWarnings("unchecked")
    public <T> T getValueForField(String fieldName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getterMethod = getGetterMethodForField(fieldName);
        return (T) getterMethod.invoke(object);
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty())
            return str;

        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}