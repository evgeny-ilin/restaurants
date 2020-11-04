package club.beingsoft.restaurants.controller;

//Field selection + pagging + version API + X-HTTP-Method-Override https://medium.com/@mwaysolutions/10-best-practices-for-better-restful-api-cbe81b06f291

import club.beingsoft.restaurants.model.Restaurant;
import club.beingsoft.restaurants.repository.jpa.RestaurantJpaRepository;
import club.beingsoft.restaurants.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/rest/restaurants")
public class RestaurantController {
    @Autowired
    private RestaurantJpaRepository restaurantJpaRepository;

    @GetMapping(produces = "application/json")
    public List<Restaurant> getAllRestaurants() {
        return restaurantJpaRepository.findAll();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public Restaurant getRestaurant(@PathVariable Integer id) {
        return restaurantJpaRepository.findById(id).get();
    }

    @PostMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> saveRestaurant(
            @PathVariable Integer id,
            @RequestBody Restaurant restaurant
    ) {
        ValidationUtil.checkAdmin();
        if (id != null) restaurant.setId(id);
        restaurant.setUser();
        return new ResponseEntity(restaurantJpaRepository.save(restaurant), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteRestaurant(
            @PathVariable Integer id
    ) {
        ValidationUtil.checkAdmin();
        Restaurant restaurant = restaurantJpaRepository.findById(id).get();
        restaurant.delete();
        restaurantJpaRepository.save(restaurant);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
