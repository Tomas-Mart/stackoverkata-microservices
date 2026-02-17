package stackover.resource.service.exception;

import java.io.Serial;

public class AccountNotAvailableException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 7115737050472618624L;

    public AccountNotAvailableException() {
    }

    public AccountNotAvailableException(String message) {
        super(message);
    }

    public AccountNotAvailableException(String s, Exception e) {
        super(s, e);
    }

}