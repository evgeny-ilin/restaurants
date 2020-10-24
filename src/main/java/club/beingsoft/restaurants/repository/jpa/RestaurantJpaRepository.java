package club.beingsoft.restaurants.repository.jpa;

import club.beingsoft.restaurants.model.Restaurant;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RestaurantJpaRepository extends CrudRepository<Restaurant, Integer> {
    @Override
    List<Restaurant> findAll();
}
