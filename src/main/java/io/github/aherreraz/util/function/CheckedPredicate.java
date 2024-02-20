package io.github.aherreraz.util.function;

/**
 * Represents a predicate (boolean-valued function) of one argument. Similar to a
 *  {@link java.util.function.Predicate Predicate}, but also works if a checked exception is declared
 * @param <T> the type of input to the predicate
 * @param <E> the type of the exception that can be thrown
 */
@FunctionalInterface
public interface CheckedPredicate<T, E extends Exception> {
    /**
     * Evaluates the predicate on the given argument
     * @param t the input argument
     * @return {@code true} if the input argument matches the predicate, otherwise {@code false}
     * @throws E the exception that can be thrown
     */
    boolean test(T t) throws E;
}