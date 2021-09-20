package by.babanin.batchcopy.application;

import java.util.ArrayList;
import java.util.List;

import by.babanin.batchcopy.application.exception.TaskException;

public class MultiTask<R> extends AbstractTask<List<R>> {

    private final List<AbstractTask<R>> subTasks;

    public MultiTask(List<AbstractTask<R>> subTasks) {
        this.subTasks = subTasks;
    }

    public void addListenerToSubTasks(TaskListener<R> listener) {
        subTasks.forEach(subTask -> subTask.addListener(listener));
    }

    public void removeListenerFromSubTasks(TaskListener<R> listener) {
        subTasks.forEach(subTask -> subTask.removeListener(listener));
    }

    @Override
    protected List<R> body() throws TaskException {
        List<R> results = new ArrayList<>();
        for(Task<R> task : subTasks) {
            results.add(task.run());
        }
        return results;
    }
}
