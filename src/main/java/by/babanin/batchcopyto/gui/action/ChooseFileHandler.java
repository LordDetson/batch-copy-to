package by.babanin.batchcopyto.gui.action;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class ChooseFileHandler extends AbstractActionHandler<ActionEvent> {

    private final TextField pathField;

    public ChooseFileHandler(TextField pathField) {
        this.pathField = pathField;
    }

    @Override
    void body() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(pathField.getScene().getWindow());
        if(file != null) {
            pathField.setText(file.getPath());
        }
    }
}
