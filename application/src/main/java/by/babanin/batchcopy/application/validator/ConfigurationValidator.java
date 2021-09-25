package by.babanin.batchcopy.application.validator;

import static by.babanin.batchcopy.application.util.PathUtils.validateDirectory;
import static by.babanin.batchcopy.application.util.PathUtils.validateReadableFile;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import by.babanin.batchcopy.domain.Configuration;

public class ConfigurationValidator implements Validator<Configuration> {

    @Override
    public List<String> validate(Configuration configuration) {
        Path sourceDirectory = configuration.getSourceDirectory();
        Path targetDirectory = configuration.getTargetDirectory();
        Path fileListPath = configuration.getFileListPath();

        List<String> messages = new ArrayList<>();
        messages.addAll(validateDirectory(sourceDirectory));
        messages.addAll(validateDirectory(targetDirectory));
        messages.addAll(validateReadableFile(fileListPath));

        return messages;
    }
}
