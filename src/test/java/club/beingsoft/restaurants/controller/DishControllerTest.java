package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.model.Dish;
import club.beingsoft.restaurants.model.User;
import club.beingsoft.restaurants.util.SecurityUtil;
import club.beingsoft.restaurants.util.exception.EntityDeletedException;
import club.beingsoft.restaurants.util.exception.NotFoundException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static club.beingsoft.restaurants.DishTestData.*;
import static club.beingsoft.restaurants.UserTestData.ADMIN;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class DishControllerTest {
    @Autowired
    private DishController dishController;

    private static MockedStatic<SecurityUtil> securityUtilMocked;

    @BeforeClass
    public static void beforeAll() {
        securityUtilMocked = Mockito.mockStatic(SecurityUtil.class);
        User user = ADMIN;
        securityUtilMocked.when(SecurityUtil::getAuthUser).thenReturn(user);
    }

    @AfterClass
    public static void close() {
        securityUtilMocked.close();
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
    public void getDishNullId() {
        Assert.assertThrows(IllegalArgumentException.class, () -> dishController.getDish(null));
    }

    @Test
    public void getDishNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> dishController.getDish(NOT_FOUND_ID));
    }

    @Test
    public void saveNewDish() {
        Dish dishDB = (Dish) dishController.saveDish(null, NEW_DISH).getBody();
        assert dishDB != null;
        NEW_DISH.setId(dishDB.getId());
        Assert.assertEquals(NEW_DISH, dishDB);
    }

    @Test
    public void saveNewNullDish() {
        Assert.assertThrows(NotFoundException.class, () -> dishController.saveDish(null, null));
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
    public void deleteDishNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> dishController.deleteDish(NOT_FOUND_ID));
    }

    @Test
    public void deleteDishInMenu() {
        Assert.assertThrows(EntityDeletedException.class, () -> dishController.deleteDish(DISH_1_ID));
    }

}