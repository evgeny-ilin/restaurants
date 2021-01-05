package club.beingsoft.restaurants;

import club.beingsoft.restaurants.model.Role;
import club.beingsoft.restaurants.model.User;
import club.beingsoft.restaurants.to.UserTo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static club.beingsoft.restaurants.model.AbstractBaseEntity.START_SEQ;
import static club.beingsoft.restaurants.util.UserUtil.asTo;

public class UserTestData {
    public static final User ADMIN = new User(START_SEQ, "ADMIN_TEST", "admin@mail.ru", "1", Role.ADMIN);
    public static final User USER_1 = new User(START_SEQ + 1, "USER_TEST", "user@mail.ru", "1", Role.USER);
    public static final User USER_2 = new User(START_SEQ + 16, "USER_TEST-2", "user2@mail.ru", "2", Role.USER);
    public static final User USER_UPDATED = new User(USER_2.id(), "USER_UPDATED", "user_updated@mail.ru", "2", Role.USER);
    public static final User DELETED_USER = new User(START_SEQ + 17, "USER_DELETED", "user_deleted@mail.ru", "2", Role.USER);
    public static final User USER_NEW = new User(START_SEQ + 18, "USER_NEW", "user_new@mail.ru", "1", Role.USER);

    public static final UserTo ADMIN_TO = asTo(ADMIN);
    public static final UserTo USER_TO = asTo(USER_1);
    public static final UserTo USER_TO_NEW = asTo(USER_NEW);
    public static final UserTo USER_TO_UPDATED = asTo(USER_UPDATED);
    public static final UserTo ADMIN_TO_DISABLED = asTo(ADMIN);
    public static final List<UserTo> USERS_TOS_LIST = new ArrayList<>(Arrays.asList(asTo(ADMIN), asTo(USER_1), asTo(USER_2)));
    public static final String NOT_FOUND_EMAIL = "not_found@email.com";

    static {
        ADMIN_TO_DISABLED.setEnabled(false);
        DELETED_USER.delete(ADMIN);
        USERS_TOS_LIST.add(asTo(DELETED_USER));
    }

    public static UserTo getUserToNew() {
        UserTo userTo = asTo(new User(START_SEQ + 17, "USER_NEW", "user_new@mail.ru", "1", Role.USER));
        userTo.setPassword("1");
        return userTo;
    }
}
