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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Locale;

import static club.beingsoft.restaurants.DishTestData.*;
import static club.beingsoft.restaurants.UserTestData.ADMIN;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class DishControllerTest {
    private static final Logger log = LoggerFactory.getLogger(DishControllerTest.class);

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
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        List<Dish> dishDB = dishController.getAllDishes();
        Assert.assertEquals(DISHES, dishDB);
    }

    @Test
    public void getDish() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Dish dishDB = dishController.getDish(DISH_1_ID);
        Assert.assertEquals(DISH_1, dishDB);
    }

    @Test
    public void getDishNullId() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Assert.assertThrows(ConstraintViolationException.class, () -> dishController.getDish(null));
    }

    @Test
    public void getDishNotFound() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Assert.assertThrows(NotFoundException.class, () -> dishController.getDish(NOT_FOUND_ID));
    }

    @Test
    public void saveNewDish() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Dish dishDB = (Dish) dishController.saveDish(null, getNewDish()).getBody();
        assert dishDB != null;
        Dish newDish = getNewDish();
        newDish.setId(dishDB.getId());
        Assert.assertEquals(newDish, dishDB);
    }

    @Test
    public void saveNewNullDish() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Assert.assertThrows(ConstraintViolationException.class, () -> dishController.saveDish(null, null));
    }

    @Test
    public void updateDish() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Dish dishDB = (Dish) dishController.saveDish(DISH_3_ID, UPDATED_DISH).getBody();
        Assert.assertEquals(UPDATED_DISH, dishDB);
    }

    @Test
    public void deleteDish() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        dishController.deleteDish(DELETED_DISH_ID);
        Dish dishDB = dishController.getDish(DELETED_DISH_ID);
        Assert.assertEquals(DELETED_DISH, dishDB);
    }

    @Test
    public void deleteDishNotFound() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Assert.assertThrows(NotFoundException.class, () -> dishController.deleteDish(NOT_FOUND_ID));
    }

    @Test
    public void deleteDishInMenu() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Assert.assertThrows(EntityDeletedException.class, () -> dishController.deleteDish(DISH_1_ID));
    }

}