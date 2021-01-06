package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.model.Restaurant;
import club.beingsoft.restaurants.model.User;
import club.beingsoft.restaurants.to.RestaurantWithVotesTo;
import club.beingsoft.restaurants.util.SecurityUtil;
import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

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

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            System.out.println("Starting test: " + description.getMethodName());
        }
    };

    @Test
    public void getAllRestaurants() {
        List<Restaurant> restaurantsDB = restaurantController.getAll();
        Assert.assertEquals(RESTAURANTS, restaurantsDB);
    }

    @Test
    public void getAllRestaurantsWithDishesToday() {
        List<Restaurant> restaurantsDB = restaurantController.getAllWithDishesToDate(LocalDate.now());
        Assert.assertEquals(RESTAURANTS_WITH_DISHES, restaurantsDB);
    }

    @Test
    public void getRestaurant() {
        Restaurant restaurantDB = restaurantController.get(RESTAURANT_1_ID);
        Assert.assertEquals(RESTAURANT_1, restaurantDB);
    }

    @Test
    public void saveNewRestaurant() {
        Restaurant newRestaurant = getNewRestaurant();
        Restaurant restaurantDB = (Restaurant) restaurantController.save(null, newRestaurant).getBody();
        assert restaurantDB != null;
        newRestaurant.setId(restaurantDB.getId());
        Assert.assertEquals(newRestaurant, restaurantDB);
    }

    @Test
    public void updateRestaurant() {
        restaurantController.save(RESTAURANT_3_ID, UPDATED_RESTAURANT);
        Restaurant restaurantDB = restaurantController.get(RESTAURANT_3_ID);
        Assert.assertEquals(UPDATED_RESTAURANT, restaurantDB);
    }

    @Test
    public void deleteRestaurant() {
        restaurantController.delete(DELETED_RESTAURANT_ID);
        Restaurant restaurantDB = restaurantController.get(DELETED_RESTAURANT_ID);
        Assert.assertEquals(DELETED_RESTAURANT, restaurantDB);
    }


    @Test
    public void getSortedByVotesRestaurants() {
        List<RestaurantWithVotesTo> restaurantDB = restaurantController.getSortedByVotes(LocalDate.now());
        Assert.assertEquals(RESTAURANTS_WITH_VOTES_TO, restaurantDB);
    }
}