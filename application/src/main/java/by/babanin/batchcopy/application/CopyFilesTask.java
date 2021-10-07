package by.babanin.batchcopy.application;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import by.babanin.batchcopy.application.ValidatebleTask.TaskMode;
import by.babanin.batchcopy.application.exception.TaskException;
import by.babanin.batchcopy.application.validator.ConfigurationValidator;
import by.babanin.batchcopy.application.validator.Validator;
import by.babanin.batchcopy.domain.Configuration;

public class CopyFilesTask extends MultiTask<CopyTaskResult> {

    private static final Validator<Configuration> VALIDATOR = new ConfigurationValidator();

    private final Configuration configuration;
    private final List<ProgressListener<Long>> progressListeners = new ArrayList<>();

    private TaskMode mode = TaskMode.ACTION;

    public CopyFilesTask(Configuration configuration) {
        super("files copy task");
        this.configuration = configuration;
    }

    @Override
    protected List<CopyTaskResult> body() throws TaskException {
        validateConfiguration();
        List<CopyFileTask> tasks = createCopyFileTasks();
        if(mode == TaskMode.ACTION) {
            Long fileSizesSum = calculateFileSizeSum(tasks);
            progressListeners.forEach(listener -> listener.saveProgressEnd(fileSizesSum));
        }
        addSubTasks(tasks);
        return super.body();
    }

    private Long calculateFileSizeSum(List<CopyFileTask> tasks) throws TaskException {
        AtomicLong fileSizeSum = new AtomicLong(0L);
        for(CopyFileTask task : tasks) {
            getFileSize(task).ifPresent(fileSizeSum::addAndGet);
            progressListeners.forEach(task::addProgressListener);
        }
        return fileSizeSum.get();
    }

    private Optional<Long> getFileSize(CopyFileTask task) throws TaskException {
        task.setMode(TaskMode.VALIDATION);
        CopyTaskResult result = task.run();
        task.setMode(mode);
        return result.getFileSize();
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
        catch(Exception e) {
            throw new TaskException(e);
        }
    }

    public TaskMode getMode() {
        return mode;
    }

    public void setMode(TaskMode mode) {
        this.mode = mode;
    }

    public void addProgressListener(ProgressListener<Long> listener) {
        progressListeners.add(listener);
    }

    public void removeProgressListener(ProgressListener<Long> listener) {
        progressListeners.remove(listener);
    }

    public void removeAllProgressListeners() {
        progressListeners.clear();
    }
}
