package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.model.Dish;
import club.beingsoft.restaurants.repository.jpa.DishJpaRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DishControllerTest {

    @Autowired
    private DishJpaRepository dishJpaRepository;

    @Autowired
    private DishController dishController;

    @Test
    public void whenFindingDishById_thenCorrect() {
        List<Dish> dishes = dishController.getAllDishes();
        dishes.stream().forEach(System.out::println);
    }
}