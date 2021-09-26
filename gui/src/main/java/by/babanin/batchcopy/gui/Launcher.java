package by.babanin.batchcopy.gui;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL mainSceneResource = getClass().getResource("/fxml/BatchCopyScene.fxml");
        if(mainSceneResource != null) {
            Parent root = FXMLLoader.load(mainSceneResource);
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();
        }
    }
}
