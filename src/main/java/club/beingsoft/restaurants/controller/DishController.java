package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.model.Dish;
import club.beingsoft.restaurants.model.Menu;
import club.beingsoft.restaurants.model.QMenu;
import club.beingsoft.restaurants.repository.jpa.DishJpaRepository;
import club.beingsoft.restaurants.repository.jpa.MenuJpaRepository;
import club.beingsoft.restaurants.util.exception.EntityDeletedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

import static club.beingsoft.restaurants.util.ValidationUtil.assureIdConsistent;
import static club.beingsoft.restaurants.util.ValidationUtil.getFoundException;

@RestController
@RequestMapping(path = "/rest/dishes")
@Transactional(readOnly = true)
@Validated
public class DishController {

    @Autowired
    private DishJpaRepository dishJpaRepository;

    @Autowired
    private MenuJpaRepository menuJpaRepository;

    @GetMapping(produces = "application/json")
    public List<Dish> getAll() {
        return dishJpaRepository.findAll();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public Dish get(@PathVariable @NotNull Integer id) {
        return dishJpaRepository.findById(id).orElseThrow(() -> getFoundException(Dish.class, id));
    }

    @PostMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    @Transactional
    public ResponseEntity save(
            @PathVariable Integer id,
            @RequestBody @NotNull Dish dish
    ) {
        assureIdConsistent(dish, id);
        dish.setUser();
        return new ResponseEntity(dishJpaRepository.save(dish), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    @Transactional
    public ResponseEntity delete(
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
        dish.delete();
        dishJpaRepository.save(dish);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
