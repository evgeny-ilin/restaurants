package club.beingsoft.restaurants.util;

import club.beingsoft.restaurants.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static java.util.Objects.requireNonNull;

public class SecurityUtil {

    private SecurityUtil() {
    }

    public static User getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        AuthorizedUser authorizedUser = (principal instanceof AuthorizedUser) ? (AuthorizedUser) principal : null;
        requireNonNull(authorizedUser, "No authorized user found");
        return authorizedUser.getUser();
    }
}