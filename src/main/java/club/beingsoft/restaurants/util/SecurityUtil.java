package club.beingsoft.restaurants.util;

import club.beingsoft.restaurants.model.Role;
import club.beingsoft.restaurants.model.User;

public class SecurityUtil {

    //private static int id = AbstractBaseEntity.START_SEQ;
    private static int id = 1;

    private static User user = new User(id, "user1", "user1@yandex.ru", "password", Role.USER);

    private SecurityUtil() {
    }

    public static int getAuthUserId() {
        return id;
    }

    public static void setAuthUserId(int id) {
        SecurityUtil.id = id;
    }

    public static User getAuthUser() {
        return user;
    }

    public static void setAuthUser(User user) {
        SecurityUtil.user = user;
    }

}