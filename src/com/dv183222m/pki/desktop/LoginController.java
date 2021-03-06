package com.dv183222m.pki.desktop;

import com.dv183222m.pki.data.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {
    @FXML
    TextField username;
    @FXML
    PasswordField password;

    public void login(ActionEvent actionEvent) throws IOException {
        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Please enter username and password.",
                    ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
            return;
        }

        User user = DbContext.INSTANCE.getUser(username.getText());

        if (user == null) {
            Alert alert = new Alert(Alert.AlertType.NONE,
                    "\"" + username.getText() + "\" is not associated with any account.", ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
        } else if (user.getPassword().equals(password.getText())) {
            if(user.getType().equals(UserType.Worker)) {
                Alert alert = new Alert(Alert.AlertType.NONE,
                        "Only clients can use desktop app.", ButtonType.OK);
                alert.setTitle("Error");
                alert.showAndWait();
                return;
            }
            ProfileController.setUser(user);
            Parent root = FXMLLoader.load(getClass().getResource("profile.fxml"));
            Main.PRIMARY_STAGE.setScene(new Scene(root, 800, 600));
            Main.PRIMARY_STAGE.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.NONE,"The password you’ve entered is incorrect.",
                    ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
        }
    }

    public void workers(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("workers.fxml"));
        Main.PRIMARY_STAGE.setScene(new Scene(root, 800, 600));
        Main.PRIMARY_STAGE.show();
    }

    public void register(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
        Main.PRIMARY_STAGE.setScene(new Scene(root, 800, 600));
        Main.PRIMARY_STAGE.show();
    }
}
