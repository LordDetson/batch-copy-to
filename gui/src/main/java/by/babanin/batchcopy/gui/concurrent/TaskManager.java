package by.babanin.batchcopy.gui.concurrent;

import by.babanin.batchcopy.application.ApplicationTask;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;

public final class TaskManager {

    private TaskManager() {}

    public static <R> void run(ApplicationTask<R> task) {
        Task<R> backgroundTask = createBackgroundTask(task);
        backgroundTask.setOnFailed(TaskManager::handleException);
        Thread thread = new Thread(backgroundTask, task.getName());
        thread.setDaemon(true);
        thread.start();
    }

    private static <R> Task<R> createBackgroundTask(ApplicationTask<R> task) {
        return new Task<R>() {

            @Override
            protected R call() throws Exception {
                return task.run();
            }
        };
    }

    private static void handleException(WorkerStateEvent event) {
        event.getSource().getException().printStackTrace();
    }
}
