package by.babanin.batchcopyto.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import by.babanin.batchcopyto.domain.SourceTargetItem;
import by.babanin.batchcopyto.exception.ApplicationException;

public class CopyFileTask extends AbstractTask<CopyTaskResult>{

    private final SourceTargetItem item;

    public CopyFileTask(SourceTargetItem item) {
        this.item = item;
    }

    @Override
    protected CopyTaskResult body() {
        CopyTaskResult result;
        try {
            Path targetFile = item.getTargetFile();
            Path parentTargetDirectory = targetFile.getParent();
            if(!Files.exists(parentTargetDirectory)) {
                Files.createDirectories(parentTargetDirectory);
            }
            Files.copy(item.getSourceFile(), targetFile);
            result = new CopyTaskResult(item);
        }
        catch(IOException e) {
            result = new CopyTaskResult(item, new ApplicationException(e));
        }
        return result;
    }
}
