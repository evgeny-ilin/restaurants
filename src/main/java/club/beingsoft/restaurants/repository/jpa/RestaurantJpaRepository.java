package club.beingsoft.restaurants.repository.jpa;

import club.beingsoft.restaurants.model.Restaurant;
import club.beingsoft.restaurants.to.RestaurantWithVotesTo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RestaurantJpaRepository extends CrudRepository<Restaurant, Integer> {
    @Override
    List<Restaurant> findAll();

    @Query(value = "SELECT *\n" +
            "FROM RESTAURANTS\n" +
            "WHERE EXISTS(SELECT 1\n" +
            "             FROM MENUS\n" +
            "                      JOIN MENU_DISHES MD on MENUS.ID = MD.MENU_ID\n" +
            "                      JOIN DISHES D on D.ID = MD.DISH_ID\n" +
            "             WHERE RESTAURANT_ID = RESTAURANTS.ID\n" +
            "               AND MENU_DATE = ?1\n" +
            "               AND MENUS.DELETE_DATE IS NULL\n" +
            "               AND D.DELETE_DATE IS NULL)\n" +
            "  AND DELETE_DATE IS NULL",
            nativeQuery = true)
    List<Restaurant> getAllWithDishesToDate(LocalDate date);

    @Query(name = "Restaurant.getMostScored", nativeQuery = true)
    List<RestaurantWithVotesTo> getMostScored(LocalDate date);
}
