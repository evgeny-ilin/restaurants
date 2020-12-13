package club.beingsoft.restaurants.controller;

//Field selection + pagging + version API + X-HTTP-Method-Override https://medium.com/@mwaysolutions/10-best-practices-for-better-restful-api-cbe81b06f291

import club.beingsoft.restaurants.model.Restaurant;
import club.beingsoft.restaurants.repository.jpa.RestaurantJpaRepository;
import club.beingsoft.restaurants.to.RestaurantWithVotesTo;
import club.beingsoft.restaurants.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

import static club.beingsoft.restaurants.util.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(path = "/rest/restaurants")
@Transactional(readOnly = true)
@Validated
public class RestaurantController {
    @Autowired
    private RestaurantJpaRepository restaurantJpaRepository;

    @GetMapping(produces = "application/json")
    public List<Restaurant> getAllRestaurants() {
        return restaurantJpaRepository.findAll();
    }

    @GetMapping(path = "/withdishes", produces = "application/json")
    public List<Restaurant> getAllRestaurantsWithDishesToday() {
        return restaurantJpaRepository.getAllRestaurantsWithDishesToday();
    }

    @GetMapping(path = "/sortedbyvotes", produces = "application/json")
    public List<RestaurantWithVotesTo> getSortedByVotesRestaurants() {
        return restaurantJpaRepository.getSortedByVotesRestaurantsToday();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public Restaurant getRestaurant(@PathVariable @NotNull Integer id) {
        return restaurantJpaRepository.findById(id).orElseThrow(() -> new NotFoundException(Restaurant.class, id));
    }

    @PostMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    @Transactional
    public ResponseEntity<Object> saveRestaurant(
            @PathVariable Integer id,
            @RequestBody @NotNull Restaurant restaurant
    ) {
        assureIdConsistent(restaurant, id);
        restaurant.setUser();
        return new ResponseEntity(restaurantJpaRepository.save(restaurant), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    @Transactional
    public ResponseEntity deleteRestaurant(
            @PathVariable @NotNull Integer id
    ) {
        Restaurant restaurant = restaurantJpaRepository.findById(id).orElseThrow(() -> new NotFoundException(Restaurant.class, id));
        restaurant.delete();
        restaurantJpaRepository.save(restaurant);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
