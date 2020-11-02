package club.beingsoft.restaurants;

import club.beingsoft.restaurants.model.Role;
import club.beingsoft.restaurants.model.User;

public class UserTestData {
    public static final User USER_ADMIN = new User(100000, "ADMIN_TEST", "admin@mail.ru", "1", Role.ADMIN);
    public static final User USER_USER = new User(100001, "USER_TEST", "user@mail.ru", "1", Role.USER);
}
