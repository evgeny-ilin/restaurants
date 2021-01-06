package club.beingsoft.restaurants.util.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private String message;

    private String debugMessage;

    private List<FieldValidationError> fieldValidationErrors;

    ApiError() {
    }

    ApiError(Throwable ex) {
        this();
        this.message = "Unexpected error";
        this.setDebugMessage(ex.getLocalizedMessage());
    }

    ApiError(String message, Throwable ex) {
        this();
        this.message = message;
        this.setDebugMessage(ex.getLocalizedMessage());
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(error -> {
            FieldValidationError subError = new FieldValidationError();
            subError.setField(error.getField());
            subError.setMessage(error.getDefaultMessage());
            subError.setRejectedValue(error.getRejectedValue());
            subError.setObject(error.getObjectName());
            this.addSubError(subError);
        });
    }

    private void addSubError(FieldValidationError subError) {
        if (fieldValidationErrors == null) {
            fieldValidationErrors = new ArrayList<>();
        }
        fieldValidationErrors.add(subError);
    }

    public List<FieldValidationError> getFieledValidationErrors() {
        return fieldValidationErrors;
    }

    public void setFieldValidationErrors(List<FieldValidationError> apiValidationErrors) {
        this.fieldValidationErrors = apiValidationErrors;
    }
}
