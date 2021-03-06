package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.model.User;
import club.beingsoft.restaurants.service.UserService;
import club.beingsoft.restaurants.to.UserTo;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

import static club.beingsoft.restaurants.util.UserUtil.asTo;
import static club.beingsoft.restaurants.util.UserUtil.createNewFromTo;
import static club.beingsoft.restaurants.util.ValidationUtil.assureIdConsistent;
import static club.beingsoft.restaurants.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(path = "/rest/users")
@Transactional(readOnly = true)
@Validated
@Tag(name = "Пользователи", description = "Управление пользователями")
public class UserController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService service;

    @GetMapping
    public List<UserTo> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    @GetMapping("/{id}")
    public UserTo get(@PathVariable @NotNull Integer id) {
        log.info("get {}", id);
        return service.get(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public UserTo create(@RequestBody @NotNull UserTo userTo) {
        log.info("create from to {}", userTo);
        User user = createNewFromTo(userTo);
        checkNew(user);
        return asTo(service.create(user));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void delete(@PathVariable @NotNull Integer id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@RequestBody @NotNull UserTo userTo, @PathVariable @NotNull Integer id) {
        log.info("update {} with id={}", userTo, id);
        assureIdConsistent(userTo, id);
        service.update(userTo);
    }

    @GetMapping("/by")
    public UserTo getByMail(@RequestParam @NotNull String email) {
        log.info("getByEmail {}", email);
        return service.getByEmail(email);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Transactional
    public void enable(@PathVariable @NotNull Integer id, @RequestParam @NotNull boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        service.enable(id, enabled);
    }
}
