package club.beingsoft.restaurants;

import club.beingsoft.restaurants.model.Dish;
import club.beingsoft.restaurants.model.Menu;
import club.beingsoft.restaurants.to.DishTo;
import club.beingsoft.restaurants.util.TestMatcher;

import java.math.BigDecimal;
import java.util.List;

import static club.beingsoft.restaurants.MenuTestData.MENU_1;
import static club.beingsoft.restaurants.UserTestData.ADMIN;
import static club.beingsoft.restaurants.model.AbstractBaseEntity.START_SEQ;
import static club.beingsoft.restaurants.util.DishUtil.asTo;

public class DishTestData {

    public static final int DISH_1_ID = START_SEQ + 9;
    public static final int DISH_2_ID = START_SEQ + 10;
    public static final int DISH_3_ID = START_SEQ + 11;
    public static final int DELETED_DISH_ID = START_SEQ + 12;
    public static final int NOT_FOUND_ID = 10;
    public static final Dish DISH_1 = new Dish(DISH_1_ID, MENU_1, "DISH-TEST-1", new BigDecimal("1.10"));
    public static final Dish DISH_2 = new Dish(DISH_2_ID, MENU_1, "DISH-TEST-2", new BigDecimal("2.20"));
    public static final Dish DISH_3 = new Dish(DISH_3_ID, (Menu) null, "DISH-TEST-3", new BigDecimal("3.30"));
    public static final Dish DELETED_DISH = new Dish(DELETED_DISH_ID, MENU_1, "DISH-DELETE-TEST", new BigDecimal("4.40"));
    public static final Dish UPDATED_DISH = new Dish(DISH_3_ID, MENU_1, "DISH-UPDATE-TEST", new BigDecimal("0.00"));
    public static TestMatcher<Dish> DISH_MATCHER = TestMatcher.usingFieldsWithIgnoringAssertions(Dish.class, "user", "menus", "editDate", "deleteDate");
    public static final List<Dish> DISHES = List.of(DISH_1, DISH_2, DISH_3, DELETED_DISH);

    static {
        DELETED_DISH.delete(ADMIN);
    }

    public static DishTo getNewDish() {
        return asTo(new Dish(null, (Menu) null, "DISH-NEW-TEST", new BigDecimal("4.40")));
    }
}
