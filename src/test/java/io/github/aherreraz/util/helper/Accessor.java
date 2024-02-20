package io.github.aherreraz.util.helper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import static io.github.aherreraz.util.LambdaExceptionUtil.toFunction;

@Data
@Builder
@AllArgsConstructor
public class Accessor {
    private Object object;

    public Method getGetterMethodForField(String fieldName) throws NoSuchMethodException {
        Class<?> clazz = object.getClass();
        return clazz.getMethod("get" + capitalize(fieldName));
    }

    public void printGetterMethodForField(String fieldName) throws NoSuchMethodException {
        Method getterMethod = getGetterMethodForField(fieldName);
        System.out.println(getterMethod);
    }

    @SuppressWarnings("unchecked")
    public <T> T getValueForField(String fieldName) throws ReflectiveOperationException {
        Method getterMethod = getGetterMethodForField(fieldName);
        return (T) getterMethod.invoke(object);
    }

    public void printDeclaredFieldValues() throws ReflectiveOperationException {
        Class<?> clazz = object.getClass();
        Stream.of(clazz.getDeclaredFields())
                .map(toFunction(field -> getValueForField(field.getName())))
                .forEach(System.out::println);
    }

    public boolean doesFieldHaveValue(String fieldName) throws ReflectiveOperationException {
        return getValueForField(fieldName) != null;
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty())
            return str;

        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}