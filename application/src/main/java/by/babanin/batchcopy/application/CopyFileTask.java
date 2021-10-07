package by.babanin.batchcopy.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import by.babanin.batchcopy.application.exception.TaskException;
import by.babanin.batchcopy.application.util.PathUtils;

public class CopyFileTask extends ValidatebleTask<CopyTaskResult> {

    private final Path sourceFile;
    private final Path targetFile;
    private final List<ProgressListener<Long>> progressListeners = new ArrayList<>();

    public CopyFileTask(Path sourceFile, Path targetFile) {
        super("file copy task");
        this.sourceFile = sourceFile;
        this.targetFile = targetFile;
    }

    @Override
    protected CopyTaskResult doValidation() {
        CopyTaskResult result = new CopyTaskResult(sourceFile, targetFile);
        List<String> messages = PathUtils.validateFile(sourceFile);
        messages.addAll(PathUtils.validateAlreadyExistFile(targetFile));
        if(!messages.isEmpty()) {
            result.setException(new TaskException(String.join("\n", messages)));
            return result;
        }
        try {
            result.setFileSize(Files.size(sourceFile));
        }
        catch(IOException e) {
            result.setException(new TaskException(e));
        }
        return result;
    }

    @Override
    protected CopyTaskResult doAction() {
        CopyTaskResult result = doValidation();
        if(!result.getException().isPresent()) {
            try {
                createParentDirectoryIfNotExist();
                Files.copy(sourceFile, targetFile);
                result.getFileSize().ifPresent(
                        fileSize -> progressListeners.forEach(listener -> listener.updateProgress(fileSize)));
            }
            catch(IOException e) {
                result.setException(new TaskException(e));
            }
        }
        return result;
    }

    private void createParentDirectoryIfNotExist() throws IOException {
        Path parentTargetDirectory = targetFile.getParent();
        if(!Files.exists(parentTargetDirectory)) {
            Files.createDirectories(parentTargetDirectory);
        }
    }

    public void addProgressListener(ProgressListener<Long> listener) {
        progressListeners.add(listener);
    }

    public void removeProgressListener(ProgressListener<Long> listener) {
        progressListeners.remove(listener);
    }

    public void removeAllProgressListeners() {
        progressListeners.clear();
    }
}
