package club.beingsoft.restaurants;

import club.beingsoft.restaurants.model.Role;
import club.beingsoft.restaurants.model.User;
import club.beingsoft.restaurants.to.UserTo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static club.beingsoft.restaurants.util.UserUtil.asTo;

public class UserTestData {
    public static final User ADMIN = new User(100000, "ADMIN_TEST", "admin@mail.ru", "1", Role.ADMIN);
    public static final User USER = new User(100001, "USER_TEST", "user@mail.ru", "1", Role.USER);
    public static final User USER_NEW = new User(100016, "USER_NEW", "user_new@mail.ru", "1", Role.USER);
    public static final User USER_UPDATED = new User(100001, "USER_UPDATED", "user_updated@mail.ru", "2", Role.USER);
    public static final UserTo ADMIN_TO = asTo(ADMIN);
    public static final UserTo USER_TO = asTo(USER);
    public static final UserTo USER_TO_NEW = asTo(USER_NEW);
    public static final UserTo USER_TO_UPDATED = asTo(USER_UPDATED);
    public static final UserTo USER_TO_DISABLED = asTo(USER);
    public static final List<UserTo> USERS_TOS_LIST = new ArrayList<>(Arrays.asList(asTo(ADMIN), asTo(USER)));
    public static final String NOT_FOUND_EMAIL = "not_found@email.com";

    static {
        USER_TO_DISABLED.setEnabled(false);
    }

    public static UserTo getUserToNew() {
        UserTo userTo = asTo(new User(100015, "USER_NEW", "user_new@mail.ru", "1", Role.USER));
        userTo.setPassword("1");
        return userTo;
    }
}
