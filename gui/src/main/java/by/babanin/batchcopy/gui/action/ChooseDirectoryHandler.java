package by.babanin.batchcopy.gui.action;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

public class ChooseDirectoryHandler extends AbstractActionHandler<ActionEvent> {

    private final TextField pathField;

    public ChooseDirectoryHandler(TextField pathField) {
        this.pathField = pathField;
    }

    @Override
    void body() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(pathField.getScene().getWindow());
        if(file != null) {
            pathField.setText(file.getPath());
        }
    }
}
