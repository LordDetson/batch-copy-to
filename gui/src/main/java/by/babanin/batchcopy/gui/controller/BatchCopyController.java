package by.babanin.batchcopy.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import by.babanin.batchcopy.gui.action.ChooseDirectoryHandler;
import by.babanin.batchcopy.gui.action.ChooseFileHandler;
import by.babanin.batchcopy.gui.action.CopyEnableListener;
import by.babanin.batchcopy.gui.action.CopyHandler;
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

        ChooseDirectoryHandler sourceDirectoryChooseHandler = new ChooseDirectoryHandler(sourceDirectoryField);
        ChooseDirectoryHandler targetDirectoryChooseHandler = new ChooseDirectoryHandler(targetDirectoryField);
        ChooseFileHandler fileListChooseHandler = new ChooseFileHandler(fileListField);

        CopyEnableListener copyEnableListener = new CopyEnableListener(sourceDirectoryField, targetDirectoryField, fileListField,
                copyButton);
        sourceDirectoryChooseHandler.addListener(copyEnableListener);
        targetDirectoryChooseHandler.addListener(copyEnableListener);
        fileListChooseHandler.addListener(copyEnableListener);

        sourceDirectoryButton.setOnAction(sourceDirectoryChooseHandler);
        targetDirectoryButton.setOnAction(targetDirectoryChooseHandler);
        fileListButton.setOnAction(fileListChooseHandler);
        copyButton.setOnAction(new CopyHandler(sourceDirectoryField, targetDirectoryField, fileListField, messageArea, progressBar));
    }
}
