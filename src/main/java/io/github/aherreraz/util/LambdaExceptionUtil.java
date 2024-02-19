package io.github.aherreraz.util;

import io.github.aherreraz.util.function.CheckedConsumer;
import io.github.aherreraz.util.function.CheckedFunction;

import java.util.function.Consumer;
import java.util.function.Function;

public class LambdaExceptionUtil {
    public static <T, R, E extends Exception> Function<T, R> rethrow(CheckedFunction<T, R, E> function) throws E {
        return t -> {
            R result = null;
            try {
                result = function.apply(t);
            } catch (Exception exception) {
                throwActualException(exception);
            }
            return result;
        };
    }

    public static <T, E extends Exception> Consumer<T> rethrow(CheckedConsumer<T, E> consumer) throws E {
        return t -> {
            try {
                consumer.accept(t);
            } catch (Exception exception) {
                throwActualException(exception);
            }
        };
    }

    @SuppressWarnings("unchecked")
    private static <E extends Exception> void throwActualException(Exception exception) throws E {
        throw (E) exception;
    }
}