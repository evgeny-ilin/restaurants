package club.beingsoft.restaurants.controller;

//Field selection + pagging + version API + X-HTTP-Method-Override https://medium.com/@mwaysolutions/10-best-practices-for-better-restful-api-cbe81b06f291

import club.beingsoft.restaurants.model.Restaurant;
import club.beingsoft.restaurants.repository.jpa.RestaurantJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/rest/restaurants")
public class RestaurantController {
    @Autowired
    private RestaurantJpaRepository repository;

    @GetMapping(produces = "application/json")
    public List<Restaurant> getAllRestaurants() {
        return repository.findAll();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public Restaurant getRestaurant(@PathVariable Integer id) {
        return repository.findById(id).get();
    }

    @PostMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> saveRestaurant(
            @PathVariable Integer id,
            @RequestBody Restaurant restaurant
    ) {
        if (id > 0) restaurant.setId(id);
        restaurant.setUser();
        return new ResponseEntity(repository.save(restaurant), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteDish(
            @PathVariable Integer id
    ) {
        repository.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
