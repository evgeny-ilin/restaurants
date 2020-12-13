package club.beingsoft.restaurants.service;

import club.beingsoft.restaurants.repository.jpa.UserJpaRepository;
import club.beingsoft.restaurants.util.AuthorizedUser;
import club.beingsoft.restaurants.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserJpaRepository repository;

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws NotFoundException {
        return new AuthorizedUser(repository.findByEmail(email).orElseThrow(() -> new NotFoundException("Not founf user with email = " + email)));
    }
}
