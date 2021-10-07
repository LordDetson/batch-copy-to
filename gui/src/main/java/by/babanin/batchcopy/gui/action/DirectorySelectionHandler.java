package by.babanin.batchcopy.gui.action;

import java.io.File;

import by.babanin.batchcopy.gui.component.DirectorySelectionField;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

public class DirectorySelectionHandler extends AbstractActionHandler<ActionEvent> {

    private final TextField pathField;
    private final DirectorySelectionField directoryField;

    public DirectorySelectionHandler(TextField pathField, DirectorySelectionField directoryField) {
        this.pathField = pathField;
        this.directoryField = directoryField;
    }

    @Override
    public void body() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(pathField.getScene().getWindow());
        if(file != null) {
            directoryField.setPath(file.toPath());
        }
    }
}
