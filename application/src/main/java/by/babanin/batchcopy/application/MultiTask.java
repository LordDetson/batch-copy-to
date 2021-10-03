package by.babanin.batchcopy.application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import by.babanin.batchcopy.application.exception.TaskException;

public class MultiTask<R> extends AbstractTask<List<R>> {

    private final List<AbstractTask<R>> subTasks = new ArrayList<>();
    private final List<TaskListener<R>> subTaskListeners = new ArrayList<>();

    public MultiTask(String name) {
        this(name, Collections.emptyList());
    }

    public MultiTask(String name, Collection<? extends AbstractTask<R>> subTasks) {
        super(name);
        addSubTasks(subTasks);
    }

    public void addSubTask(AbstractTask<R> subTask) {
        subTasks.add(subTask);
    }

    public void addSubTasks(Collection<? extends AbstractTask<R>> subTasks) {
        this.subTasks.addAll(subTasks);
    }

    public void removeSubTask(AbstractTask<R> subTask) {
        subTasks.remove(subTask);
    }

    public void removeSubTasks(Collection<? extends AbstractTask<R>> subTasks) {
        this.subTasks.removeAll(subTasks);
    }

    public void removeAllSubTasks() {
        this.subTasks.clear();
    }

    public void addSubTaskListener(TaskListener<R> listener) {
        subTaskListeners.add(listener);
    }

    public void removeSubTaskListener(TaskListener<R> listener) {
        subTaskListeners.remove(listener);
    }

    public void removeAllSubTaskListeners() {
        subTaskListeners.clear();
    }

    public List<TaskListener<R>> getSubTaskListeners() {
        return subTaskListeners;
    }

    @Override
    protected List<R> body() throws TaskException {
        List<R> results = new ArrayList<>();
        for(AbstractTask<R> subTask : subTasks) {
            subTaskListeners.forEach(subTask::addListener);
            results.add(subTask.run());
        }
        return results;
    }
}
