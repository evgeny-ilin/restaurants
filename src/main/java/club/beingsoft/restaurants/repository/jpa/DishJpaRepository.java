package club.beingsoft.restaurants.repository.jpa;

import club.beingsoft.restaurants.model.Dish;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishJpaRepository extends CrudRepository<Dish, Integer> {
    @Override
    List<Dish> findAll();
}
