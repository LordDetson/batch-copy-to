package by.babanin.batchcopy.gui.component;

import by.babanin.batchcopy.application.util.PathUtils;
import by.babanin.batchcopy.gui.action.AbstractActionHandler;
import by.babanin.batchcopy.gui.action.DirectorySelectionHandler;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class DirectorySelectionField extends PathSelectionField {

    public DirectorySelectionField(TextField directoryField, Button chooseButton) {
        super(directoryField, chooseButton);
    }

    @Override
    protected void validatePathField(ObservableList<String> validation, TextField directoryField) {
        validation.clear();
        validation.addAll(PathUtils.validateDirectory(getPath()));
    }

    @Override
    protected AbstractActionHandler<ActionEvent> createSelectionPathHandler(TextField pathField) {
        return new DirectorySelectionHandler(pathField, this);
    }
}
