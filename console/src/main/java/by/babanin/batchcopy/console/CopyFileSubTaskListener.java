package by.babanin.batchcopy.console;

import java.io.Console;
import java.nio.file.Path;
import java.util.Optional;

import by.babanin.batchcopy.application.CopyTaskResult;
import by.babanin.batchcopy.application.TaskListener;
import by.babanin.batchcopy.application.exception.TaskException;

public class CopyFileSubTaskListener implements TaskListener<CopyTaskResult> {

    private final Console console;

    public CopyFileSubTaskListener(Console console) {
        this.console = console;
    }

    @Override
    public void doBefore() {
        // do nothing
    }

    @Override
    public void doAfter(CopyTaskResult result) {
        StringBuilder builder = new StringBuilder();
        Path targetFile = result.getTargetFile();
        Path sourceFile = result.getSourceFile();
        Optional<TaskException> exception = result.getException();
        Optional<Long> fileSize = result.getFileSize();
        builder.append(exception.isPresent() ? "Not coped " : "Coped ")
                .append("\"").append(targetFile.getFileName().toString()).append("\"")
                .append(" file from ")
                .append("\"").append(sourceFile.getParent().toString()).append("\"")
                .append(" to ")
                .append("\"").append(targetFile.getParent().toString()).append("\"");
        fileSize.ifPresent(size -> builder.append(" (").append(size).append(" bytes)"));
        builder.append("\n");
        exception.ifPresent(e -> builder.append(e.getMessage()).append("\n"));
        console.writer().append(builder).flush();
    }

    @Override
    public void doFailed(TaskException taskException) {
        console.writer().append(taskException.getMessage()).flush();
    }
}
