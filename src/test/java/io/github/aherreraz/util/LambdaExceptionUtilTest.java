package io.github.aherreraz.util;

import io.github.aherreraz.util.function.CheckedFunction;
import io.github.aherreraz.util.helper.Accessor;
import io.github.aherreraz.util.helper.TestObject;
import org.junit.jupiter.api.*;

import java.lang.reflect.Method;
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

        @Test
        @DisplayName("Should apply function")
        public void checkedFunction_success() throws NoSuchMethodException {
            CheckedFunction<String, Method, NoSuchMethodException> function = accessor::getGetterMethodForField;

            String actual = rethrow(function).apply("integerField").getName();
            String expected = "getIntegerField";
            assertEquals(actual, expected);
        }

        @Test
        @DisplayName("Should rethrow exception in function")
        public void checkedFunction_rethrowException() {
            CheckedFunction<String, Method, NoSuchMethodException> function = accessor::getGetterMethodForField;
            String string = "invalidField";

            assertThrows(NoSuchMethodException.class,
                    () -> rethrow(function).apply(string));
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
}