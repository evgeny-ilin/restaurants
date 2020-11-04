package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.model.Dish;
import club.beingsoft.restaurants.util.SecurityUtil;
import club.beingsoft.restaurants.util.exception.PermissionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static club.beingsoft.restaurants.DishTestData.*;
import static club.beingsoft.restaurants.UserTestData.ADMIN;
import static club.beingsoft.restaurants.UserTestData.USER;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class DishControllerTest {

    @Autowired
    private DishController dishController;

    @Before
    public void before() {
        SecurityUtil.setAuthUser(ADMIN);
    }

    @Test
    public void getAllDishes() {
        List<Dish> dishDB = dishController.getAllDishes();
        Assert.assertEquals(DISHES, dishDB);
    }

    @Test
    public void getDish() {
        Dish dishDB = dishController.getDish(DISH_1_ID);
        Assert.assertEquals(DISH_1, dishDB);
    }

    @Test
    public void saveNewDish() {
        Dish dishDB = (Dish) dishController.saveDish(null, NEW_DISH).getBody();
        NEW_DISH.setId(dishDB.getId());
        Assert.assertEquals(NEW_DISH, dishDB);
    }

    @Test
    public void saveDishUser() {
        SecurityUtil.setAuthUser(USER);
        Assert.assertThrows(PermissionException.class, () -> dishController.saveDish(null, NEW_DISH).getBody());
    }

    @Test
    public void updateDish() {
        Dish dishDB = (Dish) dishController.saveDish(DISH_3_ID, UPDATED_DISH).getBody();
        Assert.assertEquals(UPDATED_DISH, dishDB);
    }

    @Test
    public void deleteDish() {
        dishController.deleteDish(DELETED_DISH_ID);
        Dish dishDB = dishController.getDish(DELETED_DISH_ID);
        Assert.assertEquals(DELETED_DISH, dishDB);
    }

    @Test
    public void deleteDishUser() {
        SecurityUtil.setAuthUser(USER);
        Assert.assertThrows(PermissionException.class, () -> dishController.deleteDish(DELETED_DISH_ID));
    }
}