package io.github.aherreraz.util.function;

/**
 * Represents an operation that accepts a single input argument and returns no result. Similar to a
 * {@link java.util.function.Consumer Consumer}, but also works if a checked exception is declared
 * @param <T> the type of the input to the operation
 * @param <E> the type of the exception that can be thrown
 */
@FunctionalInterface
public interface CheckedConsumer<T, E extends Exception> {
    /**
     * Performs this operation to the given argument
     * @param t the input argument
     * @throws E the exception that can be thrown
     */
    void accept(T t) throws E;
}