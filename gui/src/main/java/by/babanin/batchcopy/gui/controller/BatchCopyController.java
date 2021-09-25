package by.babanin.batchcopy.gui.controller;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.sun.javafx.collections.ObservableListWrapper;

import by.babanin.batchcopy.application.util.PathUtils;
import by.babanin.batchcopy.gui.action.ChooseDirectoryHandler;
import by.babanin.batchcopy.gui.action.ChooseFileHandler;
import by.babanin.batchcopy.gui.action.CopyHandler;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class BatchCopyController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField sourceDirectoryField;

    private ObservableList<String> sourceDirectoryValidation;

    @FXML
    private Button sourceDirectoryButton;

    @FXML
    private TextField targetDirectoryField;

    private ObservableList<String> targetDirectoryValidation;

    @FXML
    private Button targetDirectoryButton;

    @FXML
    private TextField fileListField;

    private ObservableList<String> fileListValidation;

    @FXML
    private Button fileListButton;

    @FXML
    private TextArea messageArea;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button copyButton;

    @FXML
    private Button validButton;

    @FXML
    void initialize() {
        assert sourceDirectoryButton != null : "fx:id=\"sourceDirectoryButton\" was not injected: check your FXML file 'Untitled'.";
        assert fileListButton != null : "fx:id=\"fileListButton\" was not injected: check your FXML file 'Untitled'.";
        assert progressBar != null : "fx:id=\"progressBar\" was not injected: check your FXML file 'Untitled'.";
        assert messageArea != null : "fx:id=\"messageArea\" was not injected: check your FXML file 'Untitled'.";
        assert copyButton != null : "fx:id=\"copyButton\" was not injected: check your FXML file 'Untitled'.";
        assert sourceDirectoryField != null : "fx:id=\"sourceDirectoryField\" was not injected: check your FXML file 'Untitled'.";
        assert targetDirectoryButton != null : "fx:id=\"targetDirectoryButton\" was not injected: check your FXML file 'Untitled'.";
        assert fileListField != null : "fx:id=\"fileListField\" was not injected: check your FXML file 'Untitled'.";
        assert targetDirectoryField != null : "fx:id=\"targetDirectoryField\" was not injected: check your FXML file 'Untitled'.";

        initialiseComponents();
        addListeners();
        validateAllFields();
        actionEnabling();
    }

    private void initialiseComponents() {
        sourceDirectoryValidation = createObservableValidation();
        targetDirectoryValidation = createObservableValidation();
        fileListValidation = createObservableValidation();

        String currentPath = Paths.get("").toAbsolutePath().toString();
        sourceDirectoryField.setText(currentPath);
        targetDirectoryField.setText(currentPath);
        fileListField.setText(currentPath);
    }

    private ObservableList<String> createObservableValidation() {
        return new ObservableListWrapper<>(new ArrayList<>());
    }

    private void addListeners() {
        sourceDirectoryValidation.addListener(createValidationListener(sourceDirectoryField));
        targetDirectoryValidation.addListener(createValidationListener(targetDirectoryField));
        fileListValidation.addListener(createValidationListener(fileListField));

        sourceDirectoryField.setOnKeyReleased(event -> validateDirectoryField(sourceDirectoryValidation, sourceDirectoryField));
        targetDirectoryField.setOnKeyReleased(event -> validateDirectoryField(targetDirectoryValidation, targetDirectoryField));
        fileListField.setOnKeyReleased(event -> validateReadableFileField(fileListValidation, fileListField));

        sourceDirectoryButton.setOnAction(createChooseDirectoryHandler(sourceDirectoryField, sourceDirectoryValidation));
        targetDirectoryButton.setOnAction(createChooseDirectoryHandler(targetDirectoryField, targetDirectoryValidation));
        fileListButton.setOnAction(createChooseFileHandler(fileListField, fileListValidation));
        CopyHandler copyHandler = new CopyHandler(sourceDirectoryField, targetDirectoryField, fileListField, messageArea, progressBar);
        copyButton.setOnAction(event -> {
            copyHandler.enableCopyMode();
            copyHandler.handle(event);
        });
        validButton.setOnAction(event -> {
            copyHandler.enableValidationMode();
            copyHandler.handle(event);
        });
    }

    private void validateAllFields() {
        validateDirectoryField(sourceDirectoryValidation, sourceDirectoryField);
        validateDirectoryField(targetDirectoryValidation, targetDirectoryField);
        validateReadableFileField(fileListValidation, fileListField);
    }

    private ListChangeListener<? super String> createValidationListener(TextField textField) {
        return change -> {
            List<String> messages = new ArrayList<>();
            messages.addAll(sourceDirectoryValidation);
            messages.addAll(targetDirectoryValidation);
            messages.addAll(fileListValidation);
            messageArea.setText(String.join("\n", messages));
            setupValidationFieldStyle(textField, change.getList().isEmpty());
            actionEnabling();
        };
    }

    private void validateDirectoryField(List<String> messages, TextField directoryField) {
        messages.clear();
        Path directory = Paths.get(directoryField.getText());
        messages.addAll(PathUtils.validateDirectory(directory));
    }

    private void validateReadableFileField(List<String> messages, TextField fileField) {
        messages.clear();
        Path file = Paths.get(fileField.getText());
        messages.addAll(PathUtils.validateReadableFile(file));
    }

    private void setupValidationFieldStyle(TextField directoryField, boolean valid) {
        directoryField.setStyle(valid ? "" : "-fx-text-box-border: red ; -fx-focus-color: red ;");
    }

    private void actionEnabling() {
        boolean enableButtons = !sourceDirectoryValidation.isEmpty() || !targetDirectoryValidation.isEmpty() || !fileListValidation.isEmpty();
        copyButton.setDisable(enableButtons);
        validButton.setDisable(enableButtons);
    }

    private ChooseDirectoryHandler createChooseDirectoryHandler(TextField directoryField, List<String> messages) {
        ChooseDirectoryHandler handler = new ChooseDirectoryHandler(directoryField);
        handler.addListener(() -> validateDirectoryField(messages, directoryField));
        return handler;
    }

    private ChooseFileHandler createChooseFileHandler(TextField fileField, List<String> messages) {
        ChooseFileHandler handler = new ChooseFileHandler(fileField);
        handler.addListener(() -> validateReadableFileField(messages, fileField));
        return handler;
    }
}
