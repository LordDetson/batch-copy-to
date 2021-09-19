package by.babanin.batchcopyto.application;

import java.util.ArrayList;
import java.util.List;

import by.babanin.batchcopyto.exception.ApplicationException;

public abstract class AbstractTask<R> implements Task<R> {

    private final List<TaskListener<R>> listeners = new ArrayList<>();

    public void addListener(TaskListener<R> listener) {
        listeners.add(listener);
    }

    public void removeListener(TaskListener<R> listener) {
        listeners.remove(listener);
    }

    @Override
    public R run() throws ApplicationException {
        listeners.forEach(TaskListener::doBefore);
        R result = body();
        listeners.forEach(listener -> listener.doAfter(result));
        return result;
    }

    protected abstract R body() throws ApplicationException;
}
