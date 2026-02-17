package stackover.gateway.service.exception;

public class ForbiddenPathException extends Exception {

    public ForbiddenPathException(String message) {
        super(message);
    }

    public ForbiddenPathException(String message, Throwable cause) {
        super(message, cause);
    }
}
