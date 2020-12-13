package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.MenuTestData;
import club.beingsoft.restaurants.model.Menu;
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

import static club.beingsoft.restaurants.DishTestData.DELETED_DISH_ID;
import static club.beingsoft.restaurants.MenuTestData.*;
import static club.beingsoft.restaurants.RestaurantTestData.*;
import static club.beingsoft.restaurants.UserTestData.ADMIN;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class MenuControllerTest {
    private static final Logger log = LoggerFactory.getLogger(MenuControllerTest.class);

    @Autowired
    private MenuController menuController;

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
    public void getAllMenus() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        List<Menu> menusDB = menuController.getAllMenus();
        Assert.assertEquals(MENUS, menusDB);
    }

    @Test
    public void getMenu() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Menu menuDB = menuController.getMenu(MENU_1_ID);
        Assert.assertEquals(MENU_1, menuDB);
    }

    @Test
    public void getMenuNullId() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Assert.assertThrows(ConstraintViolationException.class, () -> menuController.getMenu(null));
    }

    @Test
    public void getMenuNotFound() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Assert.assertThrows(NotFoundException.class, () -> menuController.getMenu(NOT_FOUND_ID));
    }

    @Test
    public void saveNewMenu() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Menu newMenu = getNewMenu();
        Menu menuDB = (Menu) menuController.saveMenu(null, RESTAURANT_3_ID, newMenu).getBody();
        assert menuDB != null;
        newMenu.setId(menuDB.getId());
        Assert.assertEquals(newMenu, menuDB);
    }

    @Test
    public void saveNewNullMenu() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Assert.assertThrows(ConstraintViolationException.class, () -> menuController.saveMenu(null, RESTAURANT_3_ID, null));
    }

    @Test
    public void saveNewRestaurantNotFound() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Assert.assertThrows(NotFoundException.class, () -> menuController.saveMenu(null, NOT_FOUND_ID, getNewMenu()));
    }

    @Test
    public void saveNewNullRestaurant() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Assert.assertThrows(ConstraintViolationException.class, () -> menuController.saveMenu(null, null, getNewMenu()));
    }

    @Test
    public void saveNewDeletedRestaurant() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Assert.assertThrows(EntityDeletedException.class, () -> menuController.saveMenu(null, DELETED_RESTAURANT_ID, getNewMenu()));
    }

    @Test
    public void updateMenu() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Menu menuDB = (Menu) menuController.saveMenu(MENU_1_ID, RESTAURANT_2_ID, MENU_1).getBody();
        Assert.assertEquals(UPDATED_MENU, menuDB);
    }

    @Test
    public void linkDishToMenu() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Menu menuDB = (Menu) menuController.linkDishToMenu(MENU_2_ID, DISHES_IDs).getBody();
        Assert.assertEquals(MenuTestData.getLinkedMenu(), menuDB);
    }

    @Test
    public void linkDishToMenuNullId() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Assert.assertThrows(ConstraintViolationException.class, () -> menuController.linkDishToMenu(null, DISHES_IDs));
    }

    @Test
    public void linkDishToMenuNotFoundDishes() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Assert.assertThrows(NotFoundException.class, () -> menuController.linkDishToMenu(MENU_2_ID, List.of(NOT_FOUND_ID)));
    }

    @Test
    public void linkDishToMenuDeletedMenu() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Assert.assertThrows(IllegalArgumentException.class, () -> menuController.linkDishToMenu(DELETED_MENU_ID, DISHES_IDs));
    }

    @Test
    public void linkDishToMenuDeletedDish() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Assert.assertThrows(EntityDeletedException.class, () -> menuController.linkDishToMenu(MENU_2_ID, List.of(DELETED_DISH_ID)));
    }

    @Test
    public void unlinkDishToMenuNullId() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Assert.assertThrows(ConstraintViolationException.class, () -> menuController.unlinkDishFromMenu(null, DISHES_IDs));
    }

    @Test
    public void unlinkDishToMenuNotFoundDishes() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Assert.assertThrows(NotFoundException.class, () -> menuController.unlinkDishFromMenu(MENU_2_ID, List.of(NOT_FOUND_ID)));
    }

    @Test
    public void unlinkDishToMenuDeletedMenu() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Assert.assertThrows(IllegalArgumentException.class, () -> menuController.unlinkDishFromMenu(DELETED_MENU_ID, DISHES_IDs));
    }

    @Test
    public void unlinkDishToMenuDeletedDish() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Assert.assertThrows(EntityDeletedException.class, () -> menuController.unlinkDishFromMenu(MENU_1_ID, List.of(DELETED_DISH_ID)));
    }

    @Test
    public void unlinkDishFromMenu() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Menu menuDB = (Menu) menuController.unlinkDishFromMenu(MENU_2_ID, DISHES_IDs).getBody();
        Assert.assertEquals(MenuTestData.getUnLinkedMenu(), menuDB);
    }

    @Test
    public void deleteMenu() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        menuController.deleteMenu(DELETED_MENU_ID);
        Menu menuDB = menuController.getMenu(DELETED_MENU_ID);
        Assert.assertEquals(DELETED_MENU, menuDB);
    }

    @Test
    public void deleteMenuNullId() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Assert.assertThrows(ConstraintViolationException.class, () -> menuController.deleteMenu(null));
    }

    @Test
    public void deleteMenuNotFound() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Assert.assertThrows(NotFoundException.class, () -> menuController.deleteMenu(NOT_FOUND_ID));
    }
}