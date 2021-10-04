package by.babanin.batchcopy.application;

import java.util.ArrayList;
import java.util.List;

import by.babanin.batchcopy.application.exception.TaskException;

public abstract class AbstractTask<R> implements ApplicationTask<R> {

    private final List<TaskListener<R>> listeners = new ArrayList<>();

    private final String name;

    protected AbstractTask(String name) {
        this.name = name;
    }

    @Override

    public void addListener(TaskListener<R> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(TaskListener<R> listener) {
        listeners.remove(listener);
    }

    @Override
    public List<TaskListener<R>> getListeners() {
        return listeners;
    }

    @Override
    public void removeAllListeners() {
        listeners.clear();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public R run() throws TaskException {
        try {
            listeners.forEach(TaskListener::doBefore);
            R result = body();
            listeners.forEach(listener -> listener.doAfter(result));
            return result;
        }
        catch(TaskException e) {
            listeners.forEach(listener -> listener.doFailed(e));
            throw e;
        }
    }

    protected abstract R body() throws TaskException;
}
