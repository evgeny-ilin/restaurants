package club.beingsoft.restaurants.service;

import club.beingsoft.restaurants.model.User;
import club.beingsoft.restaurants.repository.jpa.UserJpaRepository;
import club.beingsoft.restaurants.util.AuthorizedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static club.beingsoft.restaurants.util.ValidationUtil.checkEntityNotNull;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserJpaRepository repository;

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email);
        checkEntityNotNull("User with email = " + email, user, null);
        return new AuthorizedUser(user);
    }
}
