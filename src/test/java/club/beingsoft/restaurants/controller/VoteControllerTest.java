package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.model.User;
import club.beingsoft.restaurants.model.Vote;
import club.beingsoft.restaurants.util.SecurityUtil;

import static club.beingsoft.restaurants.UserTestData.USER_2;
import static club.beingsoft.restaurants.util.ValidationUtil.*;

import club.beingsoft.restaurants.util.exception.NotFoundException;
import club.beingsoft.restaurants.util.exception.VoteCantBeChangedException;
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

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static club.beingsoft.restaurants.RestaurantTestData.*;
import static club.beingsoft.restaurants.UserTestData.ADMIN;
import static club.beingsoft.restaurants.VoteTestData.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class VoteControllerTest {
    private static final Logger log = LoggerFactory.getLogger(VoteControllerTest.class);
    private static MockedStatic<SecurityUtil> securityUtilMocked;
    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            System.out.println("Starting test: " + description.getMethodName());
        }
    };
    @Autowired
    private VoteController voteController;

//    @BeforeClass
//    public static void beforeAll() {
//        securityUtilMocked = Mockito.mockStatic(SecurityUtil.class);
//        User user = ADMIN;
//        securityUtilMocked.when(SecurityUtil::getAuthUser).thenReturn(user);
//    }
//
//    @AfterClass
//    public static void close() {
//        securityUtilMocked.close();
//    }

    @Before
    public void beforeTest() {
        setDeadLine(LocalDate.now().atTime(11,0));
        securityUtilMocked = Mockito.mockStatic(SecurityUtil.class);
        User user = ADMIN;
        securityUtilMocked.when(SecurityUtil::getAuthUser).thenReturn(user);
    }

    @After
    public void afterTest() {
        securityUtilMocked.close();
    }

    @Test
    public void getAllVotes() {
        List<Vote> votesDB = voteController.getAll();
        Assert.assertEquals(VOTES, votesDB);
    }

    @Test
    public void getAllVotesByRestaurant() {
        List<Vote> votesDB = voteController.getAllByRestaurant(RESTAURANT_1_ID);
        Assert.assertEquals(VOTES_REST_1, votesDB);
    }

    @Test
    public void getVote() {
        Vote voteDB = voteController.get(VOTE_1_ID);
        Assert.assertEquals(VOTE_1, voteDB);
    }

    @Test
    public void createVote() {
        securityUtilMocked.close();
        securityUtilMocked = Mockito.mockStatic(SecurityUtil.class);
        User user = USER_2;
        securityUtilMocked.when(SecurityUtil::getAuthUser).thenReturn(user);
        setDeadLine(LocalDateTime.now().plus(1, ChronoUnit.HOURS));
        Vote voteDB = voteController.save(RESTAURANT_3_ID).getBody();
        Vote newVote = getNewVote();
        newVote.setId(voteDB.getId());
        Assert.assertEquals(newVote, voteDB);
    }

    @Test
    public void updateVote() {
        setDeadLine(LocalDateTime.now().plus(1, ChronoUnit.HOURS));
        voteController.save(RESTAURANT_2_ID);
        Vote voteDB = voteController.get(100015);
        Vote newVote = getNewVote();
        newVote.setId(voteDB.getId());
        newVote.setRestaurant(RESTAURANT_2);
        Assert.assertEquals(newVote, voteDB);
    }

    @Test
    public void saveVoteRestaurantNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> voteController.save(NOT_FOUND_ID).getBody());
    }

    @Test
    public void getVoteNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> voteController.get(NOT_FOUND_ID));
    }

    @Test
    public void getVoteWithIdNull() {
        Assert.assertThrows(ConstraintViolationException.class, () -> voteController.get(null));
    }

    @Test
    public void saveVoteAfterDeadLine() {
        setDeadLine(LocalDateTime.now().minus(1, ChronoUnit.HOURS));
        Assert.assertThrows(VoteCantBeChangedException.class, () -> voteController.save(RESTAURANT_2_ID));
    }
}