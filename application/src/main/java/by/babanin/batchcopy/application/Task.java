package by.babanin.batchcopy.application;

import by.babanin.batchcopy.application.exception.TaskException;

public interface Task<R> {

    R run() throws TaskException;
}
