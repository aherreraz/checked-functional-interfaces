package io.github.aherreraz.util;

import io.github.aherreraz.util.function.*;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class LambdaExceptionUtil {
    public static <T, R, E extends Exception> Function<T, R> toFunction(CheckedFunction<T, R, E> function) throws E {
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

    public static <T, E extends Exception> Consumer<T> toConsumer(CheckedConsumer<T, E> consumer) throws E {
        return t -> {
            try {
                consumer.accept(t);
            } catch (Exception exception) {
                throwActualException(exception);
            }
        };
    }

    public static <E extends Exception> Runnable toRunnable(CheckedRunnable<E> runnable) throws E {
        return () -> {
            try {
                runnable.run();
            } catch (Exception exception) {
                throwActualException(exception);
            }
        };
    }

    public static <T, E extends Exception> Predicate<T> toPredicate(CheckedPredicate<T, E> predicate) throws E {
        return t -> {
            boolean result = false;
            try {
                result = predicate.test(t);
            } catch (Exception exception) {
                throwActualException(exception);
            }
            return result;
        };
    }

    public static <T, E extends Exception> Supplier<T> toSupplier(CheckedSupplier<T, E> supplier) throws E {
        return () -> {
            T result = null;
            try {
                result = supplier.get();
            } catch (Exception exception) {
                throwActualException(exception);
            }
            return result;
        };
    }

    @SuppressWarnings("unchecked")
    private static <E extends Exception> void throwActualException(Exception exception) throws E {
        throw (E) exception;
    }
}