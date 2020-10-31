package club.beingsoft.restaurants.util;

import club.beingsoft.restaurants.model.Role;
import club.beingsoft.restaurants.model.User;
import club.beingsoft.restaurants.util.exception.MustBeAdmin;

public class CheckAdmin {
    public static void check() {
        User user = SecurityUtil.getAuthUser();
        if (!user.getRoles().contains(Role.ADMIN))
            throw new MustBeAdmin("You don't have permission on this operation");
    }
}
