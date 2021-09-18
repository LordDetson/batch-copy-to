package by.babanin.batchcopyto.domain;

import java.nio.file.Path;
import java.util.Set;

public class Configuration {

    private final Path sourceDirectory;
    private final Path targetDirectory;
    private final Set<Path> files;

    public Configuration(Path sourceDirectory, Path targetDirectory, Set<Path> files) {
        this.sourceDirectory = sourceDirectory;
        this.targetDirectory = targetDirectory;
        this.files = files;
    }

    public Path getSourceDirectory() {
        return sourceDirectory;
    }

    public Path getTargetDirectory() {
        return targetDirectory;
    }

    public Set<Path> getFiles() {
        return files;
    }
}
