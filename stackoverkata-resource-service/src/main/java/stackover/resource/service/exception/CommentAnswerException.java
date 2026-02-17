package stackover.resource.service.exception;

import java.io.Serial;

public class CommentAnswerException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = -8069721894426761488L;


    public CommentAnswerException() {
    }

    public CommentAnswerException(String message) {
        super(message);
    }

    public CommentAnswerException(String s, Exception e) {
        super(s, e);
    }
}