package club.beingsoft.restaurants.repository.jpa;

import club.beingsoft.restaurants.model.Restaurant;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantJpaRepository extends CrudRepository<Restaurant, Integer>, QuerydslPredicateExecutor<Restaurant>, RestaurantJpaRepositoryCustom {
    @Override
    List<Restaurant> findAll();
}
