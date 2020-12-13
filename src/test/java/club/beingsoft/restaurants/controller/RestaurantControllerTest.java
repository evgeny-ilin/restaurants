package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.model.Restaurant;
import club.beingsoft.restaurants.model.User;
import club.beingsoft.restaurants.to.RestaurantWithVotesTo;
import club.beingsoft.restaurants.util.SecurityUtil;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Locale;

import static club.beingsoft.restaurants.RestaurantTestData.*;
import static club.beingsoft.restaurants.UserTestData.ADMIN;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class RestaurantControllerTest {

    private static final Logger log = LoggerFactory.getLogger(RestaurantControllerTest.class);

    private static MockedStatic<SecurityUtil> securityUtilMocked;
    @Autowired
    private RestaurantController restaurantController;

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
    public void getAllRestaurants() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        List<Restaurant> restaurantsDB = restaurantController.getAllRestaurants();
        Assert.assertEquals(RESTAURANTS, restaurantsDB);
    }

    @Test
    public void getAllRestaurantsWithDishesToday() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        List<Restaurant> restaurantsDB = restaurantController.getAllRestaurantsWithDishesToday();
        Assert.assertEquals(RESTAURANTS_WITH_DISHES, restaurantsDB);
    }

    @Test
    public void getRestaurant() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Restaurant restaurantDB = restaurantController.getRestaurant(RESTAURANT_1_ID);
        Assert.assertEquals(RESTAURANT_1, restaurantDB);
    }

    @Test
    public void saveNewRestaurant() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Restaurant newRestaurant = getNewRestaurant();
        Restaurant restaurantDB = (Restaurant) restaurantController.saveRestaurant(null, newRestaurant).getBody();
        assert restaurantDB != null;
        newRestaurant.setId(restaurantDB.getId());
        Assert.assertEquals(newRestaurant, restaurantDB);
    }

    @Test
    public void updateRestaurant() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        Restaurant restaurantDB = (Restaurant) restaurantController.saveRestaurant(RESTAURANT_3_ID, UPDATED_RESTAURANT).getBody();
        Assert.assertEquals(UPDATED_RESTAURANT, restaurantDB);
    }

    @Test
    public void deleteRestaurant() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        restaurantController.deleteRestaurant(DELETED_RESTAURANT_ID);
        Restaurant restaurantDB = restaurantController.getRestaurant(DELETED_RESTAURANT_ID);
        Assert.assertEquals(DELETED_RESTAURANT, restaurantDB);
    }


    @Test
    public void getSortedByVotesRestaurants() {
        log.info(new Throwable().getStackTrace()[0].getMethodName().toUpperCase(Locale.ROOT));
        List<RestaurantWithVotesTo> restaurantDB = restaurantController.getSortedByVotesRestaurants();
        Assert.assertEquals(RESTAURANTS_WITH_VOTES_TO, restaurantDB);
    }
}