package io.github.aherreraz.util.function;

/**
 * Represents a supplier of results. Similar to a
 * {@link java.util.function.Supplier Supplier}, but also works if a checked exception is declared
 */
@FunctionalInterface
public interface CheckedSupplier<T, E extends Exception> {
    /**
     * Gets a result
     * @return a result
     * @throws E the exception that can be thrown
     */
    T get() throws E;
}