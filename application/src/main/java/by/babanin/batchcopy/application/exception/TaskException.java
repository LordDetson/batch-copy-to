package by.babanin.batchcopy.application.exception;

public class TaskException extends Exception {

    public TaskException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskException(Throwable cause) {
        super(cause);
    }
}
