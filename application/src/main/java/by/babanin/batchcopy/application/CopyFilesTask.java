package by.babanin.batchcopy.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import by.babanin.batchcopy.application.ValidatebleTask.TaskMode;
import by.babanin.batchcopy.application.exception.TaskException;
import by.babanin.batchcopy.application.validator.ConfigurationValidator;
import by.babanin.batchcopy.application.validator.Validator;
import by.babanin.batchcopy.domain.Configuration;

public class CopyFilesTask extends MultiTask<CopyTaskResult> {

    private static final Validator<Configuration> VALIDATOR = new ConfigurationValidator();

    private final Configuration configuration;
    private TaskMode mode = TaskMode.ACTION;

    public CopyFilesTask(Configuration configuration) {
        super("files copy task");
        this.configuration = configuration;
    }

    @Override
    protected List<CopyTaskResult> body() throws TaskException {
        validateConfiguration();
        List<CopyFileTask> tasks = createCopyFileTasks();
        addSubTasks(tasks);
        return super.body();
    }

    private void validateConfiguration() throws TaskException {
        List<String> messages = VALIDATOR.validate(configuration);
        if(!messages.isEmpty()) {
            throw new TaskException(String.join("\n", messages));
        }
    }

    private List<CopyFileTask> createCopyFileTasks() throws TaskException {
        return convert(configuration).entrySet().stream()
                .map(this::createCopyFileTask)
                .collect(Collectors.toList());
    }

    private CopyFileTask createCopyFileTask(Entry<Path, Path> entry) {
        CopyFileTask task = new CopyFileTask(entry.getKey(), entry.getValue());
        task.setMode(mode);
        return task;
    }

    private Map<Path, Path> convert(Configuration configuration) throws TaskException {
        Path sourceDirectory = configuration.getSourceDirectory();
        Path targetDirectory = configuration.getTargetDirectory();
        try {
            return Files.readAllLines(configuration.getFileListPath()).stream()
                    .collect(Collectors.toMap(sourceDirectory::resolve, targetDirectory::resolve));
        }
        catch(IOException e) {
            throw new TaskException(e);
        }
    }

    public TaskMode getMode() {
        return mode;
    }

    public void setMode(TaskMode mode) {
        this.mode = mode;
    }
}
