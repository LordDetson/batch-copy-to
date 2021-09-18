package by.babanin.batchcopyto.application;

import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

import by.babanin.batchcopyto.domain.Configuration;
import by.babanin.batchcopyto.domain.SourceTargetItem;

public final class ConfigurationToSourceTargetItemConverter {

    private ConfigurationToSourceTargetItemConverter() {
    }

    public static Set<SourceTargetItem> convert(Configuration configuration) {
        Path sourceDirectory = configuration.getSourceDirectory();
        Path targetDirectory = configuration.getTargetDirectory();
        Set<Path> files = configuration.getFiles();

        return files.stream()
                .map(file -> createSourceTargetItem(sourceDirectory, targetDirectory, file))
                .collect(Collectors.toSet());
    }

    private static SourceTargetItem createSourceTargetItem(Path sourceDirectory, Path targetDirectory, Path file) {
        Path sourceFile = sourceDirectory.resolve(file);
        Path targetFile = targetDirectory.resolve(file);
        return new SourceTargetItem(sourceFile, targetFile);
    }
}
