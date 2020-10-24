package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.model.Menu;
import club.beingsoft.restaurants.repository.jpa.MenuJpaRepository;
import club.beingsoft.restaurants.repository.jpa.RestaurantJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/rest/menus")
public class MenuController {
    @Autowired
    private MenuJpaRepository menuJpaRepository;

    @Autowired
    private RestaurantJpaRepository restaurantJpaRepository;

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
        if (menuId > 0) menu.setId(menuId);
        menu.setRestaurant(restaurantJpaRepository.findById(restaurantId).get());
        menu.setUser();
        return new ResponseEntity(menuJpaRepository.save(menu), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteMenu(
            @PathVariable Integer id
    ) {
        menuJpaRepository.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
