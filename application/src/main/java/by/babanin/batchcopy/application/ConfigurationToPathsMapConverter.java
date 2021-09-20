package by.babanin.batchcopy.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import by.babanin.batchcopy.domain.Configuration;

public final class ConfigurationToPathsMapConverter {

    private ConfigurationToPathsMapConverter() {
    }

    public static Map<Path, Path> convert(Configuration configuration) {
        Path sourceDirectory = configuration.getSourceDirectory();
        Path targetDirectory = configuration.getTargetDirectory();
        try {
            return Files.readAllLines(configuration.getFileListPath()).stream()
                    .collect(Collectors.toMap(sourceDirectory::resolve, targetDirectory::resolve));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }
}
