package club.beingsoft.restaurants.repository.jpa;

import club.beingsoft.restaurants.model.Dish;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Predicate;

@Repository
public interface DishJpaRepository extends CrudRepository<Dish, Integer>, QuerydslPredicateExecutor<Dish> {
    @Override
    List<Dish> findAll();
}
