package club.beingsoft.restaurants.controller;

//Field selection + pagging + version API + X-HTTP-Method-Override https://medium.com/@mwaysolutions/10-best-practices-for-better-restful-api-cbe81b06f291

import club.beingsoft.restaurants.model.Menu;
import club.beingsoft.restaurants.model.Restaurant;
import club.beingsoft.restaurants.repository.jpa.RestaurantJpaRepository;
import club.beingsoft.restaurants.to.RestaurantWithVotesTo;
import club.beingsoft.restaurants.util.SecurityUtil;
import club.beingsoft.restaurants.util.ValidationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static club.beingsoft.restaurants.util.ValidationUtil.assureIdConsistent;
import static club.beingsoft.restaurants.util.ValidationUtil.getFoundException;

@RestController
@RequestMapping(path = "/rest/restaurants", produces = "application/json")
@Transactional(readOnly = true)
@Validated
@Tag(name = "Рестораны", description = "Управление ресторанами. Можно получить некоторую статистику")
public class RestaurantController {
    @Autowired
    private RestaurantJpaRepository restaurantJpaRepository;

    @GetMapping
    public List<Restaurant> getAll() {
        return restaurantJpaRepository.findAll();
    }

    @GetMapping(path = "/withdishes")
    @Cacheable("restaurants")
    @Operation(summary = "Рестораны, имеющие блюда", description = "Выводит список действующих ресторанов у которых есть действующие блюда и меню")
    public List<Restaurant> getAllWithDishesToDate(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        date = ValidationUtil.getLocalDate(date);
        return restaurantJpaRepository.getAllWithDishesToDate(date);
    }

    @GetMapping(path = "/sortedbyvotes")
    @Cacheable("restaurantsTo")
    @Operation(summary = "Рестораны с голосами", description = "Выводит список действующих ресторанов, отсортированных по количеству голосов на дату")
    public List<RestaurantWithVotesTo> getSortedByVotes(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        date = ValidationUtil.getLocalDate(date);
        return restaurantJpaRepository.getSortedByVotes(date);
    }

    @GetMapping(path = "/hierarchy")
    @Cacheable("menus")
    @Operation(summary = "Рестораны с блюдами", description = "Выводит рестораны, меню и блюда на дату")
    public List<Menu> getAllHierarchy(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        date = ValidationUtil.getLocalDate(date);
        return restaurantJpaRepository.getAllHierarchy(date);
    }

    @GetMapping(path = "/{id}")
    @Cacheable("restaurants")
    public Restaurant get(@PathVariable @NotNull Integer id) {
        return restaurantJpaRepository.findById(id).orElseThrow(() -> getFoundException(Restaurant.class, id));
    }

    @PostMapping(path = "/{id}", consumes = "application/json")
    @CacheEvict({"restaurants", "restaurantsTo", "menus"})
    @Transactional
    public ResponseEntity<Restaurant> save(
            @PathVariable Integer id,
            @RequestBody @NotNull Restaurant restaurant
    ) {
        assureIdConsistent(restaurant, id);
        restaurant.setUser(SecurityUtil.getAuthUser());
        boolean isNew = restaurant.isNew();
        restaurantJpaRepository.save(restaurant);
        if (isNew) {
            URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/rest/menus/{id}")
                    .buildAndExpand(restaurant.getId()).toUri();
            return ResponseEntity.created(uriOfNewResource).body(restaurant);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping(path = "/{id}")
    @Transactional
    @CacheEvict({"restaurants", "restaurantsTo", "menus"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable @NotNull Integer id
    ) {
        Restaurant restaurant = restaurantJpaRepository.findById(id).orElseThrow(() -> getFoundException(Restaurant.class, id));
        restaurant.delete(SecurityUtil.getAuthUser());
        restaurantJpaRepository.save(restaurant);
    }
}
