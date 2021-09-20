package by.babanin.batchcopy.gui.action;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import by.babanin.batchcopy.application.CopyFilesTask;
import by.babanin.batchcopy.application.CopyTaskResult;
import by.babanin.batchcopy.application.TaskListener;
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
        task.addListener(createCopyFilesTaskListener(messageArea));
        task.addListenerToSubTasks(createCopyFileSubTaskListener(messageArea));
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
                messageArea.setText("Starting...\n");
            }

            @Override
            public void doAfter(List<CopyTaskResult> results) {
                List<CopyTaskResult> successfullyResults = new ArrayList<>(results);
                List<CopyTaskResult> exceptionResults = results.stream()
                        .filter(CopyTaskResult::hasException)
                        .collect(Collectors.toList());
                successfullyResults.removeAll(exceptionResults);
                StringBuilder builder = new StringBuilder();
                builder.append("\n-------------------------------------------------\n");
                builder.append("Summary result:\n");
                builder.append("Copy successfully (")
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
                if(!result.hasException()) {
                    Path targetFile = result.getTargetFile();
                    Path sourceFile = result.getSourceFile();
                    builder.append("Coped ")
                            .append(targetFile.getFileName().toString())
                            .append(" file from ")
                            .append(sourceFile.getParent().toString())
                            .append(" to ")
                            .append(targetFile.getParent().toString())
                            .append("\n");
                }
                else {
                    builder.append("Not coped: ")
                            .append(result.getException().getMessage())
                            .append("\n");
                }
                messageArea.appendText(builder.toString());
            }
        };
    }
}
