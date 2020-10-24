package club.beingsoft.restaurants.repository.jpa;

import club.beingsoft.restaurants.model.Menu;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuJpaRepository extends CrudRepository<Menu, Integer> {
    @Override
    List<Menu> findAll();
}
