package io.github.aherreraz.util;

import io.github.aherreraz.util.function.CheckedConsumer;
import io.github.aherreraz.util.function.CheckedFunction;
import io.github.aherreraz.util.helper.Accessor;
import io.github.aherreraz.util.helper.TestObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.aherreraz.util.LambdaExceptionUtil.rethrow;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LambdaExceptionUtilTest {
    private Accessor accessor;

    @BeforeEach
    public void setUp() {
        TestObject object = TestObject.builder()
                .integerField(1)
                .integerField2(2)
                .integerField3(3)
                .build();
        accessor = Accessor.builder()
                .object(object)
                .build();
    }

    @Nested
    @DisplayName("Checked Function")
    class CheckedFunctionTest {
        @Test
        @DisplayName("Should apply function")
        public void checkedFunction_success() throws NoSuchMethodException {
            CheckedFunction<String, Method, NoSuchMethodException> function = accessor::getGetterMethodForField;
            String fieldName = "integerField";

            String actual = rethrow(function).apply(fieldName).getName();
            String expected = "getIntegerField";
            assertEquals(actual, expected);
        }

        @Test
        @DisplayName("Should rethrow exception in function")
        public void checkedFunction_rethrowException() {
            CheckedFunction<String, Method, NoSuchMethodException> function = accessor::getGetterMethodForField;
            String fieldName = "invalidField";

            assertThrows(NoSuchMethodException.class,
                    () -> rethrow(function).apply(fieldName));
        }

        @Test
        @DisplayName("Should apply function to a stream")
        public void checkedFunction_stream_success() throws Exception {
            CheckedFunction<String, Integer, Exception> function = accessor::getValueForField;

            List<Integer> actual = Stream.of("integerField", "integerField2", "integerField3")
                    .map(rethrow(function))
                    .collect(Collectors.toList());
            List<Integer> expected = List.of(1, 2, 3);
            assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
        }

        @Test
        @DisplayName("Should rethrow exception in function applied to a stream")
        public void checkedFunction_chained_rethrowException() {
            CheckedFunction<String, Method, NoSuchMethodException> function = accessor::getGetterMethodForField;

            assertThrows(NoSuchMethodException.class,
                    () -> Stream.of("invalidField", "invalidField2", "invalidField3")
                            .map(rethrow(function))
                            .collect(Collectors.toList()));
        }
    }

    @Nested
    @DisplayName("Checked Consumer")
    class CheckedConsumerTest {
        @Test
        @DisplayName("Should accept consumer")
        public void checkedConsumer_success() throws NoSuchMethodException {
            CheckedConsumer<String, NoSuchMethodException> consumer = accessor::printGetterMethodForField;
            String fieldName = "integerField";

            rethrow(consumer).accept(fieldName);
        }

        @Test
        @DisplayName("Should rethrow exception in consumer")
        public void checkedConsumer_rethrowException() {
            CheckedConsumer<String, NoSuchMethodException> consumer = accessor::printGetterMethodForField;
            String fieldName = "invalidField";

            assertThrows(NoSuchMethodException.class,
                    () -> rethrow(consumer).accept(fieldName));
        }

        @Test
        @DisplayName("Should accept consumer from a stream")
        public void checkedConsumer_stream_success() throws NoSuchMethodException {
            CheckedConsumer<String, NoSuchMethodException> consumer = accessor::printGetterMethodForField;

            Stream.of("integerField", "integerField2", "integerField3")
                    .forEach(rethrow(consumer));
        }

        @Test
        @DisplayName("Should rethrow exception in consumer accepted from a stream")
        public void checkedConsumer_stream_rethrowException() {
            CheckedConsumer<String, NoSuchMethodException> consumer = accessor::printGetterMethodForField;

            assertThrows(NoSuchMethodException.class,
                    () -> Stream.of("invalidField", "invalidField2", "invalidField3")
                            .forEach(rethrow(consumer)));
        }
    }
}