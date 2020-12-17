package club.beingsoft.restaurants.util;

import static org.assertj.core.api.Assertions.assertThat;

public class UserMatcher {
    public static <T1, T2> void assertUsers(T1 actual, T2 expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("roles", "deleteDate", "deleteUser", "editDate", "registered").isEqualTo(expected);
    }
}
