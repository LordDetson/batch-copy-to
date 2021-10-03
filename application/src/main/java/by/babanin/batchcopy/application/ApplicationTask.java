package by.babanin.batchcopy.application;

import java.util.List;

import by.babanin.batchcopy.application.exception.TaskException;

public interface ApplicationTask<R> {

    R run() throws TaskException;

    void addListener(TaskListener<R> listener);

    void removeListener(TaskListener<R> listener);

    void removeAllListeners();

    List<TaskListener<R>> getListeners();

    String getName();
}
