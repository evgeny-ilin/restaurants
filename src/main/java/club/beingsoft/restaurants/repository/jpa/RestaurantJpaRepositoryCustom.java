package club.beingsoft.restaurants.repository.jpa;

import club.beingsoft.restaurants.model.Restaurant;
import club.beingsoft.restaurants.to.RestaurantWithVotesTo;

import java.util.List;

public interface RestaurantJpaRepositoryCustom {
    List<Restaurant> getAllWithDishesToday();

    List<RestaurantWithVotesTo> getSortedByVotesToday();
}
