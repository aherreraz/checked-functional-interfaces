package io.github.aherreraz.util;

import io.github.aherreraz.util.function.CheckedFunction;

import java.util.function.Function;

public class LambdaExceptionUtil {
    public static <T, R, E extends Exception> Function<T, R> rethrow(CheckedFunction<T, R, E> function) throws E {
        return t -> {
            try {
                return function.apply(t);
            } catch (Exception exception) {
                throwActualException(exception);
                return null;
            }
        };
    }

    @SuppressWarnings("unchecked")
    private static <E extends Exception> void throwActualException(Exception exception) throws E {
        throw (E) exception;
    }
}