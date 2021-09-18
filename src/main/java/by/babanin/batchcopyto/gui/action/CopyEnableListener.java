package by.babanin.batchcopyto.gui.action;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CopyEnableListener implements ActionListener {

    private final TextField sourceDirectoryField;
    private final TextField targetDirectoryField;
    private final TextField fileListField;
    private final Button button;

    public CopyEnableListener(TextField sourceDirectoryField, TextField targetDirectoryField, TextField fileListField, Button button) {
        this.sourceDirectoryField = sourceDirectoryField;
        this.targetDirectoryField = targetDirectoryField;
        this.fileListField = fileListField;
        this.button = button;
    }

    @Override
    public void finish() {
        String sourceText = sourceDirectoryField.getText();
        String targetText = targetDirectoryField.getText();
        String fileListText = fileListField.getText();

        if(!(sourceText.isEmpty() || targetText.isEmpty() || fileListText.isEmpty())) {
            Path sourcePath = Paths.get(sourceText);
            Path targetPath = Paths.get(targetText);
            Path fileListPath = Paths.get(fileListText);
            if(Files.exists(sourcePath) && Files.exists(targetPath) && Files.exists(fileListPath) && Files.isRegularFile(fileListPath)) {
                button.setDisable(false);
            }
        }
    }
}
