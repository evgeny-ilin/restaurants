package club.beingsoft.restaurants.util.exception;

public class MustBeAdmin extends RuntimeException {
    public MustBeAdmin(String message) {
        super(message);
    }
}