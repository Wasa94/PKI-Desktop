package com.dv183222m.pki.desktop;

import com.dv183222m.pki.data.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import java.io.IOException;

public class ChangePasswordController {
    public static Stage CHANGE_PASSWORD;

    @FXML PasswordField oldPassword;
    @FXML PasswordField newPassword;
    @FXML PasswordField confirmPassword;

    public void cancel(ActionEvent actionEvent) {
        CHANGE_PASSWORD.close();
    }

    public void submit(ActionEvent actionEvent) throws IOException {
        User user = ProfileController.getUser();

        String oldPass = oldPassword.getText();
        if (!oldPass.equals(user.getPassword())) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Wrong old password.", ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
            return;
        }

        String newPass = newPassword.getText();
        String confirmPass = confirmPassword.getText();

        if (newPass.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.NONE, "New password cannot be empty.", ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
            return;
        }

        if (!newPass.equals(confirmPass)) {
            Alert alert = new Alert(Alert.AlertType.NONE, "New password doesn't match.", ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
            return;
        }

        if (newPass.equals(oldPass)) {
            Alert alert = new Alert(Alert.AlertType.NONE, "New and old password cannot match.", ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
            return;
        }

        user.setPassword(newPass);

        Alert alert = new Alert(Alert.AlertType.NONE, "Password changed.", ButtonType.OK);
        alert.setTitle("Error");
        alert.show();


        ProfileController.setUser(null);
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Main.PRIMARY_STAGE.setScene(new Scene(root, 800, 600));
        Main.PRIMARY_STAGE.show();
        CHANGE_PASSWORD.close();
    }
}
