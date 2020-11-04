package club.beingsoft.restaurants.util.exception;

public class EntityNotDeletedException extends RuntimeException {
    public EntityNotDeletedException(String message) {
        super(message);
    }
}
