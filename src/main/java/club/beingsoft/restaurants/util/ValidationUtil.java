package club.beingsoft.restaurants.util;

import club.beingsoft.restaurants.model.AbstractBaseEntity;
import club.beingsoft.restaurants.model.Dish;
import club.beingsoft.restaurants.util.exception.EntityDeletedException;
import club.beingsoft.restaurants.util.exception.NotFoundException;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationUtil {

    private static final String NOT_FOUND = " not found";

    private ValidationUtil() {
    }

    public static void checkId(String entityName, Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Request has no id with entity " + entityName);
        }
    }

    public static <T> T checkEntityNotNull(String entityName, T entity, Integer id) {
        if (id == null) id = -1;
        if (entity == null)
            throw new NotFoundException("Object " + entityName + " with id " + id + NOT_FOUND);
        if (entity instanceof Optional && ((Optional<?>) entity).isEmpty()) {
            throw new NotFoundException("Object with id " + id + NOT_FOUND);
        }
        return entity;
    }

    public static <T> void checkCollectionFound(String collectionName, Collection<T> collection) {
        if (collection.isEmpty())
            throw new NotFoundException(collectionName + NOT_FOUND);
    }

    public static void checkEntityDelete(AbstractBaseEntity entity) {
        Assert.isTrue(!entity.isDeleted(), "Entity " + entity.getClass().getName() + " is deleted");
    }

    public static void checkDishDeleted(Set<Dish> dishes) {
        Set<Dish> deletedDishes = dishes.stream().filter(AbstractBaseEntity::isDeleted).collect(Collectors.toSet());
        if (!deletedDishes.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder("Dishes deleted: ");
            deletedDishes.forEach(dish -> {
                stringBuilder.append(dish);
                stringBuilder.append(", ");
            });
            throw new EntityDeletedException(stringBuilder.toString());
        }
    }
}
