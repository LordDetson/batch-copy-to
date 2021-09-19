package by.babanin.batchcopyto.application;

import by.babanin.batchcopyto.exception.ApplicationException;

public interface Task<R> {

    R run() throws ApplicationException;
}
