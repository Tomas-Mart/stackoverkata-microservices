package stackover.resource.service.exception;

import java.io.Serial;

public class QuestionException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5928912919297163881L;

    public QuestionException() {
    }

    public QuestionException(String message) {
        super(message);
    }

    public QuestionException(String s, Exception e) {
        super(s, e);
    }
}