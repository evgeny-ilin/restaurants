package club.beingsoft.restaurants.util.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ApiError handleNotFound(Exception ex, WebRequest request) {
        return new ApiError(request.getDescription(true), ex);
    }

    @ExceptionHandler(EntityDeletedException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    protected ApiError handleEntityDeletedException(Exception ex, WebRequest request) {
        return new ApiError(request.getDescription(true), ex);
    }

    @ExceptionHandler(IllegalRequestDataException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ApiError handleIllegalRequestDataException(Exception ex, WebRequest request) {
        return new ApiError(request.getDescription(true), ex);
    }

    @ExceptionHandler(VoteCantBeChangedException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    protected ApiError handleVoteCantBeChangedException(Exception ex, WebRequest request) {
        return new ApiError(request.getDescription(true), ex);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    protected ApiError handleDataIntegrityViolationException(Exception ex, WebRequest request) {
        return new ApiError(request.getDescription(true), ex);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ApiError handleAllExceptions(Exception ex, WebRequest request) {
        return new ApiError("Something get wrong with request " + request.getDescription(true), ex);
    }
}
