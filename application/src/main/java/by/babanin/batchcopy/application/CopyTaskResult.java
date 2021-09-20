package by.babanin.batchcopy.application;

import java.nio.file.Path;

import by.babanin.batchcopy.application.exception.TaskException;

public class CopyTaskResult {

    private final Path sourceFile;
    private final Path targetFile;
    private final TaskException exception;

    public CopyTaskResult(Path sourceFile, Path targetFile) {
        this(sourceFile, targetFile, null);
    }

    public CopyTaskResult(Path sourceFile, Path targetFile, TaskException exception) {
        this.sourceFile = sourceFile;
        this.targetFile = targetFile;
        this.exception = exception;
    }

    public Path getSourceFile() {
        return sourceFile;
    }

    public Path getTargetFile() {
        return targetFile;
    }

    public TaskException getException() {
        return exception;
    }

    public boolean hasException() {
        return exception != null;
    }
}
