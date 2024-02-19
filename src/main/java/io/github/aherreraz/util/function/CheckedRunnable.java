package io.github.aherreraz.util.function;


@FunctionalInterface
public interface CheckedRunnable<E extends Exception> {
    void run() throws E;
}