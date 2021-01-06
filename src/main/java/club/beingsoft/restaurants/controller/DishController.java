package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.model.Dish;
import club.beingsoft.restaurants.model.Menu;
import club.beingsoft.restaurants.model.QMenu;
import club.beingsoft.restaurants.repository.jpa.DishJpaRepository;
import club.beingsoft.restaurants.repository.jpa.MenuJpaRepository;
import club.beingsoft.restaurants.util.SecurityUtil;
import club.beingsoft.restaurants.util.exception.EntityDeletedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

import static club.beingsoft.restaurants.util.ValidationUtil.assureIdConsistent;
import static club.beingsoft.restaurants.util.ValidationUtil.getFoundException;

@RestController
@RequestMapping(path = "/rest/dishes", produces = "application/json")
@Transactional(readOnly = true)
@Validated
public class DishController {

    @Autowired
    private DishJpaRepository dishJpaRepository;

    @Autowired
    private MenuJpaRepository menuJpaRepository;

    @GetMapping
    public List<Dish> getAll() {
        return dishJpaRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public Dish get(@PathVariable @NotNull Integer id) {
        return dishJpaRepository.findById(id).orElseThrow(() -> getFoundException(Dish.class, id));
    }

    @PostMapping(path = "/{id}", consumes = "application/json")
    @Transactional
    public ResponseEntity<Dish> save(
            @PathVariable Integer id,
            @RequestBody @NotNull Dish dish
    ) {
        assureIdConsistent(dish, id);
        dish.setUser(SecurityUtil.getAuthUser());
        boolean isNew = dish.isNew();
        dishJpaRepository.save(dish);
        if (isNew) {
            URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/rest/dishes/{id}")
                    .buildAndExpand(dish.getId()).toUri();
            return ResponseEntity.created(uriOfNewResource).body(dish);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void delete(
            @PathVariable @NotNull Integer id
    ) {
        Dish dish = get(id);
        List<Menu> menus = (List<Menu>) menuJpaRepository.findAll(
                QMenu.menu.dishes
                        .contains(dish)
                        .and(QMenu.menu.deleteUser.isNull())
        );
        if (!menus.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder("Dish NOT deleted cause it's in menu: ");
            menus.forEach(menu -> {
                stringBuilder.append(menu);
                stringBuilder.append(", ");
            });
            stringBuilder.delete(stringBuilder.lastIndexOf(","), stringBuilder.length());
            throw new EntityDeletedException(stringBuilder.toString());
        }
        dish.delete(SecurityUtil.getAuthUser());
        dishJpaRepository.save(dish);
    }
}
