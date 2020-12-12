package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.model.User;
import club.beingsoft.restaurants.model.Vote;
import club.beingsoft.restaurants.util.SecurityUtil;
import club.beingsoft.restaurants.util.exception.NotFoundException;
import club.beingsoft.restaurants.util.exception.VoteCantBeChangedException;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static club.beingsoft.restaurants.RestaurantTestData.RESTAURANT_2_ID;
import static club.beingsoft.restaurants.RestaurantTestData.RESTAURANT_3_ID;
import static club.beingsoft.restaurants.UserTestData.ADMIN;
import static club.beingsoft.restaurants.VoteTestData.*;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class VoteControllerTest {
    // Some fixed date to make your tests
    private final static LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).plusHours(13);

    @Autowired
    @InjectMocks
    private VoteController voteController;

    @Mock
    private Clock clock;

    private Clock fixedClock;

    private static MockedStatic<SecurityUtil> securityUtilMocked;

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

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        //tell your tests to return the specified LOCAL_DATE when calling LocalDate.now(clock)
        fixedClock = Clock.fixed(LOCAL_DATE_TIME.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();
    }

    @Test
    public void getAllVotes() {
        List<Vote> votesDB = voteController.getAllVotes();
        Assert.assertEquals(VOTES, votesDB);
    }

    @Test
    public void getVote() {
        Vote voteDB = voteController.getVote(VOTE_1_ID);
        Assert.assertEquals(VOTE_1, voteDB);
    }

    @Test
    public void saveVote() {
        Vote voteDB = (Vote) voteController.saveVote(RESTAURANT_3_ID).getBody();
        NEW_VOTE.setId(voteDB.getId());
        Assert.assertEquals(NEW_VOTE, voteDB);
    }

    @Test
    public void saveVoteRestaurantNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> voteController.saveVote(NOT_FOUND_ID).getBody());
    }

    @Test
    public void getVoteNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> voteController.getVote(NOT_FOUND_ID));
    }

    @Test
    public void getVoteWithIdNull() {
        Assert.assertThrows(IllegalArgumentException.class, () -> voteController.getVote(null));
    }

    @Test
    public void saveVoteAfterDeadLine() {
        Assert.assertThrows(VoteCantBeChangedException.class, () -> voteController.saveVote(RESTAURANT_2_ID));
    }
}