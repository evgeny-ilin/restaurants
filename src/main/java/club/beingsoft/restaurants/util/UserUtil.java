package club.beingsoft.restaurants.util;

import club.beingsoft.restaurants.model.Role;
import club.beingsoft.restaurants.model.User;
import club.beingsoft.restaurants.to.UserTo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

public class UserUtil {

    private UserUtil() {
    }

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.USER);
    }

    public static UserTo asTo(User user) {
        return new UserTo(user.getId(), user.getName(), user.getEmail(), null, user.getRoles(), user.isEnabled(), user.getDeleteDate(), user.getDeleteUser());
    }

    public static User updateFromTo(User user, UserTo userTo) {
        if (userTo.getName() != null) {
            user.setName(userTo.getName());
        }

        if (userTo.getEmail() != null) {
            user.setEmail(userTo.getEmail().toLowerCase());
        }

        if (userTo.getPassword() != null) {
            user.setPassword(userTo.getPassword());
        }

        if (userTo.getRoles() != null) {
            user.setRoles(userTo.getRoles());
        }

        user.setEnabled(userTo.isEnabled());
        return user;
    }

    public static User prepareToSave(User user, PasswordEncoder passwordEncoder) {
        String password = user.getPassword();
        user.setPassword(StringUtils.hasText(password) ? passwordEncoder.encode(password) : password);
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}