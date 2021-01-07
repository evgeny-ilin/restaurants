package club.beingsoft.restaurants;

import club.beingsoft.restaurants.model.Restaurant;
import club.beingsoft.restaurants.to.RestaurantWithVotesTo;

import java.math.BigInteger;
import java.util.List;

import static club.beingsoft.restaurants.UserTestData.ADMIN;
import static club.beingsoft.restaurants.model.AbstractBaseEntity.START_SEQ;

public class RestaurantTestData {

    public static final int RESTAURANT_1_ID = START_SEQ + 2;
    public static final int RESTAURANT_2_ID = START_SEQ + 3;
    public static final int RESTAURANT_3_ID = START_SEQ + 4;
    public static final int DELETED_RESTAURANT_ID = START_SEQ + 5;

    public static final Restaurant RESTAURANT_1 = new Restaurant(RESTAURANT_1_ID, "RESTAURANT-TEST-1");
    public static final Restaurant RESTAURANT_2 = new Restaurant(RESTAURANT_2_ID, "RESTAURANT-TEST-2");
    public static final Restaurant RESTAURANT_3 = new Restaurant(RESTAURANT_3_ID, "RESTAURANT-TEST-3");
    public static final Restaurant UPDATED_RESTAURANT = new Restaurant(RESTAURANT_3_ID, "RESTAURANT-UPDATED");
    public static final Restaurant DELETED_RESTAURANT = new Restaurant(DELETED_RESTAURANT_ID, "DELETED-RESTAURANT-TEST");

    public static final List<Restaurant> RESTAURANTS = List.of(RESTAURANT_1, RESTAURANT_2, RESTAURANT_3, DELETED_RESTAURANT);
    public static final List<Restaurant> RESTAURANTS_WITH_DISHES = List.of(RESTAURANT_1);
    public static final List<RestaurantWithVotesTo> RESTAURANTS_WITH_VOTES_TO = List.of(
            new RestaurantWithVotesTo(RESTAURANT_1_ID, RESTAURANT_1.getName(), BigInteger.ONE),
            new RestaurantWithVotesTo(RESTAURANT_2_ID, RESTAURANT_2.getName(), BigInteger.ONE));

    static {
        UPDATED_RESTAURANT.setUser(ADMIN);
        DELETED_RESTAURANT.delete(ADMIN);
    }

    public static Restaurant getNewRestaurant() {
        return new Restaurant(null, "RESTAURANT-TEST-NEW");
    }
}
