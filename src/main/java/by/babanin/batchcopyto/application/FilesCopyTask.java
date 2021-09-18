package by.babanin.batchcopyto.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import by.babanin.batchcopyto.domain.Configuration;
import by.babanin.batchcopyto.domain.SourceTargetItem;
import by.babanin.batchcopyto.exception.ApplicationException;

public class FilesCopyTask implements Task {

    private final Set<SourceTargetItem> sourceTargetItems;

    public FilesCopyTask(Set<SourceTargetItem> sourceTargetItems) {
        this.sourceTargetItems = sourceTargetItems;
    }

    public FilesCopyTask(Configuration configuration) {
        this.sourceTargetItems = ConfigurationToSourceTargetItemConverter.convert(configuration);
    }

    @Override
    public void run() throws ApplicationException {
        try {
            for(SourceTargetItem item : sourceTargetItems) {
                Path targetFile = item.getTargetFile();
                Path parentTargetDirectory = targetFile.getParent();
                if(!Files.exists(parentTargetDirectory)) {
                    Files.createDirectories(parentTargetDirectory);
                }
                Files.copy(item.getSourceFile(), targetFile);
            }
        }
        catch(IOException e) {
            throw new ApplicationException(e);
        }
    }
}
