package club.beingsoft.restaurants.to;

import lombok.Value;

@Value
public class RestaurantWithVotesTo {
    Integer id;
    String name;
    Long vcount;
}
