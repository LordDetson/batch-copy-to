package by.babanin.batchcopy.gui.action;

import java.io.File;

import by.babanin.batchcopy.gui.component.FileSelectionField;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class FileSelectionHandler extends AbstractActionHandler<ActionEvent> {

    private final TextField pathField;
    private final FileSelectionField fileField;

    public FileSelectionHandler(TextField pathField, FileSelectionField fileField) {
        this.pathField = pathField;
        this.fileField = fileField;
    }

    @Override
    public void body() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Text File", "*.txt"));
        File file = fileChooser.showOpenDialog(pathField.getScene().getWindow());
        if(file != null) {
            fileField.setPath(file.toPath());
        }
    }
}
