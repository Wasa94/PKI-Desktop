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

public class RegisterController {
    @FXML
    TextField firstName;
    @FXML
    TextField lastName;
    @FXML
    TextField address;
    @FXML
    TextField phoneNumber;
    @FXML
    TextField username;
    @FXML
    PasswordField password;

    public void cancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Main.PRIMARY_STAGE.setScene(new Scene(root, 800, 600));
        Main.PRIMARY_STAGE.show();
    }

    public void register(ActionEvent actionEvent) throws IOException {
        if (firstName.getText().isEmpty() || lastName.getText().isEmpty() || address.getText().isEmpty()
                || phoneNumber.getText().isEmpty() || username.getText().isEmpty() || password.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.NONE, "All data is required.", ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
            return;
        }

        User user = new User(firstName.getText(), lastName.getText(), UserType.Client, address.getText(),
                phoneNumber.getText(), username.getText(), password.getText());

        boolean result = DbContext.INSTANCE.addUser(user);

        if (result == false) {
            Alert alert = new Alert(Alert.AlertType.NONE,
                    "User \"" + user.getUsername() + "\" already exists.", ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.NONE, "User created.", ButtonType.OK);
            alert.setTitle("Info");
            alert.show();

            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Main.PRIMARY_STAGE.setScene(new Scene(root, 800, 600));
            Main.PRIMARY_STAGE.show();
        }
    }
}
