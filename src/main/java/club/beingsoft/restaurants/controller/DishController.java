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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static club.beingsoft.restaurants.util.ValidationUtil.checkEntityNotNull;
import static club.beingsoft.restaurants.util.ValidationUtil.checkId;

@RestController
@RequestMapping(path = "/rest/dishes")
public class DishController {

    @Autowired
    private DishJpaRepository dishJpaRepository;

    @Autowired
    private MenuJpaRepository menuJpaRepository;

    private static final String DISH_ENTITY = "Dish";

    @GetMapping(produces = "application/json")
    public List<Dish> getAllDishes() {
        return dishJpaRepository.findAll();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public Dish getDish(@PathVariable Integer id) {
        checkId(DISH_ENTITY, id);
        Optional<Dish> dishDB = dishJpaRepository.findById(id);
        checkEntityNotNull(DISH_ENTITY, dishDB, id);
        return dishDB.get();
    }

    @PostMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> saveDish(
            @PathVariable Integer id,
            @RequestBody Dish dish
    ) {
        checkEntityNotNull(DISH_ENTITY, dish, id);

        if (id != null) dish.setId(id);
        dish.setUser();
        return new ResponseEntity(checkEntityNotNull(DISH_ENTITY, dishJpaRepository.save(dish), id), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteDish(
            @PathVariable Integer id
    ) {
        checkId(DISH_ENTITY, id);

        Dish dish = getDish(id);
        List<Menu> menus = (List<Menu>) menuJpaRepository.findAll(
                QMenu.menu.dishes
                        .contains(dish)
                        .and(QMenu.menu.delete_user.isNull())
        );
        if (menus.size() > 0) {
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
