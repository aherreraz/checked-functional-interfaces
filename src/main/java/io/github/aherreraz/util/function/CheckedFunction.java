package io.github.aherreraz.util.function;

/**
 * Represents a function that accepts one argument and produces a result. Similar to a
 * {@link java.util.function.Function Function}, but also works if a checked exception is declared
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @param <E> the type of the exception that can be thrown
 */
@FunctionalInterface
public interface CheckedFunction<T, R, E extends Exception> {
    /**
     * Applies the function to the given argument
     *
     * @param t the function argument
     * @return the function result
     * @throws E the exception that can be thrown
     */
    R apply(T t) throws E;
}