package by.babanin.batchcopy.application;

import by.babanin.batchcopy.application.exception.TaskException;

public interface TaskListener<R> {

    void doBefore();

    void doAfter(R result);

    void doFailed(TaskException taskException);
}
