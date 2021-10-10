package by.babanin.batchcopy.console;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import by.babanin.batchcopy.application.CopyTaskResult;
import by.babanin.batchcopy.application.TaskListener;
import by.babanin.batchcopy.application.exception.TaskException;

public class CopyFilesTaskListener implements TaskListener<List<CopyTaskResult>> {

    private final Console console;

    public CopyFilesTaskListener(Console console) {
        this.console = console;
    }

    @Override
    public void doBefore() {
        console.writer().println("Starting copy...");
    }

    @Override
    public void doAfter(List<CopyTaskResult> results) {
        List<CopyTaskResult> successfullyResults = new ArrayList<>(results);
        List<TaskException> exceptions = results.stream()
                .filter(result -> result.getException().isPresent())
                .map(result -> result.getException().get())
                .collect(Collectors.toList());
        successfullyResults.removeIf(result -> result.getException().isPresent());

        showResults(successfullyResults, exceptions);
    }

    private void showResults(List<CopyTaskResult> successfullyResults, List<TaskException> exceptions) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n--------------------------------------------------------------------------------------------------\n")
                .append("Summary copy result:\n")
                .append("Copy successful (")
                .append(successfullyResults.size())
                .append(" count)\n");
        if(!exceptions.isEmpty()) {
            builder.append("Copy unsuccessfully (")
                    .append(exceptions.size())
                    .append(" count)\n");
            exceptions.forEach(exception -> {
                builder.append(exception.getMessage()).append("\n");
                exception.printStackTrace();
            });
        }
        console.writer().append(builder).flush();
    }

    @Override
    public void doFailed(TaskException taskException) {
        console.writer().append(taskException.getMessage()).flush();
    }
}
