package club.beingsoft.restaurants.repository.jpa;

import club.beingsoft.restaurants.model.Restaurant;
import club.beingsoft.restaurants.to.RestaurantWithVotesTo;
import org.springframework.data.repository.NoRepositoryBean;

import java.time.LocalDate;
import java.util.List;

public interface RestaurantJpaRepositoryCustom {
    List<Restaurant> getAllWithDishesToDate(LocalDate date);

    List<RestaurantWithVotesTo> getSortedByVotesToDate(LocalDate date);
}
