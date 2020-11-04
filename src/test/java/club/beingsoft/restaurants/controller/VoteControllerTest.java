package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.model.Vote;
import club.beingsoft.restaurants.util.SecurityUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static club.beingsoft.restaurants.RestaurantTestData.RESTAURANT_3_ID;
import static club.beingsoft.restaurants.UserTestData.USER;
import static club.beingsoft.restaurants.VoteTestData.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class VoteControllerTest {

    @Autowired
    private VoteController voteController;

    @Before
    public void before() {
        SecurityUtil.setAuthUser(USER);
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
}