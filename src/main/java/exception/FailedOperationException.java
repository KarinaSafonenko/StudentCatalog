package exception;

public class FailedOperationException extends Exception {
    public FailedOperationException(String message) {
        super(message);
    }
    public FailedOperationException(Throwable exception) {
        super(exception);
    }
}
