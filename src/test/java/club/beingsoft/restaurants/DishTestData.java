package club.beingsoft.restaurants;

import club.beingsoft.restaurants.model.Dish;

import java.math.BigDecimal;
import java.util.List;

import static club.beingsoft.restaurants.MenuTestData.MENU_1;
import static club.beingsoft.restaurants.model.AbstractBaseEntity.START_SEQ;

public class DishTestData {
    public static final int DISH_1_ID = START_SEQ + 9;
    public static final int DISH_2_ID = START_SEQ + 10;
    public static final int DISH_3_ID = START_SEQ + 11;
    public static final int DELETED_DISH_ID = START_SEQ + 12;
    public static final int NOT_FOUND_ID = 10;
    public static final Dish DISH_1 = new Dish(DISH_1_ID, MENU_1, "DISH-TEST-1", new BigDecimal("1.10"));
    public static final Dish DISH_2 = new Dish(DISH_2_ID, MENU_1, "DISH-TEST-2", new BigDecimal("2.20"));
    public static final Dish DISH_3 = new Dish(DISH_3_ID, null, "DISH-TEST-3", new BigDecimal("3.30"));
    public static final Dish UPDATED_DISH = new Dish(DISH_3_ID, MENU_1, "DISH-UPDATE-TEST", new BigDecimal("0.00"));
    public static final Dish DELETED_DISH = new Dish(DELETED_DISH_ID, MENU_1, "DISH-DELETE-TEST", new BigDecimal("5.50"));
    public static final List<Dish> DISHES = List.of(DISH_1, DISH_2, DISH_3, DELETED_DISH);

    public static Dish getNewDish() {
        return new Dish(null, null, "DISH-NEW-TEST", new BigDecimal(4.40));
    }
}
