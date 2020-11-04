package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.RestaurantTestData;
import club.beingsoft.restaurants.model.Restaurant;
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

import static club.beingsoft.restaurants.RestaurantTestData.*;
import static club.beingsoft.restaurants.UserTestData.USER_ADMIN;
import static club.beingsoft.restaurants.UserTestData.USER_USER;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class RestaurantControllerTest {

    @Autowired
    private RestaurantController restaurantController;

    @Before
    public void before() {
        SecurityUtil.setAuthUser(USER_ADMIN);
    }

    @Test
    public void getAllRestaurants() {
        List<Restaurant> restaurantsDB = restaurantController.getAllRestaurants();
        Assert.assertEquals(RESTAURANTS, restaurantsDB);
    }

    @Test
    public void getRestaurant() {
        Restaurant restaurantDB = restaurantController.getRestaurant(RESTAURANT_1_ID);
        Assert.assertEquals(RESTAURANT_1, restaurantDB);
    }

    @Test
    public void saveNewRestaurantAdmin() {
        SecurityUtil.setAuthUser(USER_ADMIN);
        Restaurant restaurantDB = (Restaurant) restaurantController.saveRestaurant(null, NEW_RESTAURANT).getBody();
        NEW_RESTAURANT.setId(restaurantDB.getId());
        Assert.assertEquals(NEW_RESTAURANT, restaurantDB);
    }

    @Test
    public void saveRestaurantUser() {
        SecurityUtil.setAuthUser(USER_USER);
        Assert.assertThrows(PermissionException.class, () -> restaurantController.saveRestaurant(null, NEW_RESTAURANT).getBody());
    }


    @Test
    public void updateRestaurantAdmin() {
        SecurityUtil.setAuthUser(USER_ADMIN);
        Restaurant restaurantDB = (Restaurant) restaurantController.saveRestaurant(RESTAURANT_3_ID, RestaurantTestData.getUpdated()).getBody();
        Assert.assertEquals(RestaurantTestData.getUpdated(), restaurantDB);
    }

    @Test
    public void deleteRestaurantAdmin() {
        SecurityUtil.setAuthUser(USER_ADMIN);
        restaurantController.deleteRestaurant(DELETED_RESTAURANT_ID);
        Restaurant restaurantDB = restaurantController.getRestaurant(DELETED_RESTAURANT_ID);
        Assert.assertEquals(DELETED_RESTAURANT, restaurantDB);
    }

    @Test
    public void deleteRestaurantUser() {
        SecurityUtil.setAuthUser(USER_USER);
        Assert.assertThrows(PermissionException.class, () -> restaurantController.deleteRestaurant(DELETED_RESTAURANT_ID));
    }
}