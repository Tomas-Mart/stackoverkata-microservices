package stackover.resource.service.exception;

import java.io.Serial;

public class VoteException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -8069721894426741488L;

    public VoteException() {
    }

    public VoteException(String message) {
        super(message);
    }

    public VoteException(String s, Exception e) {
        super(s, e);
    }
}