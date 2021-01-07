package club.beingsoft.restaurants.service;

import club.beingsoft.restaurants.model.User;
import club.beingsoft.restaurants.repository.jpa.UserJpaRepository;
import club.beingsoft.restaurants.to.UserTo;
import club.beingsoft.restaurants.util.AuthorizedUser;
import club.beingsoft.restaurants.util.SecurityUtil;
import club.beingsoft.restaurants.util.UserUtil;
import club.beingsoft.restaurants.util.exception.NotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

import static club.beingsoft.restaurants.util.UserUtil.asTo;
import static club.beingsoft.restaurants.util.UserUtil.prepareToSave;
import static club.beingsoft.restaurants.util.ValidationUtil.getFoundException;

@Service("userService")
public class UserService implements UserDetailsService {

    private final UserJpaRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserJpaRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @CacheEvict(value = "users")
    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        return prepareAndSave(user);
    }

    @CacheEvict(value = "users")
    public void delete(int id) {
        User user = repository.findById(id).orElseThrow(() -> getFoundException(User.class, id));
        user.delete(SecurityUtil.getAuthUser());
        repository.save(user);
    }

    @Cacheable("users")
    public UserTo get(int id) {
        return asTo(repository.findById(id).orElseThrow(() -> getFoundException(User.class, id)));
    }

    @Cacheable("users")
    public UserTo getByEmail(String email) {
        Assert.notNull(email, "email must not be null");
        return asTo(repository.findByEmail(email).orElseThrow(() -> new NotFoundException("Not found user with email = " + email)));
    }

    public List<UserTo> getAll() {
        return repository.findAll().stream().map(UserUtil::asTo).collect(Collectors.toList());
    }

    @CacheEvict(value = "users")
    public void update(UserTo userTo) {
        User user = repository.findById(userTo.id()).orElseThrow(() -> new NotFoundException(User.class, userTo.id()));
        user.setId(userTo.id());
        prepareAndSave(UserUtil.updateFromTo(user, userTo));
    }

    @CacheEvict(value = "users")
    public void enable(int id, boolean enabled) {
        User user = repository.findById(id).orElseThrow(() -> getFoundException(User.class, id));
        user.setEnabled(enabled);
        repository.save(user);
    }

    public AuthorizedUser loadUserByUsername(String email) throws NotFoundException {
        return new AuthorizedUser(repository.findByEmail(email).orElseThrow(() -> new NotFoundException("Not found user with email = " + email)));

    }

    private User prepareAndSave(User user) {
        return repository.save(prepareToSave(user, passwordEncoder));
    }
}