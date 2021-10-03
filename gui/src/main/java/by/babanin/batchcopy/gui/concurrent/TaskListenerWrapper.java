package by.babanin.batchcopy.gui.concurrent;

import by.babanin.batchcopy.application.TaskListener;
import javafx.application.Platform;

public class TaskListenerWrapper<R> implements TaskListener<R> {

    private final TaskListener<R> listener;

    protected TaskListenerWrapper(TaskListener<R> listener) {
        this.listener = listener;
    }

    @Override
    public void doBefore() {
        Platform.runLater(listener::doBefore);
    }

    @Override
    public void doAfter(R result) {
        Platform.runLater(() -> listener.doAfter(result));
    }
}
