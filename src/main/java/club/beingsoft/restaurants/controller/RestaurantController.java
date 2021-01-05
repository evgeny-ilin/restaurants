package club.beingsoft.restaurants.controller;

//Field selection + pagging + version API + X-HTTP-Method-Override https://medium.com/@mwaysolutions/10-best-practices-for-better-restful-api-cbe81b06f291

import club.beingsoft.restaurants.model.Restaurant;
import club.beingsoft.restaurants.repository.jpa.RestaurantJpaRepository;
import club.beingsoft.restaurants.to.RestaurantWithVotesTo;
import club.beingsoft.restaurants.util.SecurityUtil;
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
@RequestMapping(path = "/rest/restaurants")
@Transactional(readOnly = true)
@Validated
public class RestaurantController {
    @Autowired
    private RestaurantJpaRepository restaurantJpaRepository;

    @GetMapping(produces = "application/json")
    public List<Restaurant> getAll() {
        return restaurantJpaRepository.findAll();
    }

    @GetMapping(path = "/withdishes", produces = "application/json")
    public List<Restaurant> getAllWithDishesToday() {
        return restaurantJpaRepository.getAllWithDishesToday();
    }

    @GetMapping(path = "/sortedbyvotes", produces = "application/json")
    public List<RestaurantWithVotesTo> getSortedByVotes() {
        return restaurantJpaRepository.getSortedByVotesToday();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public Restaurant get(@PathVariable @NotNull Integer id) {
        return restaurantJpaRepository.findById(id).orElseThrow(() -> getFoundException(Restaurant.class, id));
    }

    @PostMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    @Transactional
    public ResponseEntity<Object> save(
            @PathVariable Integer id,
            @RequestBody @NotNull Restaurant restaurant
    ) {
        assureIdConsistent(restaurant, id);
        restaurant.setUser(SecurityUtil.getAuthUser());
        return new ResponseEntity(restaurantJpaRepository.save(restaurant), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    @Transactional
    public ResponseEntity delete(
            @PathVariable @NotNull Integer id
    ) {
        Restaurant restaurant = restaurantJpaRepository.findById(id).orElseThrow(() -> getFoundException(Restaurant.class, id));
        restaurant.delete(SecurityUtil.getAuthUser());
        restaurantJpaRepository.save(restaurant);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
