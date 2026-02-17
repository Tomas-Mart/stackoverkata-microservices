package stackover.resource.service.exception;

public class FeignRequestException extends RuntimeException {

    public FeignRequestException(String message) {
        super(message);
    }

    public FeignRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}