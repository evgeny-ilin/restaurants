package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.MenuTestData;
import club.beingsoft.restaurants.model.Menu;
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

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.util.List;

import static club.beingsoft.restaurants.DishTestData.DELETED_DISH_ID;
import static club.beingsoft.restaurants.MenuTestData.*;
import static club.beingsoft.restaurants.RestaurantTestData.*;
import static club.beingsoft.restaurants.UserTestData.ADMIN;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class MenuControllerTest {
    private static final Logger log = LoggerFactory.getLogger(MenuControllerTest.class);
    private static MockedStatic<SecurityUtil> securityUtilMocked;
    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            System.out.println("Starting test: " + description.getMethodName());
        }
    };
    @Autowired
    private MenuController menuController;

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
    public void getAllMenus() {
        List<Menu> menusDB = menuController.getAll();
        MENU_MATCHER.assertMatch(menusDB, MENUS);
    }

    @Test
    public void getMenu() {
        Menu menuDB = menuController.getMenu(MENU_1_ID);
        MENU_MATCHER.assertMatch(menuDB, MENU_1);
    }

    @Test
    public void getMenuNullId() {
        Assert.assertThrows(ConstraintViolationException.class, () -> menuController.getMenu(null));
    }

    @Test
    public void getMenuNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> menuController.getMenu(NOT_FOUND_ID));
    }

    @Test
    public void saveNewMenu() {
        Menu newMenu = getNewMenu();
        Menu menuDB = (Menu) menuController.save(null, RESTAURANT_3_ID, newMenu).getBody();
        assert menuDB != null;
        newMenu.setId(menuDB.getId());
        MENU_MATCHER.assertMatch(menuDB, newMenu);
    }

    @Test
    public void saveNewNullMenu() {
        Assert.assertThrows(ConstraintViolationException.class, () -> menuController.save(null, RESTAURANT_3_ID, null));
    }

    @Test
    public void saveNewRestaurantNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> menuController.save(null, NOT_FOUND_ID, getNewMenu()));
    }

    @Test
    public void saveNewNullRestaurant() {
        Assert.assertThrows(ConstraintViolationException.class, () -> menuController.save(null, null, getNewMenu()));
    }

    @Test
    public void saveNewDeletedRestaurant() {
        Assert.assertThrows(EntityDeletedException.class, () -> menuController.save(null, DELETED_RESTAURANT_ID, getNewMenu()));
    }

    @Test
    public void updateMenu() {
        menuController.save(MENU_1_ID, RESTAURANT_2_ID, MENU_1);
        Menu menuDB = menuController.getMenu(MENU_1_ID);
        MENU_MATCHER.assertMatch(menuDB, UPDATED_MENU);
    }

    @Test
    public void linkDishToMenu() {
        Menu menuDB = menuController.linkDishToMenu(MENU_2_ID, DISHES_IDs).getBody();
        MENU_MATCHER.assertMatch(menuDB, MenuTestData.getLinkedMenu());
    }

    @Test
    public void linkDishToMenuNullId() {
        Assert.assertThrows(ConstraintViolationException.class, () -> menuController.linkDishToMenu(null, DISHES_IDs));
    }

    @Test
    public void linkDishToMenuNotFoundDishes() {
        Assert.assertThrows(NotFoundException.class, () -> menuController.linkDishToMenu(MENU_2_ID, List.of(NOT_FOUND_ID)));
    }

    @Test
    public void linkDishToMenuDeletedMenu() {
        Assert.assertThrows(IllegalArgumentException.class, () -> menuController.linkDishToMenu(DELETED_MENU_ID, DISHES_IDs));
    }

    @Test
    public void linkDishToMenuDeletedDish() {
        Assert.assertThrows(EntityDeletedException.class, () -> menuController.linkDishToMenu(MENU_2_ID, List.of(DELETED_DISH_ID)));
    }

    @Test
    public void unlinkDishToMenuNullId() {
        Assert.assertThrows(ConstraintViolationException.class, () -> menuController.unlinkDishFromMenu(null, DISHES_IDs));
    }

    @Test
    public void unlinkDishToMenuNotFoundDishes() {
        Assert.assertThrows(NotFoundException.class, () -> menuController.unlinkDishFromMenu(MENU_2_ID, List.of(NOT_FOUND_ID)));
    }

    @Test
    public void unlinkDishToMenuDeletedMenu() {
        Assert.assertThrows(IllegalArgumentException.class, () -> menuController.unlinkDishFromMenu(DELETED_MENU_ID, DISHES_IDs));
    }

    @Test
    public void unlinkDishToMenuDeletedDish() {
        Assert.assertThrows(EntityDeletedException.class, () -> menuController.unlinkDishFromMenu(MENU_1_ID, List.of(DELETED_DISH_ID)));
    }

    @Test
    public void unlinkDishFromMenu() {
        Menu menuDB = (Menu) menuController.unlinkDishFromMenu(MENU_2_ID, DISHES_IDs).getBody();
        MENU_MATCHER.assertMatch(menuDB, MenuTestData.getUnLinkedMenu());
    }

    @Test
    @Transactional
    public void deleteMenu() {
        menuController.delete(DELETED_MENU_ID);
        Menu menuDB = menuController.getMenu(DELETED_MENU_ID);
        MENU_MATCHER.assertMatch(menuDB, DELETED_MENU);
    }

    @Test
    public void deleteMenuNullId() {
        Assert.assertThrows(ConstraintViolationException.class, () -> menuController.delete(null));
    }

    @Test
    public void deleteMenuNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> menuController.delete(NOT_FOUND_ID));
    }
}