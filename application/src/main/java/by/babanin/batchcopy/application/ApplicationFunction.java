package by.babanin.batchcopy.application;

import by.babanin.batchcopy.application.exception.TaskException;

@FunctionalInterface
public interface ApplicationFunction<T, R> {

    R apply(T t) throws TaskException;
}
