package by.babanin.batchcopyto.gui.action;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import by.babanin.batchcopyto.application.FilesCopyTask;
import by.babanin.batchcopyto.domain.Configuration;
import by.babanin.batchcopyto.exception.ApplicationException;
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

        List<String> fileNames = Collections.emptyList();
        try {
            fileNames = Files.readAllLines(fileListPath);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        Set<Path> fileList = fileNames.stream()
                .map(Paths::get)
                .collect(Collectors.toSet());
        Configuration configuration = new Configuration(sourcePath, targetPath, fileList);

        FilesCopyTask task = new FilesCopyTask(configuration);
        try {
            messageArea.setText("Started");
            task.run();
            messageArea.setText("Finished");
        }
        catch(ApplicationException e) {
            messageArea.setText(e.getMessage());
            e.printStackTrace();
        }
    }
}
