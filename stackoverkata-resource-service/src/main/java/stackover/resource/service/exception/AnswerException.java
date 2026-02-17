package stackover.resource.service.exception;

import lombok.Getter;

import java.io.Serial;

@Getter
public class AnswerException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 7115737071472618624L;
    private final int statusCode;

    public AnswerException() {
        this("Answer operation failed", 400);
    }

    public AnswerException(String message) {
        this(message, 400); // По умолчанию 400 Bad Request
    }

    public AnswerException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public AnswerException(String message, Throwable cause) {
        this(message, cause, 400);
    }

    public AnswerException(String message, Throwable cause, int statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }
}