package io.github.aherreraz.util;

import io.github.aherreraz.util.function.*;
import io.github.aherreraz.util.helper.Accessor;
import io.github.aherreraz.util.helper.TestObject;
import io.github.aherreraz.util.helper.TestObjectWithPrivateFields;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.aherreraz.util.LambdaExceptionUtil.*;
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

            String actual = toFunction(function).apply(fieldName).getName();
            String expected = "getIntegerField";
            assertEquals(actual, expected);
        }

        @Test
        @DisplayName("Should rethrow exception in function")
        public void checkedFunction_rethrowException() {
            CheckedFunction<String, Method, NoSuchMethodException> function = accessor::getGetterMethodForField;
            String fieldName = "invalidField";

            assertThrows(NoSuchMethodException.class,
                    () -> toFunction(function).apply(fieldName));
        }

        @Test
        @DisplayName("Should apply function to a stream")
        public void checkedFunction_stream_success() throws ReflectiveOperationException {
            CheckedFunction<String, Integer, ReflectiveOperationException> function = accessor::getValueForField;

            List<Integer> actual = Stream.of("integerField", "integerField2", "integerField3")
                    .map(toFunction(function))
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
                            .map(toFunction(function))
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

            toConsumer(consumer).accept(fieldName);
        }

        @Test
        @DisplayName("Should rethrow exception in consumer")
        public void checkedConsumer_rethrowException() {
            CheckedConsumer<String, NoSuchMethodException> consumer = accessor::printGetterMethodForField;
            String fieldName = "invalidField";

            assertThrows(NoSuchMethodException.class,
                    () -> toConsumer(consumer).accept(fieldName));
        }

        @Test
        @DisplayName("Should accept consumer from a stream")
        public void checkedConsumer_stream_success() throws NoSuchMethodException {
            CheckedConsumer<String, NoSuchMethodException> consumer = accessor::printGetterMethodForField;

            Stream.of("integerField", "integerField2", "integerField3")
                    .forEach(toConsumer(consumer));
        }

        @Test
        @DisplayName("Should rethrow exception in consumer accepted from a stream")
        public void checkedConsumer_stream_rethrowException() {
            CheckedConsumer<String, NoSuchMethodException> consumer = accessor::printGetterMethodForField;

            assertThrows(NoSuchMethodException.class,
                    () -> Stream.of("invalidField", "invalidField2", "invalidField3")
                            .forEach(toConsumer(consumer)));
        }
    }

    @Nested
    @DisplayName("Checked Runnable")
    class CheckedRunnableTest {
        @Test
        @DisplayName("Should run runnable")
        public void checkedRunnable_success() throws ReflectiveOperationException {
            CheckedRunnable<ReflectiveOperationException> runnable = accessor::printDeclaredFieldValues;

            toRunnable(runnable).run();
        }

        @Test
        @DisplayName("Should rethrow exception in runnable")
        public void checkedRunnable_rethrowException() {
            TestObjectWithPrivateFields object = TestObjectWithPrivateFields.builder()
                    .integerField(1)
                    .integerField2(2)
                    .privateIntegerField(3).build();
            accessor.setObject(object);
            CheckedRunnable<ReflectiveOperationException> runnable = accessor::printDeclaredFieldValues;

            assertThrows(NoSuchMethodException.class,
                    () -> toRunnable(runnable).run());
        }
    }

    @Nested
    @DisplayName("Checked Predicate")
    class CheckedPredicateTest {
        @Test
        @DisplayName("Should test predicate")
        public void checkedPredicate_success() throws ReflectiveOperationException {
            CheckedPredicate<String, ReflectiveOperationException> predicate = accessor::doesFieldHaveValue;

            boolean actual = toPredicate(predicate).test("integerField");
            boolean expected = true;

            assertEquals(actual, expected);
        }

        @Test
        @DisplayName("Should rethrow exception in predicate")
        public void checkedPredicate_rethrowException() {
            TestObjectWithPrivateFields object = TestObjectWithPrivateFields.builder()
                    .integerField(1)
                    .integerField2(2)
                    .privateIntegerField(3)
                    .build();
            accessor.setObject(object);
            CheckedPredicate<String, ReflectiveOperationException> predicate = accessor::doesFieldHaveValue;

            assertThrows(NoSuchMethodException.class,
                    () -> toPredicate(predicate).test("privateIntegerField"));
        }

        @Test
        @DisplayName("Should test predicate on a stream")
        public void checkedPredicate_stream_success() throws ReflectiveOperationException {
            TestObject object = TestObject.builder()
                    .integerField(1)
                    .integerField3(3)
                    .build();
            accessor.setObject(object);
            CheckedPredicate<String, ReflectiveOperationException> predicate = accessor::doesFieldHaveValue;

            List<String> actual = Stream.of("integerField", "integerField2", "integerField3")
                    .filter(toPredicate(predicate))
                    .collect(Collectors.toList());
            List<String> expected = List.of("integerField", "integerField3");
            assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
        }

        @Test
        @DisplayName("Should rethrow exception in predicate tested on a stream")
        public void checkedPredicate_stream_rethrowException() {
            TestObjectWithPrivateFields object = TestObjectWithPrivateFields.builder()
                    .integerField(1)
                    .integerField3(3)
                    .privateIntegerField(4)
                    .build();
            accessor.setObject(object);
            CheckedPredicate<String, ReflectiveOperationException> predicate = accessor::doesFieldHaveValue;

            assertThrows(NoSuchMethodException.class,
                    () -> Stream.of("integerField", "integerField2", "integerField3", "privateIntegerField")
                            .filter(toPredicate(predicate))
                            .collect(Collectors.toList()));
        }
    }

    @Nested
    @DisplayName("Checked Supplier")
    class CheckedSupplierTest {
        @Test
        @DisplayName("Should get value from supplier")
        public void checkedSupplier_success() throws ReflectiveOperationException {
            CheckedSupplier<List<Object>, ReflectiveOperationException> supplier = accessor::getDeclaredFieldValues;

            List<Object> actual = toSupplier(supplier).get();
            List<Object> expected = List.of(1, 2, 3);
            assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
        }

        @Test
        @DisplayName("Should rethrow exception in supplier")
        public void checkedRunnable_rethrowException() {
            TestObjectWithPrivateFields object = TestObjectWithPrivateFields.builder()
                    .integerField(1)
                    .integerField2(2)
                    .privateIntegerField(3).build();
            accessor.setObject(object);
            CheckedSupplier<List<Object>, ReflectiveOperationException> supplier = accessor::getDeclaredFieldValues;

            assertThrows(NoSuchMethodException.class,
                    () -> toSupplier(supplier).get());
        }
    }
}