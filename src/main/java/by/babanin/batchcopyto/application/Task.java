package by.babanin.batchcopyto.application;

import by.babanin.batchcopyto.exception.ApplicationException;

public interface Task {

    void run() throws ApplicationException;
}
