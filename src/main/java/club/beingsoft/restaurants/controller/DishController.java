package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.model.Dish;
import club.beingsoft.restaurants.model.Menu;
import club.beingsoft.restaurants.model.QMenu;
import club.beingsoft.restaurants.repository.jpa.DishJpaRepository;
import club.beingsoft.restaurants.repository.jpa.MenuJpaRepository;
import club.beingsoft.restaurants.util.CheckPermissions;
import club.beingsoft.restaurants.util.exception.EntityNotDeletedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/rest/dishes")
public class DishController {

    @Autowired
    private DishJpaRepository dishJpaRepository;

    @Autowired
    private MenuJpaRepository menuJpaRepository;

    @GetMapping(produces = "application/json")
    public List<Dish> getAllDishes() {
        return dishJpaRepository.findAll();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public Dish getDish(@PathVariable Integer id) {
        return dishJpaRepository.findById(id).get();
    }

    @PostMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> saveDish(
            @PathVariable Integer id,
            @RequestBody Dish dish
    ) {
        CheckPermissions.checkAdmin();
        if (id != null) dish.setId(id);
        dish.setUser();
        return new ResponseEntity(dishJpaRepository.save(dish), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteDish(
            @PathVariable Integer id
    ) {
        CheckPermissions.checkAdmin();
        Dish dish = getDish(id);
        List<Menu> menus = (List<Menu>) menuJpaRepository.findAll(
                QMenu.menu.dishes
                        .contains(dish)
                        .and(QMenu.menu.delete_user.isNull())
        );
        if (menus.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder("Menu NOT deleted: ");
            menus.forEach(menu -> {
                stringBuilder.append(menu);
                stringBuilder.append(", ");
            });
            throw new EntityNotDeletedException(stringBuilder.toString());
        }
        dish.delete();
        dishJpaRepository.save(dish);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
