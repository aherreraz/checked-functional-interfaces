package io.github.aherreraz.util.function;

/**
 * Represents an operation that takes no arguments and returns no result.
 * @param <E> the type of the exception that can be thrown
 */
@FunctionalInterface
public interface CheckedRunnable<E extends Exception> {
    /**
     * Performs this operation
     * @throws E the exception that can be thrown
     */
    void run() throws E;
}