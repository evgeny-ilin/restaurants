package club.beingsoft.restaurants;

import club.beingsoft.restaurants.model.Dish;
import club.beingsoft.restaurants.model.Menu;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static club.beingsoft.restaurants.DishTestData.*;
import static club.beingsoft.restaurants.RestaurantTestData.*;
import static club.beingsoft.restaurants.model.AbstractBaseEntity.START_SEQ;

public class MenuTestData {
    public static final List<Integer> DISHES_IDs = List.of(DISH_1_ID, DISH_2_ID);
    public static int MENU_1_ID = START_SEQ + 6;
    public static int MENU_2_ID = START_SEQ + 7;
    public static int DELETED_MENU_ID = START_SEQ + 8;
    public static final int NOT_FOUND_ID = 10;
    public static Menu MENU_1 = new Menu(MENU_1_ID, LocalDate.now(), RESTAURANT_1);
    public static Menu MENU_2 = new Menu(MENU_2_ID, LocalDate.now(), RESTAURANT_2);
    public static Menu UPDATED_MENU = new Menu(MENU_1_ID, LocalDate.now(), DELETED_RESTAURANT);
    public static Menu DELETED_MENU = new Menu(DELETED_MENU_ID, LocalDate.now(), DELETED_RESTAURANT);
    public static final List<Menu> MENUS = List.of(MENU_1, MENU_2, DELETED_MENU);

    static {
        Field menuDateField;
        try {
            menuDateField = MENU_2.getClass().getDeclaredField("menuDate");
            menuDateField.setAccessible(true);
            LocalDate date = LocalDate.now().minusDays(1);
            menuDateField.set(MENU_2, date);
            menuDateField.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        DELETED_MENU.delete();
    }

    public static Menu getLinkedMenu() {
        Set<Dish> dishes = Set.of(DISH_1, DISH_2);
        MENU_2.setDishes(dishes);
        return MENU_2;
    }

    public static Menu getUnLinkedMenu() {
        MENU_2.setDishes(null);
        return MENU_2;
    }

    public static Menu getNewMenu() {
        return new Menu(null, LocalDate.now().plusDays(1), RESTAURANT_2);
    }
}
