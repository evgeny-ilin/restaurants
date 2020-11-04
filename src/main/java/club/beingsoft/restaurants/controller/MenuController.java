package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.model.Dish;
import club.beingsoft.restaurants.model.Menu;
import club.beingsoft.restaurants.model.QDish;
import club.beingsoft.restaurants.model.Restaurant;
import club.beingsoft.restaurants.repository.jpa.DishJpaRepository;
import club.beingsoft.restaurants.repository.jpa.MenuJpaRepository;
import club.beingsoft.restaurants.repository.jpa.RestaurantJpaRepository;
import club.beingsoft.restaurants.util.exception.EntityNotDeletedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static club.beingsoft.restaurants.util.ValidationUtil.*;

@RestController
@RequestMapping(path = "/rest/menus")
public class MenuController {
    @Autowired
    private MenuJpaRepository menuJpaRepository;

    @Autowired
    private RestaurantJpaRepository restaurantJpaRepository;

    @Autowired
    private DishJpaRepository dishJpaRepository;

    private static final String MENU_ENTITY = "Menu";

    @GetMapping(produces = "application/json")
    public List<Menu> getAllMenus() {
        return menuJpaRepository.findAll();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public Menu getMenu(@PathVariable Integer id) {
        checkId(MENU_ENTITY, id);
        Optional<Menu> menu = menuJpaRepository.findById(id);
        checkEntityNotNull(MENU_ENTITY, menu, id);
        return menu.get();
    }

    @PostMapping(path = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> saveMenu(
            @RequestParam(name = "menu") Integer menuId,
            @RequestParam(name = "restaurant") Integer restaurantId,
            @RequestBody Menu menu
    ) {
        checkAdmin();
        if (menuId != null) menu.setId(menuId);
        Optional<Restaurant> restaurantDB = restaurantJpaRepository.findById(restaurantId);
        checkEntityNotNull("Restaurant", restaurantDB, restaurantId);
        Restaurant restaurant = restaurantDB.get();

        if (restaurant.isDeleted())
            throw new EntityNotDeletedException(restaurant.getName() + " was deleted");
        menu.setRestaurant(restaurant);
        menu.setUser();
        return new ResponseEntity(checkEntityNotNull(MENU_ENTITY, menuJpaRepository.save(menu), menuId), HttpStatus.CREATED);
    }

    //TODO make tests
    @PostMapping(path = "/link", produces = "application/json")
    public ResponseEntity<Object> linkDishToMenu(
            @RequestParam(name = "menuId") Integer menuId,
            @RequestParam(name = "dishesIds") List<Integer> dishesIds
    ) {
        checkAdmin();
        checkId(MENU_ENTITY, menuId);
        checkCollectionFound("DISHES IDs", dishesIds);
        Menu menu = getMenu(menuId);
        Set<Dish> dishes = new HashSet<Dish>((Collection) dishJpaRepository.findAll(QDish.dish.id.in(dishesIds)));
        checkCollectionFound("DISHES", dishes);
        checkDeleted(dishes);
        menu.setDishes(dishes);
        return new ResponseEntity(checkEntityNotNull(MENU_ENTITY, menuJpaRepository.save(menu), menuId), HttpStatus.CREATED);
    }

    //TODO make tests
    @PostMapping(path = "/unlink", produces = "application/json")
    public ResponseEntity<Object> unlinkDishFromMenu(
            @RequestParam(name = "menuId") Integer menuId,
            @RequestParam(name = "dishesIds") List<Integer> dishesIds
    ) {
        checkAdmin();
        checkId(MENU_ENTITY, menuId);
        checkCollectionFound("DISHES IDs", dishesIds);

        Menu menu = getMenu(menuId);
        Set<Dish> dishes = new HashSet<Dish>((Collection) dishJpaRepository.findAll(QDish.dish.id.in(dishesIds)));
        checkCollectionFound("DISHES", dishes);
        checkDeleted(dishes);
        menu.removeDish(dishes);
        return new ResponseEntity(menuJpaRepository.save(menu), HttpStatus.CREATED);
    }

    //TODO make tests
    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteMenu(
            @PathVariable Integer id
    ) {
        checkAdmin();
        checkId(MENU_ENTITY, id);
        Optional<Menu> menuDB = menuJpaRepository.findById(id);
        checkEntityNotNull(MENU_ENTITY, menuDB, id);
        Menu menu = menuDB.get();
        menu.delete();
        checkEntityNotNull(MENU_ENTITY, menuJpaRepository.save(menu), id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
