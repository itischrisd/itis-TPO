package exception;

public class NoCommandException extends RuntimeException {

    public NoCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
