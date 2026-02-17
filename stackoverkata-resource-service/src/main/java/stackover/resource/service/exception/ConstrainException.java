package stackover.resource.service.exception;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.util.Set;

@Getter
public class ConstrainException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 5723952907135446546L;

    private Set<? extends ConstraintViolation<?>> constraintViolations;
    private HttpStatus status;

    public ConstrainException() {
        this.status = HttpStatus.BAD_REQUEST;
    }

    public ConstrainException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }

    public ConstrainException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public ConstrainException(String message, Set<? extends ConstraintViolation<?>> constraintViolations) {
        super(message);
        this.constraintViolations = constraintViolations;
        this.status = HttpStatus.BAD_REQUEST;
    }

    public ConstrainException(String message, Exception e) {
        super(message, e);
        this.status = HttpStatus.BAD_REQUEST;
    }

    public ConstrainException(String message, Set<? extends ConstraintViolation<?>> constraintViolations, HttpStatus status) {
        super(message);
        this.constraintViolations = constraintViolations;
        this.status = status;
    }
}