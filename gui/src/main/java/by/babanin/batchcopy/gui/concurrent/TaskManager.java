package by.babanin.batchcopy.gui.concurrent;

import java.util.ArrayList;
import java.util.List;

import by.babanin.batchcopy.application.ApplicationTask;
import by.babanin.batchcopy.application.MultiTask;
import by.babanin.batchcopy.application.TaskListener;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;

public final class TaskManager {

    private TaskManager() {}

    public static <R> void run(ApplicationTask<R> task) {
        if(task instanceof MultiTask) {
            wrapListeners((MultiTask<R>) task);
        }
        else {
            wrapListeners(task);
        }
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

    private static <R> void wrapListeners(ApplicationTask<R> task) {
        List<TaskListener<R>> listeners = new ArrayList<>(task.getListeners());
        task.removeAllListeners();
        listeners.stream()
                .map(TaskListenerWrapper::new)
                .forEach(task::addListener);
    }

    private static <R> void wrapListeners(MultiTask<R> task) {
        List<TaskListener<List<R>>> listeners = new ArrayList<>(task.getListeners());
        task.removeAllListeners();
        listeners.stream()
                .map(TaskListenerWrapper::new)
                .forEach(task::addListener);

        List<TaskListener<R>> subTaskListeners = new ArrayList<>(task.getSubTaskListeners());
        task.removeAllSubTaskListeners();
        subTaskListeners.stream()
                .map(TaskListenerWrapper::new)
                .forEach(task::addSubTaskListener);
    }

    private static void handleException(WorkerStateEvent event) {
        event.getSource().getException().printStackTrace();
    }
}
