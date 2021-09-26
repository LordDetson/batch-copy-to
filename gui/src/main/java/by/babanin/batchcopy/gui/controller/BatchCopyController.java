package by.babanin.batchcopy.gui.controller;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import by.babanin.batchcopy.gui.action.CopyHandler;
import by.babanin.batchcopy.gui.component.DirectorySelectionField;
import by.babanin.batchcopy.gui.component.FileSelectionField;
import javafx.collections.ListChangeListener;
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

    @FXML
    private Button sourceDirectoryButton;

    @FXML
    private TextField targetDirectoryField;

    @FXML
    private Button targetDirectoryButton;

    @FXML
    private TextField fileListField;

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

    private DirectorySelectionField chooseSourceDirectoryField;
    private DirectorySelectionField chooseTargetDirectoryField;
    private FileSelectionField chooseFileListField;

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

        Path currentPath = Paths.get("").toAbsolutePath();
        chooseSourceDirectoryField.setPath(currentPath);
        chooseTargetDirectoryField.setPath(currentPath);
        chooseFileListField.setPath(currentPath);
    }

    private void initialiseComponents() {
        chooseSourceDirectoryField = new DirectorySelectionField(sourceDirectoryField, sourceDirectoryButton);
        chooseTargetDirectoryField = new DirectorySelectionField(targetDirectoryField, targetDirectoryButton);
        chooseFileListField = new FileSelectionField(fileListField, fileListButton);
    }

    private void addListeners() {
        ListChangeListener<? super String> validationListener = createValidationListener();
        chooseSourceDirectoryField.addValidationListener(validationListener);
        chooseTargetDirectoryField.addValidationListener(validationListener);
        chooseFileListField.addValidationListener(validationListener);

        CopyHandler copyHandler = createCopyHandler();
        copyButton.setOnAction(event -> {
            copyHandler.enableCopyMode();
            copyHandler.handle(event);
        });
        validButton.setOnAction(event -> {
            copyHandler.enableValidationMode();
            copyHandler.handle(event);
        });
    }

    private CopyHandler createCopyHandler() {
        return new CopyHandler(chooseSourceDirectoryField, chooseTargetDirectoryField, chooseFileListField, messageArea, progressBar);
    }

    private ListChangeListener<? super String> createValidationListener() {
        return change -> {
            List<String> messages = new ArrayList<>();
            messages.addAll(chooseSourceDirectoryField.getValidation());
            messages.addAll(chooseTargetDirectoryField.getValidation());
            messages.addAll(chooseFileListField.getValidation());
            messageArea.setText(String.join("\n", messages));
            actionEnabling();
        };
    }

    private void actionEnabling() {
        boolean canCopy = canCopy();
        copyButton.setDisable(!canCopy);
        validButton.setDisable(!canCopy);
    }

    private boolean canCopy() {
        return chooseSourceDirectoryField.isValid() && chooseTargetDirectoryField.isValid() && chooseFileListField.isValid();
    }
}
