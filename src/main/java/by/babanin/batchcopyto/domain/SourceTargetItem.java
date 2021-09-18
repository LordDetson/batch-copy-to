package by.babanin.batchcopyto.domain;

import java.nio.file.Path;
import java.util.Objects;

public class SourceTargetItem {

    private final Path sourceFile;
    private final Path targetFile;

    public SourceTargetItem(Path sourceFile, Path targetFile) {
        this.sourceFile = sourceFile;
        this.targetFile = targetFile;
    }

    public Path getSourceFile() {
        return sourceFile;
    }

    public Path getTargetFile() {
        return targetFile;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        SourceTargetItem that = (SourceTargetItem) o;
        return sourceFile.equals(that.sourceFile) && targetFile.equals(that.targetFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceFile, targetFile);
    }
}
