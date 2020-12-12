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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static club.beingsoft.restaurants.DishTestData.DELETED_DISH_ID;
import static club.beingsoft.restaurants.MenuTestData.*;
import static club.beingsoft.restaurants.RestaurantTestData.*;
import static club.beingsoft.restaurants.UserTestData.ADMIN;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class MenuControllerTest {

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
        List<Menu> menusDB = menuController.getAllMenus();
        Assert.assertEquals(MENUS, menusDB);
    }

    @Test
    public void getMenu() {
        Menu menuDB = menuController.getMenu(MENU_1_ID);
        Assert.assertEquals(MENU_1, menuDB);
    }

    @Test
    public void getMenuNullId() {
        Assert.assertThrows(IllegalArgumentException.class, () -> menuController.getMenu(null));
    }

    @Test
    public void getMenuNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> menuController.getMenu(NOT_FOUND_ID));
    }

    @Test
    public void saveNewMenu() {
        Menu menuDB = (Menu) menuController.saveMenu(null, RESTAURANT_3_ID, NEW_MENU).getBody();
        assert menuDB != null;
        NEW_MENU.setId(menuDB.getId());
        Assert.assertEquals(NEW_MENU, menuDB);
    }

    @Test
    public void saveNewNullMenu() {
        Assert.assertThrows(NotFoundException.class, () -> menuController.saveMenu(null, RESTAURANT_3_ID, null));
    }

    @Test
    public void saveNewRestaurantNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> menuController.saveMenu(null, NOT_FOUND_ID, NEW_MENU));
    }

    @Test
    public void saveNewNullRestaurant() {
        Assert.assertThrows(IllegalArgumentException.class, () -> menuController.saveMenu(null, null, NEW_MENU));
    }

    @Test
    public void saveNewDeletedRestaurant() {
        Assert.assertThrows(EntityDeletedException.class, () -> menuController.saveMenu(null, DELETED_RESTAURANT_ID, NEW_MENU));
    }

    @Test
    public void updateMenu() {
        Menu menuDB = (Menu) menuController.saveMenu(MENU_1_ID, RESTAURANT_2_ID, MENU_1).getBody();
        Assert.assertEquals(UPDATED_MENU, menuDB);
    }

    @Test
    public void linkDishToMenu() {
        Menu menuDB = (Menu) menuController.linkDishToMenu(MENU_2_ID, DISHES_IDs).getBody();
        Assert.assertEquals(MenuTestData.getLinkedMenu(), menuDB);
    }

    @Test
    public void linkDishToMenuNullId() {
        Assert.assertThrows(IllegalArgumentException.class, () -> menuController.linkDishToMenu(null, DISHES_IDs));
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
        Assert.assertThrows(IllegalArgumentException.class, () -> menuController.linkDishToMenu(null, List.of(DELETED_DISH_ID)));
    }

    @Test
    public void unlinkDishToMenuNullId() {
        Assert.assertThrows(IllegalArgumentException.class, () -> menuController.unlinkDishFromMenu(null, DISHES_IDs));
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
        Assert.assertThrows(IllegalArgumentException.class, () -> menuController.unlinkDishFromMenu(null, List.of(DELETED_DISH_ID)));
    }

    @Test
    public void unlinkDishFromMenu() {
        Menu menuDB = (Menu) menuController.unlinkDishFromMenu(MENU_2_ID, DISHES_IDs).getBody();
        Assert.assertEquals(MenuTestData.getUnLinkedMenu(), menuDB);
    }

    @Test
    public void deleteMenu() {
        menuController.deleteMenu(DELETED_MENU_ID);
        Menu menuDB = menuController.getMenu(DELETED_MENU_ID);
        Assert.assertEquals(DELETED_MENU, menuDB);
    }

    @Test
    public void deleteMenuNullId() {
        Assert.assertThrows(IllegalArgumentException.class, () -> menuController.deleteMenu(null));
    }

    @Test
    public void deleteMenuNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> menuController.deleteMenu(NOT_FOUND_ID));
    }
}