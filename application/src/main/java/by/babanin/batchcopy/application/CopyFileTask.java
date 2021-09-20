package by.babanin.batchcopy.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import by.babanin.batchcopy.application.exception.TaskException;

public class CopyFileTask extends AbstractTask<CopyTaskResult>{

    private final Path sourceFile;
    private final Path targetFile;

    public CopyFileTask(Path sourceFile, Path targetFile) {
        this.sourceFile = sourceFile;
        this.targetFile = targetFile;
    }

    @Override
    protected CopyTaskResult body() {
        CopyTaskResult result;
        try {
            Path parentTargetDirectory = targetFile.getParent();
            if(!Files.exists(parentTargetDirectory)) {
                Files.createDirectories(parentTargetDirectory);
            }
            Files.copy(sourceFile, targetFile);
            result = new CopyTaskResult(sourceFile, targetFile);
        }
        catch(IOException e) {
            result = new CopyTaskResult(sourceFile, targetFile, new TaskException(e));
        }
        return result;
    }
}
