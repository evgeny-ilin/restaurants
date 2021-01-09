package club.beingsoft.restaurants.repository.jpa;

import club.beingsoft.restaurants.model.Dish;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DishJpaRepository extends CrudRepository<Dish, Integer>, QuerydslPredicateExecutor<Dish> {
    @Override
    List<Dish> findAll();

    @Query("select d from Dish d join d.menus  m " +
            "where m.id = ?1 " +
            "and m.deleteDate is null " +
            "and d.deleteDate is null ")
    List<Dish> getDishesForMenu(Integer menuId);


    @Query("select d from Restaurant  r join Menu m on m.restaurant.id = r.id join m.dishes d " +
            "where r.id = :restaurantId " +
            "and m.menuDate = :menuDate " +
            "and m.deleteDate is null " +
            "and d.deleteDate is null " +
            "and r.deleteDate is null ")
    List<Dish> getDishesForRestaurant(@Param("restaurantId") Integer restaurantId, @Param("menuDate") LocalDate menuDate);
}
