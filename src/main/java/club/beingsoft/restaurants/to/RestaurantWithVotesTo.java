package club.beingsoft.restaurants.to;

import lombok.Value;

import java.math.BigInteger;

@Value
public class RestaurantWithVotesTo {
    Integer id;
    String name;
    BigInteger vcount;
}
