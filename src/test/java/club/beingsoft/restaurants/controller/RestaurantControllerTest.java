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
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static club.beingsoft.restaurants.RestaurantTestData.*;
import static club.beingsoft.restaurants.UserTestData.ADMIN;
import static club.beingsoft.restaurants.util.TestUtil.userHttpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class RestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL = RestaurantController.REST_URL + '/';
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
            log.info("Starting test: {}", description.getMethodName());
        }
    };

    @Test
    public void getAllRestaurants() {
        List<Restaurant> restaurantsDB = restaurantController.getAll();
        RESTAURANT_MATCHER.assertMatch(restaurantsDB, RESTAURANTS);
    }

    @Test
    public void getAllRestaurantsWithDishesToday() {
        List<Restaurant> restaurantsDB = restaurantController.getAllWithDishesToDate(LocalDate.now());
        RESTAURANT_MATCHER.assertMatch(restaurantsDB, RESTAURANTS_WITH_DISHES);
    }

    @Test
    public void getRestaurant() {
        Restaurant restaurantDB = restaurantController.get(RESTAURANT_1_ID);
        RESTAURANT_MATCHER.assertMatch(restaurantDB, RESTAURANT_1);
    }

    @Test
    public void saveNewRestaurant() {
        Restaurant newRestaurant = getNewRestaurant();
        Restaurant restaurantDB = restaurantController.save(null, newRestaurant).getBody();
        assert restaurantDB != null;
        newRestaurant.setId(restaurantDB.getId());
        RESTAURANT_MATCHER.assertMatch(restaurantDB, newRestaurant);
    }

    @Test
    public void updateRestaurant() {
        restaurantController.save(RESTAURANT_3_ID, UPDATED_RESTAURANT);
        Restaurant restaurantDB = restaurantController.get(RESTAURANT_3_ID);
        RESTAURANT_MATCHER.assertMatch(restaurantDB, UPDATED_RESTAURANT);
    }

    @Test
    @Transactional
    public void deleteRestaurant() {
        restaurantController.delete(DELETED_RESTAURANT_ID);
        Restaurant restaurantDB = restaurantController.get(DELETED_RESTAURANT_ID);
        RESTAURANT_MATCHER.assertMatch(restaurantDB, DELETED_RESTAURANT);
    }

    @Test
    public void getSortedByVotesRestaurants() {
        List<RestaurantWithVotesTo> restaurantDB = restaurantController.getSortedByVotes(LocalDate.now());
        Assert.assertEquals(RESTAURANTS_WITH_VOTES_TO, restaurantDB);
    }

    @Test
    public void getHierarchy() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "hierarchy?date=" + LocalDate.now())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(List.of(RESTAURANT_1, RESTAURANT_1)))
                .andReturn();
    }
}