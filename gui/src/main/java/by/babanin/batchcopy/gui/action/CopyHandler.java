package by.babanin.batchcopy.gui.action;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import by.babanin.batchcopy.application.CopyFilesTask;
import by.babanin.batchcopy.application.CopyTaskResult;
import by.babanin.batchcopy.application.TaskListener;
import by.babanin.batchcopy.application.ValidatebleTask.TaskMode;
import by.babanin.batchcopy.application.exception.TaskException;
import by.babanin.batchcopy.domain.Configuration;
import javafx.event.ActionEvent;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CopyHandler extends AbstractActionHandler<ActionEvent> {

    private final TextField sourceDirectoryField;
    private final TextField targetDirectoryField;
    private final TextField fileListField;
    private final TextArea messageArea;
    private final ProgressBar progressBar;

    private TaskMode mode = TaskMode.ACTION;

    public CopyHandler(TextField sourceDirectoryField, TextField targetDirectoryField, TextField fileListField, TextArea messageArea,
            ProgressBar progressBar) {
        this.sourceDirectoryField = sourceDirectoryField;
        this.targetDirectoryField = targetDirectoryField;
        this.fileListField = fileListField;
        this.messageArea = messageArea;
        this.progressBar = progressBar;
    }

    @Override
    void body() {
        String sourceText = sourceDirectoryField.getText();
        String targetText = targetDirectoryField.getText();
        String fileListText = fileListField.getText();

        Path sourcePath = Paths.get(sourceText);
        Path targetPath = Paths.get(targetText);
        Path fileListPath = Paths.get(fileListText);
        Configuration configuration = new Configuration(sourcePath, targetPath, fileListPath);

        CopyFilesTask task = new CopyFilesTask(configuration);
        task.setMode(mode);
        task.addListener(createCopyFilesTaskListener(messageArea));
        task.addSubTaskListener(createCopyFileSubTaskListener(messageArea));
        try {
            task.run();
        }
        catch(TaskException e) {
            messageArea.appendText(e.getMessage());
            e.printStackTrace();
        }
    }

    private TaskListener<List<CopyTaskResult>> createCopyFilesTaskListener(TextArea messageArea) {
        return new TaskListener<List<CopyTaskResult>>() {

            @Override
            public void doBefore() {
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
                List<CopyTaskResult> exceptionResults = results.stream()
                        .filter(CopyTaskResult::hasException)
                        .collect(Collectors.toList());
                successfullyResults.removeAll(exceptionResults);

                showResults(successfullyResults, exceptionResults);
            }

            private void showResults(List<CopyTaskResult> successfullyResults, List<CopyTaskResult> exceptionResults) {
                StringBuilder builder = new StringBuilder();
                builder.append("\n--------------------------------------------------------------------------------------------------\n")
                        .append("Summary ")
                        .append(getModeCaption())
                        .append(" result:\n")
                        .append("Copy successful (")
                        .append(successfullyResults.size())
                        .append(" count)\n");
                if(!exceptionResults.isEmpty()) {
                    builder.append("Copy unsuccessfully (")
                            .append(exceptionResults.size())
                            .append(" count)\n");
                    exceptionResults.forEach(result -> {
                        TaskException exception = result.getException();
                        builder.append(exception.getMessage()).append("\n");
                        exception.printStackTrace();
                    });
                }
                messageArea.appendText(builder.toString());
            }
        };
    }

    private TaskListener<CopyTaskResult> createCopyFileSubTaskListener(TextArea messageArea) {
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
                builder.append(result.hasException() ? "Not coped " : "Coped ")
                        .append("\"").append(targetFile.getFileName().toString()).append("\"")
                        .append(" file from ")
                        .append("\"").append(sourceFile.getParent().toString()).append("\"")
                        .append(" to ")
                        .append("\"").append(targetFile.getParent().toString()).append("\"")
                        .append("\n");
                if(result.hasException()) {
                    builder.append(result.getException().getMessage())
                            .append("\n");
                }
                messageArea.appendText(builder.toString());
            }
        };
    }

    public void enableCopyMode() {
        mode = TaskMode.ACTION;
    }

    public void enableValidationMode() {
        mode = TaskMode.VALIDATION;
    }
}
