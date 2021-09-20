package by.babanin.batchcopy.domain;

import java.nio.file.Path;

public class Configuration {

    private final Path sourceDirectory;
    private final Path targetDirectory;
    private final Path fileListPath;

    public Configuration(Path sourceDirectory, Path targetDirectory, Path fileListPath) {
        this.sourceDirectory = sourceDirectory;
        this.targetDirectory = targetDirectory;
        this.fileListPath = fileListPath;
    }

    public Path getSourceDirectory() {
        return sourceDirectory;
    }

    public Path getTargetDirectory() {
        return targetDirectory;
    }

    public Path getFileListPath() {
        return fileListPath;
    }
}
