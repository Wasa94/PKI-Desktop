package com.dv183222m.pki.desktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage PRIMARY_STAGE;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("ClientWorker App");
        primaryStage.sizeToScene();
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setResizable(false);
        primaryStage.show();

        PRIMARY_STAGE = primaryStage;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
