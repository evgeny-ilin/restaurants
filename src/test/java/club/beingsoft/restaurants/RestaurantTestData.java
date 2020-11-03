package club.beingsoft.restaurants;

import club.beingsoft.restaurants.model.Restaurant;

import java.util.List;

import static club.beingsoft.restaurants.model.AbstractBaseEntity.START_SEQ;

public class RestaurantTestData {

    public static final int RESTAURANT_1_ID = START_SEQ + 2;
    public static final int RESTAURANT_2_ID = START_SEQ + 3;
    public static final int RESTAURANT_3_ID = START_SEQ + 4;
    public static final int DELETED_RESTAURANT_ID = START_SEQ + 5;

    public static final Restaurant RESTAURANT_1 = new Restaurant(RESTAURANT_1_ID, "RESTAURANT-TEST-1");
    public static final Restaurant RESTAURANT_2 = new Restaurant(RESTAURANT_2_ID, "RESTAURANT-TEST-2");
    public static final Restaurant RESTAURANT_3 = new Restaurant(RESTAURANT_3_ID, "RESTAURANT-TEST-3");
    public static final Restaurant DELETED_RESTAURANT = new Restaurant(DELETED_RESTAURANT_ID, "RESTAURANT-TEST-4");
    public static final Restaurant NEW_RESTAURANT = new Restaurant(null, "RESTAURANT-TEST-NEW");

    public static final List<Restaurant> RESTAURANTS = List.of(RESTAURANT_1, RESTAURANT_2, RESTAURANT_3, DELETED_RESTAURANT);

    static {
        //SecurityUtil.setAuthUser(USER_ADMIN);
        RESTAURANT_1.setUser();
        RESTAURANT_2.setUser();
        RESTAURANT_3.setUser();
        DELETED_RESTAURANT.setUser();
        DELETED_RESTAURANT.delete();
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT_3_ID, "RESTAURANT-UPDATED");
    }
}
