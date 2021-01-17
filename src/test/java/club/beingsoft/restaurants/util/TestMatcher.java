package club.beingsoft.restaurants.util;

import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

import static club.beingsoft.restaurants.util.TestUtil.readListFromJsonMvcResult;
import static org.assertj.core.api.Assertions.assertThat;

public class TestMatcher<T> {
    private final Class<T> clazz;
    private final BiConsumer<T, T> assertion;
    private final BiConsumer<Iterable<T>, Iterable<T>> iterableAssertion;
    private static String[] IGNORING_FIELDS;

    private TestMatcher(Class<T> clazz, BiConsumer<T, T> assertion, BiConsumer<Iterable<T>, Iterable<T>> iterableAssertion) {
        this.clazz = clazz;
        this.assertion = assertion;
        this.iterableAssertion = iterableAssertion;
    }

    public static <T> TestMatcher<T> usingAssertions(Class<T> clazz, BiConsumer<T, T> assertion, BiConsumer<Iterable<T>, Iterable<T>> iterableAssertion) {
        return new TestMatcher<>(clazz, assertion, iterableAssertion);
    }

    public static <T> TestMatcher<T> usingEqualsAssertions(Class<T> clazz) {
        return usingAssertions(clazz,
                (a, e) -> assertThat(a).isEqualTo(e),
                (a, e) -> assertThat(a).isEqualTo(e));
    }

    public static <T> TestMatcher<T> usingFieldsWithIgnoringAssertions(Class<T> clazz, String... fieldsToIgnore) {
        IGNORING_FIELDS = Arrays.copyOf(fieldsToIgnore, fieldsToIgnore.length);
        return usingAssertions(clazz,
                (a, e) -> assertThat(a).isEqualToIgnoringGivenFields(e, fieldsToIgnore),
                (a, e) -> assertThat(a).usingElementComparatorIgnoringFields(fieldsToIgnore).isEqualTo(e));
    }

    public void assertMatch(T actual, T expected) {
        assertion.accept(actual, expected);
    }

    @SafeVarargs
    public final void assertMatch(Iterable<T> actual, T... expected) {
        assertMatch(actual, expected);
    }

    public void assertMatch(Iterable<T> actual, Iterable<T> expected) {
        //iterableAssertion.accept(actual, expected);
        assertThat(actual).usingRecursiveComparison().ignoringFields(IGNORING_FIELDS).isEqualTo(expected);
    }

    public ResultMatcher contentJson(T expected) {
        return result -> assertMatch(TestUtil.readFromJsonMvcResult(result, clazz), expected);
    }

    @SafeVarargs
    public final ResultMatcher contentJson(T... expected) {
        return contentJson(List.of(expected));
    }

    public ResultMatcher contentJson(Iterable<T> expected) {
        return result -> assertMatch(readListFromJsonMvcResult(result, clazz), expected);
    }
}
