package stackover.resource.service.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ApiRequestException extends RuntimeException {

    @JsonProperty("message")
    private final String errorMessage;

    public ApiRequestException(String message) {
        super(message);
        this.errorMessage = message;
    }

    public ApiRequestException(String message, Exception e) {
        super(message, e);
        this.errorMessage = message;
    }
}