package club.beingsoft.restaurants.util;

import club.beingsoft.restaurants.model.Role;
import club.beingsoft.restaurants.model.User;
import club.beingsoft.restaurants.util.exception.PermissionException;

public class CheckPermissions {
    public static void checkAdmin() {
        User user = SecurityUtil.getAuthUser();
        if (!user.getRoles().contains(Role.ADMIN))
            throw new PermissionException("You don't have permission on this operation");
    }
}
