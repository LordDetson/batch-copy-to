package by.babanin.batchcopy.gui.component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.collections.ObservableListWrapper;

import by.babanin.batchcopy.gui.action.AbstractActionHandler;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public abstract class PathSelectionField {

    private final TextField pathField;
    private final Button selectionButton;
    private final ObservableList<String> validation;

    protected PathSelectionField(TextField pathField, Button selectionButton) {
        this.pathField = pathField;
        this.selectionButton = selectionButton;
        this.validation = new ObservableListWrapper<>(new ArrayList<>());

        initialise();
    }

    private void initialise() {
        validation.addListener(createValidationListener(pathField));
        pathField.setOnKeyReleased(event -> validatePathField(validation, pathField));
        selectionButton.setOnAction(createSelectionPathHandler(pathField));
    }

    private ListChangeListener<? super String> createValidationListener(TextField pathField) {
        return change -> setupValidationFieldStyle(pathField, change.getList().isEmpty());
    }

    private void setupValidationFieldStyle(TextField pathField, boolean valid) {
        pathField.setStyle(valid ? "" : "-fx-text-box-border: red ; -fx-focus-color: red ;");
    }

    public void addValidationListener(ListChangeListener<? super String> listener) {
        validation.addListener(listener);
    }

    public void setPath(Path path) {
        pathField.setText(path.toString());
        validatePathField(validation, pathField);
    }

    public Path getPath() {
        return Paths.get(pathField.getText());
    }

    public List<String> getValidation() {
        return new ArrayList<>(validation);
    }

    public boolean isValid() {
        return validation.isEmpty();
    }

    protected abstract void validatePathField(ObservableList<String> validation, TextField pathField);

    protected abstract AbstractActionHandler<ActionEvent> createSelectionPathHandler(TextField pathField);

}
