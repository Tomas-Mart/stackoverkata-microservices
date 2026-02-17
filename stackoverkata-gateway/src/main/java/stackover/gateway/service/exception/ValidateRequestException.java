package stackover.gateway.service.exception;

public class ValidateRequestException extends Exception {

    public ValidateRequestException(String message) {
        super(message);
    }

    public ValidateRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
