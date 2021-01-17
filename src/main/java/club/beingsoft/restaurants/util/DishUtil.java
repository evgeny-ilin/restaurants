package club.beingsoft.restaurants.util;

import club.beingsoft.restaurants.model.Dish;
import club.beingsoft.restaurants.to.DishTo;

public class DishUtil {
    private DishUtil() {
    }

    public static Dish createNewFromTo(DishTo dishTo) {
        return new Dish(dishTo.getId(), dishTo.getMenus(), dishTo.getName(), dishTo.getPrice());
    }

    public static DishTo asTo(Dish dish) {
        return new DishTo(dish.getId(), dish.getName(), dish.getPrice(), dish.getMenus());
    }

    public static Dish updateFromTo(Dish dish, DishTo dishTo) {
        if (dishTo.getName() != null) {
            dish.setName(dishTo.getName());
        }

        if (dishTo.getMenus() != null) {
            dish.setMenus(dishTo.getMenus());
        }

        if (dishTo.getPrice() != null) {
            dish.setPrice(dishTo.getPrice());
        }

        return dish;
    }
}
