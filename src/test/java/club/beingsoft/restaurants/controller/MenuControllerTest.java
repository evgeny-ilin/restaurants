package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.MenuTestData;
import club.beingsoft.restaurants.model.Menu;
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

import static club.beingsoft.restaurants.MenuTestData.*;
import static club.beingsoft.restaurants.RestaurantTestData.DELETED_RESTAURANT_ID;
import static club.beingsoft.restaurants.RestaurantTestData.RESTAURANT_2_ID;
import static club.beingsoft.restaurants.UserTestData.ADMIN;
import static club.beingsoft.restaurants.UserTestData.USER;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class MenuControllerTest {

    @Autowired
    private MenuController menuController;

    @Before
    public void before() {
        SecurityUtil.setAuthUser(ADMIN);
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
    public void saveNewMenuAdmin() {
        SecurityUtil.setAuthUser(ADMIN);
        Menu menuDB = (Menu) menuController.saveMenu(null, DELETED_RESTAURANT_ID, NEW_MENU).getBody();
        NEW_MENU.setId(menuDB.getId());
        Assert.assertEquals(NEW_MENU, menuDB);
    }

    @Test
    public void updateMenuAdmin() {
        SecurityUtil.setAuthUser(ADMIN);
        Menu menuDB = (Menu) menuController.saveMenu(MENU_1_ID, DELETED_RESTAURANT_ID, MENU_1).getBody();
        Assert.assertEquals(UPDATED_MENU, menuDB);
    }

    @Test
    public void saveNewMenuUser() {
        SecurityUtil.setAuthUser(USER);
        Assert.assertThrows(PermissionException.class, () -> menuController.saveMenu(null, RESTAURANT_2_ID, NEW_MENU).getBody());
    }

    @Test
    public void linkDishToMenuAdmin() {
        SecurityUtil.setAuthUser(ADMIN);
        Menu menuDB = (Menu) menuController.linkDishToMenu(MENU_2_ID, DISHES_IDs).getBody();
        Assert.assertEquals(MenuTestData.getLinkedMenu(), menuDB);
    }

    @Test
    public void linkDishToMenuUser() {
        SecurityUtil.setAuthUser(USER);
        Assert.assertThrows(PermissionException.class, () -> menuController.linkDishToMenu(MENU_2_ID, DISHES_IDs));
    }

    @Test
    public void unlinkDishFromMenuAdmin() {
        SecurityUtil.setAuthUser(ADMIN);
        Menu menuDB = (Menu) menuController.unlinkDishFromMenu(MENU_2_ID, DISHES_IDs).getBody();
        Assert.assertEquals(MenuTestData.getUnLinkedMenu(), menuDB);
    }

    @Test
    public void unlinkDishFromMenuUser() {
        SecurityUtil.setAuthUser(USER);
        Assert.assertThrows(PermissionException.class, () -> menuController.unlinkDishFromMenu(MENU_2_ID, DISHES_IDs));
    }

    @Test
    public void deleteMenuAdmin() {
        SecurityUtil.setAuthUser(ADMIN);
        menuController.deleteMenu(DELETED_MENU_ID);
        Menu menuDB = menuController.getMenu(DELETED_MENU_ID);
        Assert.assertEquals(DELETED_MENU, menuDB);
    }

    @Test
    public void deleteMenuUser() {
        SecurityUtil.setAuthUser(USER);
        Assert.assertThrows(PermissionException.class, () -> menuController.deleteMenu(DELETED_MENU_ID));
    }
}