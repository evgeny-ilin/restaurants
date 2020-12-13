package club.beingsoft.restaurants;

import club.beingsoft.restaurants.model.Vote;

import java.time.LocalDate;
import java.util.List;

import static club.beingsoft.restaurants.RestaurantTestData.*;
import static club.beingsoft.restaurants.model.AbstractBaseEntity.START_SEQ;

public class VoteTestData {
    public static final int VOTE_1_ID = START_SEQ + 13;
    public static final int VOTE_2_ID = START_SEQ + 14;
    public static final int NOT_FOUND_ID = 10;
    public static final Vote VOTE_1 = new Vote(VOTE_1_ID, RESTAURANT_1, LocalDate.now());
    public static final Vote VOTE_2 = new Vote(VOTE_2_ID, RESTAURANT_2, LocalDate.now());
    public static final Vote NEW_VOTE = new Vote(null, RESTAURANT_3, LocalDate.now());
    public static final List<Vote> VOTES = List.of(VOTE_1, VOTE_2);
    public static final List<Vote> VOTES_REST_1 = List.of(VOTE_1);
}
