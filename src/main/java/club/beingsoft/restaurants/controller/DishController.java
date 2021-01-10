package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.model.Dish;
import club.beingsoft.restaurants.model.Menu;
import club.beingsoft.restaurants.model.QMenu;
import club.beingsoft.restaurants.repository.jpa.DishJpaRepository;
import club.beingsoft.restaurants.repository.jpa.MenuJpaRepository;
import club.beingsoft.restaurants.to.DishTo;
import club.beingsoft.restaurants.util.SecurityUtil;
import club.beingsoft.restaurants.util.ValidationUtil;
import club.beingsoft.restaurants.util.exception.EntityDeletedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static club.beingsoft.restaurants.util.DishUtil.asTo;
import static club.beingsoft.restaurants.util.DishUtil.createNewFromTo;
import static club.beingsoft.restaurants.util.ValidationUtil.assureIdConsistent;
import static club.beingsoft.restaurants.util.ValidationUtil.getFoundException;

@RestController
@RequestMapping(path = "/rest/dishes", produces = MediaType.APPLICATION_JSON_VALUE)
@Transactional(readOnly = true)
@Validated
@Tag(name = "Блюда", description = "Управление блюдами. Связка блюд с меню через контроллер меню")
public class DishController {

    @Autowired
    private DishJpaRepository dishJpaRepository;

    @Autowired
    private MenuJpaRepository menuJpaRepository;

    @GetMapping
    public List<Dish> getAll() {
        return dishJpaRepository.findAll();
    }

    @GetMapping(path = "/restaurant")
    @Cacheable("dishes")
    @Operation(summary = "Список блюд для ресторана на дату")
    public List<Dish> getDishesForRestaurant(@RequestParam @NotNull Integer restaurantId,
                                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        date = ValidationUtil.getLocalDate(date);
        return dishJpaRepository.getDishesForRestaurant(restaurantId, date);
    }

    @GetMapping(path = "/{id}")
    @Cacheable("dishes")
    public Dish get(@PathVariable @NotNull Integer id) {
        return dishJpaRepository.findById(id).orElseThrow(() -> getFoundException(Dish.class, id));
    }

    @GetMapping(path = "/menu/{menuId}")
    @Cacheable("dishes")
    @Operation(summary = "Список блюд для меню")
    public List<Dish> getDishesForMenu(@PathVariable @NotNull Integer menuId) {
        return dishJpaRepository.getDishesForMenu(menuId);
    }

    @PostMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict("dishes")
    @Transactional
    public ResponseEntity<DishTo> save(
            @PathVariable Integer id,
            @RequestBody @NotNull DishTo dishTo
    ) {
        Dish dish = createNewFromTo(dishTo);
        assureIdConsistent(dish, id);
        dish.setUser(SecurityUtil.getAuthUser());
        boolean isNew = dish.isNew();
        dishJpaRepository.save(dish);
        if (isNew) {
            URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/rest/dishes/{id}")
                    .buildAndExpand(dish.getId()).toUri();
            return ResponseEntity.created(uriOfNewResource).body(asTo(dish));
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict("dishes")
    @Transactional
    public void delete(
            @PathVariable @NotNull Integer id
    ) {
        Dish dish = get(id);
        List<Menu> menus = (List<Menu>) menuJpaRepository.findAll(
                QMenu.menu.dishes
                        .contains(dish)
                        .and(QMenu.menu.deleteUser.isNull())
        );
        if (!menus.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder("Dish NOT deleted cause it's in menu: ");
            menus.forEach(menu -> {
                stringBuilder.append(menu);
                stringBuilder.append(", ");
            });
            stringBuilder.delete(stringBuilder.lastIndexOf(","), stringBuilder.length());
            throw new EntityDeletedException(stringBuilder.toString());
        }
        dish.delete(SecurityUtil.getAuthUser());
        dishJpaRepository.save(dish);
    }
}
