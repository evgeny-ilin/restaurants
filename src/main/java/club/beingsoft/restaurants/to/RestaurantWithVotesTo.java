package club.beingsoft.restaurants.to;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
@Schema(description = "Список ресторанов с количеством голосов")
public class RestaurantWithVotesTo {
    Integer id;
    String name;
    Long vcount;
}
