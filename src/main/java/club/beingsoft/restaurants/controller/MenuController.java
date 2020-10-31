package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.model.Dish;
import club.beingsoft.restaurants.model.Menu;
import club.beingsoft.restaurants.model.QDish;
import club.beingsoft.restaurants.repository.jpa.DishJpaRepository;
import club.beingsoft.restaurants.repository.jpa.MenuJpaRepository;
import club.beingsoft.restaurants.repository.jpa.RestaurantJpaRepository;
import club.beingsoft.restaurants.util.CheckAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/rest/menus")
public class MenuController {
    @Autowired
    private MenuJpaRepository menuJpaRepository;

    @Autowired
    private RestaurantJpaRepository restaurantJpaRepository;

    @Autowired
    private DishJpaRepository dishJpaRepository;

    @GetMapping(produces = "application/json")
    public List<Menu> getAllMenus() {
        return menuJpaRepository.findAll();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public Menu getMenu(@PathVariable Integer id) {
        return menuJpaRepository.findById(id).get();
    }

    @PostMapping(path = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> saveMenu(
            @RequestParam(name = "menu") Integer menuId,
            @RequestParam(name = "restaurant") Integer restaurantId,
            @RequestBody Menu menu
    ) {
        CheckAdmin.check();
        if (menuId > 0) menu.setId(menuId);
        menu.setRestaurant(restaurantJpaRepository.findById(restaurantId).get());
        menu.setUser();
        return new ResponseEntity(menuJpaRepository.save(menu), HttpStatus.CREATED);
    }

    @PostMapping(path = "/link", produces = "application/json")
    public ResponseEntity<Object> linkDishToMenu(
            @RequestParam(name = "menuId") Integer menuId,
            @RequestParam(name = "dishesIds") List<Integer> dishesIds
    ) {
        CheckAdmin.check();
        Menu menu = getMenu(menuId);
        menu.setId(menuId);

        Set<Dish> dishes = new HashSet<Dish>((Collection) dishJpaRepository.findAll(QDish.dish.id.in(dishesIds)));
        menu.setDishes(dishes);
        menu.setUser();
        return new ResponseEntity(menuJpaRepository.save(menu), HttpStatus.CREATED);
    }

    @PostMapping(path = "/unlink", produces = "application/json")
    public ResponseEntity<Object> unlinkDishFromMenu(
            @RequestParam(name = "menuId") Integer menuId,
            @RequestParam(name = "dishesIds") List<Integer> dishesIds
    ) {
        CheckAdmin.check();
        Menu menu = getMenu(menuId);
        menu.setId(menuId);

        Set<Dish> dishes = new HashSet<Dish>((Collection) dishJpaRepository.findAll(QDish.dish.id.in(dishesIds)));
        menu.removeDish(dishes);
        menu.setUser();
        return new ResponseEntity(menuJpaRepository.save(menu), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteMenu(
            @PathVariable Integer id
    ) {
        CheckAdmin.check();
        menuJpaRepository.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
