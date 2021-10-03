package by.babanin.batchcopy.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import by.babanin.batchcopy.application.exception.TaskException;
import by.babanin.batchcopy.application.util.PathUtils;

public class CopyFileTask extends ValidatebleTask<CopyTaskResult> {

    private final Path sourceFile;
    private final Path targetFile;

    public CopyFileTask(Path sourceFile, Path targetFile) {
        super("file copy task");
        this.sourceFile = sourceFile;
        this.targetFile = targetFile;
    }

    @Override
    protected CopyTaskResult doValidation() {
        List<String> messages = PathUtils.validateFile(sourceFile);
        messages.addAll(PathUtils.validateAlreadyExistFile(targetFile));
        if(!messages.isEmpty()) {
            return new CopyTaskResult(sourceFile, targetFile, new TaskException(String.join("\n", messages)));
        }
        return new CopyTaskResult(sourceFile, targetFile);
    }

    @Override
    protected CopyTaskResult doAction() {
        CopyTaskResult result = doValidation();
        if(!result.hasException()) {
            try {
                createParentDirectoryIfNotExist();
                Files.copy(sourceFile, targetFile);
                result = new CopyTaskResult(sourceFile, targetFile);
            }
            catch(IOException e) {
                result = new CopyTaskResult(sourceFile, targetFile, new TaskException(e));
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
}
