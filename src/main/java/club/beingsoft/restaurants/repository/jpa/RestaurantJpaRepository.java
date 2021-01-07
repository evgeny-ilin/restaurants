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

    @Query("select new club.beingsoft.restaurants.to.RestaurantWithVotesTo(r.id,r.name,count(v.id)) from Restaurant r, Vote v " +
            "where r.deleteDate is null " +
            "and v.restaurant = r " +
            "and v.deleteDate is null " +
            "and v.voteDate = ?1 " +
            "group by r.id,r.name " +
            "order by 3 desc")
    List<RestaurantWithVotesTo> getSortedByVotes(LocalDate date);
}
