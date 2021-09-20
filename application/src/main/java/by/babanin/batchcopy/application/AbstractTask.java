package by.babanin.batchcopy.application;

import java.util.ArrayList;
import java.util.List;

import by.babanin.batchcopy.application.exception.TaskException;

public abstract class AbstractTask<R> implements Task<R> {

    private final List<TaskListener<R>> listeners = new ArrayList<>();

    public void addListener(TaskListener<R> listener) {
        listeners.add(listener);
    }

    public void removeListener(TaskListener<R> listener) {
        listeners.remove(listener);
    }

    @Override
    public R run() throws TaskException {
        listeners.forEach(TaskListener::doBefore);
        R result = body();
        listeners.forEach(listener -> listener.doAfter(result));
        return result;
    }

    protected abstract R body() throws TaskException;
}
