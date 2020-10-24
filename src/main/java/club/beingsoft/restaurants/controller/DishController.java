package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.model.Dish;
import club.beingsoft.restaurants.repository.jpa.DishJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/rest/dishes")
public class DishController {

    @Autowired
    private DishJpaRepository repository;

    @GetMapping(produces = "application/json")
    public List<Dish> getAllDishes() {
        return repository.findAll();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public Dish getDish(@PathVariable Integer id) {
        return repository.findById(id).get();
    }

    @PostMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> saveDish(
            @PathVariable Integer id,
            @RequestBody Dish dish
    ) {
        if (id > 0) dish.setId(id);
        dish.setUser();
        return new ResponseEntity(repository.save(dish), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteDish(
            @PathVariable Integer id
    ) {
        repository.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
