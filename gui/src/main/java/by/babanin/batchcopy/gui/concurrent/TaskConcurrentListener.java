package by.babanin.batchcopy.gui.concurrent;

import by.babanin.batchcopy.application.TaskListener;
import by.babanin.batchcopy.application.exception.TaskException;
import javafx.application.Platform;

public abstract class TaskConcurrentListener<R> implements TaskListener<R> {

    @Override
    public void doBefore() {
        Platform.runLater(this::before);
    }

    public abstract void before();

    @Override
    public void doAfter(R result) {
        Platform.runLater(() -> after(result));
    }

    public abstract void after(R result);

    @Override
    public void doFailed(TaskException taskException) {
        Platform.runLater(() -> failed(taskException));
    }

    public abstract void failed(TaskException taskException);
}
