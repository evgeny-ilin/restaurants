package club.beingsoft.restaurants.service;

import club.beingsoft.restaurants.model.User;
import club.beingsoft.restaurants.repository.jpa.UserJpaRepository;
import club.beingsoft.restaurants.to.UserTo;
import club.beingsoft.restaurants.util.AuthorizedUser;
import club.beingsoft.restaurants.util.UserUtil;
import club.beingsoft.restaurants.util.exception.NotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

import static club.beingsoft.restaurants.util.UserUtil.asTo;
import static club.beingsoft.restaurants.util.UserUtil.prepareToSave;

@Service("userService")
public class UserService implements UserDetailsService {

    private final UserJpaRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserJpaRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        return prepareAndSave(user);
    }

    public void delete(int id) {
        User user = repository.findById(id).orElseThrow(() -> new NotFoundException(User.class, id));
        user.delete();
        repository.save(user);
    }

    public UserTo get(int id) {
        return asTo(repository.findById(id).orElseThrow(() -> new NotFoundException(User.class, id)));
    }

    public UserTo getByEmail(String email) {
        Assert.notNull(email, "email must not be null");
        return asTo(repository.findByEmail(email).orElseThrow(() -> new NotFoundException("Not found user with email = " + email)));
    }

    public List<UserTo> getAll() {
        return repository.findAll().stream().map(UserUtil::asTo).collect(Collectors.toList());
    }

    @Transactional
    public void update(UserTo userTo) {
        User user = repository.findById(userTo.id()).orElseThrow(() -> new NotFoundException(User.class, userTo.id()));
        user.setId(userTo.id());
        prepareAndSave(UserUtil.updateFromTo(user, userTo));
    }

    @Transactional
    public void enable(int id, boolean enabled) {
        User user = repository.findById(id).orElseThrow(() -> new NotFoundException(User.class, id));
        user.setEnabled(enabled);
        repository.save(user);
    }

    public AuthorizedUser loadUserByUsername(String email) throws NotFoundException {
        return new AuthorizedUser(repository.findByEmail(email).orElseThrow(() -> new NotFoundException("Not founf user with email = " + email)));

    }

    private User prepareAndSave(User user) {
        return repository.save(prepareToSave(user, passwordEncoder));
    }
}