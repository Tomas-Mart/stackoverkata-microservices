package stackover.resource.service.exception;

import java.io.Serial;

public class TagNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3093952821622304752L;

    public TagNotFoundException() {
    }

    public TagNotFoundException(String message) {
        super(message);
    }

    public TagNotFoundException(String s, Exception e) {
        super(s, e);
    }
}