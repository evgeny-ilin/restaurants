package club.beingsoft.restaurants.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class FieldUtil {
    private static final LocalDateTime END_DATE = LocalDateTime.parse("2999-12-31T23:59:59");

    public static LocalDateTime getSysdate() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    public static LocalDateTime getEndDate() {
        return END_DATE;
    }
}
