package stackover.resource.service.exception;

import java.io.Serial;

public class TagAlreadyExistsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6711820158011040724L;

    public TagAlreadyExistsException() {
    }

    public TagAlreadyExistsException(String message) {
        super(message);
    }

    public TagAlreadyExistsException(String s, Exception e) {
        super(s, e);
    }
}