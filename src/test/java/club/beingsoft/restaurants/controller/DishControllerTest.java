package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.model.Dish;
import club.beingsoft.restaurants.model.User;
import club.beingsoft.restaurants.util.SecurityUtil;
import club.beingsoft.restaurants.util.exception.EntityDeletedException;
import club.beingsoft.restaurants.util.exception.NotFoundException;
import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
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

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            System.out.println("Starting test: " + description.getMethodName());
        }
    };

    @Test
    public void getAllDishes() {
        List<Dish> dishDB = dishController.getAll();
        Assert.assertEquals(DISHES, dishDB);
    }

    @Test
    public void getDish() {
        Dish dishDB = dishController.get(DISH_1_ID);
        Assert.assertEquals(DISH_1, dishDB);
    }

    @Test
    public void getDishNullId() {
        Assert.assertThrows(ConstraintViolationException.class, () -> dishController.get(null));
    }

    @Test
    public void getDishNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> dishController.get(NOT_FOUND_ID));
    }

    @Test
    public void saveNewDish() {
        Dish dishDB = (Dish) dishController.save(null, getNewDish()).getBody();
        assert dishDB != null;
        Dish newDish = getNewDish();
        newDish.setId(dishDB.getId());
        Assert.assertEquals(newDish, dishDB);
    }

    @Test
    public void saveNewNullDish() {
        Assert.assertThrows(ConstraintViolationException.class, () -> dishController.save(null, null));
    }

    @Test
    public void updateDish() {
        Dish dishDB = (Dish) dishController.save(DISH_3_ID, UPDATED_DISH).getBody();
        Assert.assertEquals(UPDATED_DISH, dishDB);
    }

    @Test
    public void deleteDish() {
        dishController.delete(DELETED_DISH_ID);
        Dish dishDB = dishController.get(DELETED_DISH_ID);
        Assert.assertEquals(DELETED_DISH, dishDB);
    }

    @Test
    public void deleteDishNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> dishController.delete(NOT_FOUND_ID));
    }

    @Test
    public void deleteDishInMenu() {
        Assert.assertThrows(EntityDeletedException.class, () -> dishController.delete(DISH_1_ID));
    }

}