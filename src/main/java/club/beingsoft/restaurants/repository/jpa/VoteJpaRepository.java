package club.beingsoft.restaurants.repository.jpa;

import club.beingsoft.restaurants.model.Vote;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VoteJpaRepository extends CrudRepository<Vote, Integer> {
    @Override
    List<Vote> findAll();
}
