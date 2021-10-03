package by.babanin.batchcopy.application;

import java.nio.file.Path;
import java.util.Optional;

import by.babanin.batchcopy.application.exception.TaskException;

public class CopyTaskResult {

    private final Path sourceFile;
    private final Path targetFile;
    private Long fileSize;
    private TaskException exception;

    public CopyTaskResult(Path sourceFile, Path targetFile) {
        this.sourceFile = sourceFile;
        this.targetFile = targetFile;
    }

    public Path getSourceFile() {
        return sourceFile;
    }

    public Path getTargetFile() {
        return targetFile;
    }

    public Optional<Long> getFileSize() {
        return Optional.ofNullable(fileSize);
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Optional<TaskException> getException() {
        return Optional.ofNullable(exception);
    }

    public void setException(TaskException exception) {
        this.exception = exception;
    }
}
