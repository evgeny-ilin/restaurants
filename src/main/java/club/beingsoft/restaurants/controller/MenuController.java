package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.model.Dish;
import club.beingsoft.restaurants.model.Menu;
import club.beingsoft.restaurants.model.QDish;
import club.beingsoft.restaurants.model.Restaurant;
import club.beingsoft.restaurants.repository.jpa.DishJpaRepository;
import club.beingsoft.restaurants.repository.jpa.MenuJpaRepository;
import club.beingsoft.restaurants.repository.jpa.RestaurantJpaRepository;
import club.beingsoft.restaurants.util.SecurityUtil;
import club.beingsoft.restaurants.util.exception.EntityDeletedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static club.beingsoft.restaurants.util.ValidationUtil.*;


@RestController
@RequestMapping(path = "/rest/menus")
@Transactional(readOnly = true)
@Validated
public class MenuController {
    @Autowired
    private MenuJpaRepository menuJpaRepository;

    @Autowired
    private RestaurantJpaRepository restaurantJpaRepository;

    @Autowired
    private DishJpaRepository dishJpaRepository;

    @GetMapping(produces = "application/json")
    public List<Menu> getAll() {
        return menuJpaRepository.findAll();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public Menu getMenu(@PathVariable @NotNull Integer id) {
        return menuJpaRepository.findById(id).orElseThrow(() -> getFoundException(Menu.class, id));
    }

    @PostMapping(path = "/", consumes = "application/json", produces = "application/json")
    @Transactional
    public ResponseEntity<Menu> save(
            @RequestParam(name = "menu") Integer menuId,
            @RequestParam(name = "restaurant") @NotNull Integer restaurantId,
            @RequestBody @NotNull Menu menu
    ) {
        assureIdConsistent(menu, menuId);
        Restaurant restaurant = restaurantJpaRepository.findById(restaurantId).orElseThrow(() -> getFoundException(Restaurant.class, restaurantId));
        if (restaurant.isDeleted())
            throw new EntityDeletedException(restaurant.getName() + " was deleted");
        menu.setRestaurant(restaurant);
        menu.setUser(SecurityUtil.getAuthUser());
        return new ResponseEntity(menuJpaRepository.save(menu), HttpStatus.CREATED);
    }

    @PostMapping(path = "/link", produces = "application/json")
    @Transactional
    public ResponseEntity<Menu> linkDishToMenu(
            @RequestParam(name = "menuId") @NotNull Integer menuId,
            @RequestParam(name = "dishesIds") @NotNull @NotEmpty List<Integer> dishesIds
    ) {
        Menu menu = getMenu(menuId);
        checkEntityDelete(menu);
        Set<Dish> dishes = new HashSet<>((Collection) dishJpaRepository.findAll(QDish.dish.id.in(dishesIds)));
        checkCollectionFound("DISHES", dishes);
        checkDishDeleted(dishes);
        menu.setDishes(dishes);
        return new ResponseEntity(menuJpaRepository.save(menu), HttpStatus.CREATED);
    }

    @PostMapping(path = "/unlink", produces = "application/json")
    @Transactional
    public ResponseEntity<Menu> unlinkDishFromMenu(
            @RequestParam(name = "menuId") @NotNull Integer menuId,
            @RequestParam(name = "dishesIds") @NotNull @NotEmpty List<Integer> dishesIds
    ) {
        Menu menu = getMenu(menuId);
        checkEntityDelete(menu);
        Set<Dish> dishes = new HashSet<>((Collection) dishJpaRepository.findAll(QDish.dish.id.in(dishesIds)));
        checkCollectionFound("DISHES", dishes);
        checkDishDeleted(dishes);
        menu.removeDish(dishes);
        return new ResponseEntity(menuJpaRepository.save(menu), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable @NotNull Integer id
    ) {
        Menu menu = menuJpaRepository.findById(id).orElseThrow(() -> getFoundException(Menu.class, id));
        menu.delete(SecurityUtil.getAuthUser());
        menuJpaRepository.save(menu);
    }
}
