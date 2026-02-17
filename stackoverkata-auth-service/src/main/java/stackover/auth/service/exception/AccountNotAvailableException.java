package stackover.auth.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AccountNotAvailableException extends RuntimeException {

    public AccountNotAvailableException() {
        super();
    }

    public AccountNotAvailableException(String message) {
        super(message);
    }
}