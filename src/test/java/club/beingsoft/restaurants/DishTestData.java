package club.beingsoft.restaurants;

import club.beingsoft.restaurants.model.Dish;

import java.math.BigDecimal;

import static club.beingsoft.restaurants.MenuTestData.MENU_1;
import static club.beingsoft.restaurants.model.AbstractBaseEntity.START_SEQ;

public class DishTestData {
    public static final int DISH_1_ID = START_SEQ + 8;
    public static final int DISH_2_ID = START_SEQ + 9;
    public static final int DISH_3_ID = START_SEQ + 10;
    public static final int DISH_4_ID = START_SEQ + 11;
    public static final int DELETED_DISH_ID = START_SEQ + 12;
    public static final int NOT_FOUND = 10;
    public static final Dish DISH_1 = new Dish(DISH_1_ID, MENU_1, "DISH-1-TEST", BigDecimal.valueOf(1.00));
    public static final Dish DISH_2 = new Dish(DISH_2_ID, MENU_1, "DISH-2-TEST", BigDecimal.valueOf(2.00));

    public static Dish getUpdated() {
        return new Dish(DISH_4_ID, MENU_1, "DISH-UPDATE-TEST", BigDecimal.valueOf(0.00));
    }
}
