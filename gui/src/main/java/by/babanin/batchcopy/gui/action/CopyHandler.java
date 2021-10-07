package by.babanin.batchcopy.gui.action;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import by.babanin.batchcopy.application.CopyFilesTask;
import by.babanin.batchcopy.application.CopyTaskResult;
import by.babanin.batchcopy.application.ProgressListener;
import by.babanin.batchcopy.application.TaskListener;
import by.babanin.batchcopy.application.ValidatebleTask.TaskMode;
import by.babanin.batchcopy.application.exception.TaskException;
import by.babanin.batchcopy.domain.Configuration;
import by.babanin.batchcopy.gui.component.DirectorySelectionField;
import by.babanin.batchcopy.gui.component.FileSelectionField;
import by.babanin.batchcopy.gui.concurrent.TaskManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;

public class CopyHandler extends AbstractActionHandler<ActionEvent> {

    private final DirectorySelectionField sourceDirectoryField;
    private final DirectorySelectionField targetDirectoryField;
    private final FileSelectionField fileListField;
    private final TextArea messageArea;
    private final ProgressBar progressBar;
    private final Button copyButton;
    private final Button validButton;

    private TaskMode mode = TaskMode.ACTION;

    public CopyHandler(DirectorySelectionField sourceDirectoryField, DirectorySelectionField targetDirectoryField,
            FileSelectionField fileListField, TextArea messageArea, ProgressBar progressBar, Button copyButton, Button validButton) {
        this.sourceDirectoryField = sourceDirectoryField;
        this.targetDirectoryField = targetDirectoryField;
        this.fileListField = fileListField;
        this.messageArea = messageArea;
        this.progressBar = progressBar;
        this.copyButton = copyButton;
        this.validButton = validButton;
    }

    @Override
    public void body() {
        Path sourcePath = sourceDirectoryField.getPath();
        Path targetPath = targetDirectoryField.getPath();
        Path fileListPath = fileListField.getPath();
        Configuration configuration = new Configuration(sourcePath, targetPath, fileListPath);

        CopyFilesTask task = new CopyFilesTask(configuration);
        task.setMode(mode);
        task.addListener(createCopyFilesTaskListener());
        task.addProgressListener(createProgressListener());
        task.addSubTaskListener(createCopyFileSubTaskListener());
        TaskManager.run(task);
    }

    private TaskListener<List<CopyTaskResult>> createCopyFilesTaskListener() {
        return new TaskListener<List<CopyTaskResult>>() {

            @Override
            public void doBefore() {
                if(mode == TaskMode.ACTION) {
                    showProgressBar(true);
                }
                messageArea.setText("Starting " + getModeCaption() + "...\n");
            }

            private String getModeCaption() {
                String header = "";
                switch(mode) {
                case ACTION:
                    header = "copy";
                    break;
                case VALIDATION:
                    header = "validation";
                    break;
                default:
                    assert false : mode + " isn't supported";
                }
                return header;
            }

            @Override
            public void doAfter(List<CopyTaskResult> results) {
                List<CopyTaskResult> successfullyResults = new ArrayList<>(results);
                List<TaskException> exceptions = results.stream()
                        .filter(result -> result.getException().isPresent())
                        .map(result -> result.getException().get())
                        .collect(Collectors.toList());
                successfullyResults.removeIf(result -> result.getException().isPresent());

                showResults(successfullyResults, exceptions);
                if(mode == TaskMode.ACTION) {
                    showProgressBar(false);
                }
            }

            private void showResults(List<CopyTaskResult> successfullyResults, List<TaskException> exceptions) {
                StringBuilder builder = new StringBuilder();
                builder.append("\n--------------------------------------------------------------------------------------------------\n")
                        .append("Summary ")
                        .append(getModeCaption())
                        .append(" result:\n")
                        .append("Copy successful (")
                        .append(successfullyResults.size())
                        .append(" count)\n");
                if(!exceptions.isEmpty()) {
                    builder.append("Copy unsuccessfully (")
                            .append(exceptions.size())
                            .append(" count)\n");
                    exceptions.forEach(exception -> {
                        builder.append(exception.getMessage()).append("\n");
                        exception.printStackTrace();
                    });
                }
                messageArea.appendText(builder.toString());
            }

            @Override
            public void doFailed(TaskException taskException) {
                messageArea.appendText(taskException.getMessage());
                if(mode == TaskMode.ACTION) {
                    showProgressBar(false);
                }
            }
        };
    }

    private TaskListener<CopyTaskResult> createCopyFileSubTaskListener() {
        return new TaskListener<CopyTaskResult>() {

            @Override
            public void doBefore() {
                // do nothing
            }

            @Override
            public void doAfter(CopyTaskResult result) {
                StringBuilder builder = new StringBuilder();
                Path targetFile = result.getTargetFile();
                Path sourceFile = result.getSourceFile();
                Optional<TaskException> exception = result.getException();
                Optional<Long> fileSize = result.getFileSize();
                builder.append(exception.isPresent() ? "Not coped " : "Coped ")
                        .append("\"").append(targetFile.getFileName().toString()).append("\"")
                        .append(" file from ")
                        .append("\"").append(sourceFile.getParent().toString()).append("\"")
                        .append(" to ")
                        .append("\"").append(targetFile.getParent().toString()).append("\"");
                fileSize.ifPresent(size -> builder.append(" (").append(size).append(" bytes)"));
                builder.append("\n");
                exception.ifPresent(e -> builder.append(e.getMessage()).append("\n"));
                messageArea.appendText(builder.toString());
            }

            @Override
            public void doFailed(TaskException taskException) {
                messageArea.appendText(taskException.getMessage());
                if(mode == TaskMode.ACTION) {
                    showProgressBar(false);
                }
            }
        };
    }

    private ProgressListener<Long> createProgressListener() {
        return new ProgressListener<Long>() {

            private Long progressEnd = 0L;
            private Long progressSum = 0L;

            @Override
            public void saveProgressEnd(Long progressEnd) {
                Platform.runLater(() -> this.progressEnd = progressEnd);
            }

            @Override
            public void updateProgress(Long progress) {
                Platform.runLater(() -> {
                    progressSum += progress;
                    progressBar.setProgress(progressSum / (double) progressEnd);
                });
            }
        };
    }

    public void enableCopyMode() {
        mode = TaskMode.ACTION;
    }

    public void enableValidationMode() {
        mode = TaskMode.VALIDATION;
    }

    private void showProgressBar(boolean enable) {
        copyButton.setVisible(!enable);
        validButton.setVisible(!enable);
        progressBar.setVisible(enable);
        if(!enable) {
            progressBar.setProgress(0);
        }
    }
}
