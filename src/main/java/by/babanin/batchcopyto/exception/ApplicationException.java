package by.babanin.batchcopyto.exception;

public class ApplicationException extends Exception {

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }
}
