package club.beingsoft.restaurants.util;

import club.beingsoft.restaurants.model.AbstractBaseEntity;
import club.beingsoft.restaurants.model.Dish;
import club.beingsoft.restaurants.model.Role;
import club.beingsoft.restaurants.model.User;
import club.beingsoft.restaurants.util.exception.EntityDeletedException;
import club.beingsoft.restaurants.util.exception.NotFoundException;
import club.beingsoft.restaurants.util.exception.PermissionException;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationUtil {

    public static void checkId(String entityName, Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Request has no id with entity " + entityName);
        }
    }

    public static <T> T checkEntityNotNull(String entityName, T entity, Integer id) {
        if (id == null) id = -1;
        if (entity == null)
            throw new NotFoundException("Object " + entityName + " with id " + id + " not found");
        if (entity instanceof Optional) {
            if (!((Optional<?>) entity).isPresent())
                throw new NotFoundException("Object with id " + id + " not found");
        }
        return entity;
    }

    public static <T> Collection<T> checkCollectionFound(String collectionName, Collection<T> collection) {
        if (collection.size() == 0)
            throw new NotFoundException(collectionName + " not found");
        return collection;
    }

    public static void checkAdmin() {
        User user = SecurityUtil.getAuthUser();
        if (!user.getRoles().contains(Role.ADMIN))
            throw new PermissionException("You don't have permission on this operation");
    }

    public static void checkEntityDelete(AbstractBaseEntity entity) {
        Assert.isTrue(entity.isDeleted(), "Entity " + entity.getClass().getName() + " is deleted");
    }

    public static void checkDishDeleted(Set<Dish> dishes) {
        Set<Dish> deletedDishes = new HashSet<>(dishes.stream().filter(AbstractBaseEntity::isDeleted).collect(Collectors.toSet()));
        if (deletedDishes.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder("Dishes deleted: ");
            deletedDishes.forEach(dish -> {
                stringBuilder.append(dish);
                stringBuilder.append(", ");
            });
            throw new EntityDeletedException(stringBuilder.toString());
        }
    }
}
