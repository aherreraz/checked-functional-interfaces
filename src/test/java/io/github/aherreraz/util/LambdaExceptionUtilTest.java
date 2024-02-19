package io.github.aherreraz.util;

import io.github.aherreraz.util.function.CheckedFunction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.aherreraz.util.LambdaExceptionUtil.rethrow;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LambdaExceptionUtilTest {
    @Nested
    @DisplayName("Checked Function")
    class CheckedFunctionTest {
        @Test
        @DisplayName("Should apply function")
        public void checkedFunction_success() {
            CheckedFunction<String, Integer, RuntimeException> function = Integer::parseInt;
            String string = "6";

            Integer actual = rethrow(function).apply(string);
            Integer expected = 6;
            assertEquals(actual, expected);
        }

        @Test
        @DisplayName("Should rethrow exception in function")
        public void checkedFunction_rethrowException() {
            CheckedFunction<String, Integer, RuntimeException> function = Integer::parseInt;
            String string = "notANumber";

            assertThrows(NumberFormatException.class,
                    () -> rethrow(function).apply(string));
        }

        @Test
        @DisplayName("Should apply function to a stream")
        public void checkedFunction_stream_success() {
            CheckedFunction<String, Integer, RuntimeException> function = Integer::parseInt;

            List<Integer> actual = Stream.of("1", "2", "3", "4", "5", "6")
                    .map(rethrow(function))
                    .collect(Collectors.toList());
            List<Integer> expected = List.of(1, 2, 3, 4, 5, 6);
            assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
        }

        @Test
        @DisplayName("Should rethrow exception in function applied to a stream")
        public void checkedFunction_chained_rethrowException() {
            CheckedFunction<String, Integer, RuntimeException> function = Integer::parseInt;

            assertThrows(NumberFormatException.class,
                    () -> Stream.of("one", "two", "three", "four", "five", "six")
                            .map(rethrow(function))
                            .collect(Collectors.toList()));
        }
    }
}