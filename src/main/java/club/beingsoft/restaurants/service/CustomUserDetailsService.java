package club.beingsoft.restaurants.service;

import club.beingsoft.restaurants.model.User;
import club.beingsoft.restaurants.repository.jpa.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static club.beingsoft.restaurants.util.ValidationUtil.checkEntityNotNull;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserJpaRepository repository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = repository.findByName(userName);
        checkEntityNotNull("USER", user, null);
        String[] roles = user.getRoles().stream().map(Enum::name).toArray(String[]::new);
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getName())
                .password(user.getPassword())
                .roles(roles)
                .build();
    }
}
