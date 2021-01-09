package club.beingsoft.restaurants;

import club.beingsoft.restaurants.model.Vote;
import club.beingsoft.restaurants.util.TestMatcher;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static club.beingsoft.restaurants.RestaurantTestData.*;
import static club.beingsoft.restaurants.UserTestData.USER_1;
import static club.beingsoft.restaurants.model.AbstractBaseEntity.START_SEQ;

public class VoteTestData {
    public static TestMatcher<Vote> VOTE_MATCHER = TestMatcher.usingFieldsWithIgnoringAssertions(Vote.class, "user", "restaurant", "editDate", "deleteDate");
    public static final int VOTE_1_ID = START_SEQ + 13;
    public static final int VOTE_2_ID = START_SEQ + 14;
    public static final int VOTE_3_ID = START_SEQ + 15;
    public static final int NOT_FOUND_ID = 10;
    public static final Vote VOTE_1 = new Vote(VOTE_1_ID, RESTAURANT_1, LocalDate.now().minus(1, ChronoUnit.DAYS));
    public static final Vote VOTE_2 = new Vote(VOTE_2_ID, RESTAURANT_1, LocalDate.now());
    public static final Vote VOTE_3 = new Vote(VOTE_3_ID, RESTAURANT_2, LocalDate.now());

    public static final List<Vote> VOTES = List.of(VOTE_1, VOTE_3);
    public static final List<Vote> VOTES_REST_1 = List.of(VOTE_1);

    static {
        VOTE_2.setUserForTest(USER_1);
    }

    public static Vote getNewVote() {
        return new Vote(null, RESTAURANT_3, LocalDate.now());
    }
}
