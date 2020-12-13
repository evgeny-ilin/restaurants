package club.beingsoft.restaurants.repository.jpa;

import club.beingsoft.restaurants.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
