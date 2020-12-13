package club.beingsoft.restaurants.repository.jpa.impl;

import club.beingsoft.restaurants.model.*;
import club.beingsoft.restaurants.repository.jpa.RestaurantJpaRepositoryCustom;
import club.beingsoft.restaurants.to.RestaurantWithVotesTo;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQuery;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantJpaRepositoryCustomImpl implements RestaurantJpaRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Restaurant> getAllRestaurantsWithDishesToday() {
        QRestaurant restaurant = QRestaurant.restaurant;
        QMenu menu = QMenu.menu;
        QDish dish = QDish.dish;

        return new JPAQuery<Restaurant>(entityManager)
                .from(restaurant)
                .innerJoin(menu).on(menu.restaurant.id.eq(restaurant.id))
                .innerJoin(menu.dishes, dish)
                .where(restaurant.deleteDate.isNull()
                        .and(menu.deleteDate.isNull())
                        .and(dish.deleteDate.isNull())
                        .and(menu.menuDate.eq(LocalDate.now()))
                )
                .distinct()
                .fetch();
    }

    @Override
    public List<RestaurantWithVotesTo> getSortedByVotesRestaurantsToday() {
        QRestaurant restaurant = QRestaurant.restaurant;
        QVote vote = QVote.vote;

        NumberPath<Long> count = Expressions.numberPath(Long.class, "c");

        List<Tuple> result = new JPAQuery<RestaurantWithVotesTo>(entityManager)
                .select(restaurant.id, restaurant.name, vote.count().as(count))
                .from(restaurant)
                .innerJoin(vote).on(vote.restaurant.id.eq(restaurant.id))
                .where(vote.voteDate.eq(LocalDate.now()))
                .groupBy(restaurant.id)
                .orderBy(count.desc())
                .fetch();

        return result.stream().map(tuple -> new RestaurantWithVotesTo(tuple.get(0, Integer.class), tuple.get(1, String.class), tuple.get(2, Long.class))).collect(Collectors.toList());
    }
}
