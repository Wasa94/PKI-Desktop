package com.dv183222m.pki.desktop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.SegmentedButton;

public class ShareRequestController {
    public static Stage SHARE_REQUEST;

    @FXML TextField username;
    @FXML PasswordField password;
    @FXML SegmentedButton buttons;

    @FXML
    public void initialize() {
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty() && !password.getText().isEmpty()) {
                buttons.setDisable(false);
            } else {
                buttons.setDisable(true);
            }
        });

        password.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!username.getText().isEmpty() && !newValue.isEmpty()) {
                buttons.setDisable(false);
            } else {
                buttons.setDisable(true);
            }
        });
    }

    public void submit(ActionEvent actionEvent) {
        SHARE_REQUEST.close();

        Alert alert = new Alert(Alert.AlertType.NONE, "Worker shared succesfully.", ButtonType.OK);
        alert.setTitle("Info");
        alert.showAndWait();
    }

    public void cancel(ActionEvent actionEvent) {
        SHARE_REQUEST.close();
    }
}
