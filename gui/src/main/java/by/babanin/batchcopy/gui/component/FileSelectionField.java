package by.babanin.batchcopy.gui.component;

import by.babanin.batchcopy.application.util.PathUtils;
import by.babanin.batchcopy.gui.action.AbstractActionHandler;
import by.babanin.batchcopy.gui.action.FileSelectionHandler;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class FileSelectionField extends PathSelectionField {

    public FileSelectionField(TextField fileField, Button chooseButton) {
        super(fileField, chooseButton);
    }

    @Override
    protected void validatePathField(ObservableList<String> validation, TextField fileField) {
        validation.clear();
        validation.addAll(PathUtils.validateReadableFile(getPath()));
    }

    @Override
    protected AbstractActionHandler<ActionEvent> createSelectionPathHandler(TextField pathField) {
        return new FileSelectionHandler(pathField, this);
    }
}
