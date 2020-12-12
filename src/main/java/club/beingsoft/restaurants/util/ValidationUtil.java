package club.beingsoft.restaurants.util;

import club.beingsoft.restaurants.model.AbstractBaseEntity;
import club.beingsoft.restaurants.model.Dish;
import club.beingsoft.restaurants.util.exception.EntityDeletedException;
import club.beingsoft.restaurants.util.exception.IllegalRequestDataException;
import club.beingsoft.restaurants.util.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import javax.validation.*;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationUtil {

    private static final String NOT_FOUND = " not found";

    private static final Validator validator;

    static {
        //  From Javadoc: implementations are thread-safe and instances are typically cached and reused.
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        //  From Javadoc: implementations of this interface must be thread-safe
        validator = factory.getValidator();
    }

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

    public static <T> void validate(T bean) {
        // https://alexkosarev.name/2018/07/30/bean-validation-api/
        Set<ConstraintViolation<T>> violations = validator.validate(bean);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(HasId bean, int id) {
//      conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean + " must be with id=" + id);
        }
    }

    //  http://stackoverflow.com/a/28565320/548473
    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }

    public static ResponseEntity<String> getErrorResponse(BindingResult result) {
        return ResponseEntity.unprocessableEntity().body(
                result.getFieldErrors().stream()
                        .map(fe -> String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()))
                        .collect(Collectors.joining("<br>"))
        );
    }
}
