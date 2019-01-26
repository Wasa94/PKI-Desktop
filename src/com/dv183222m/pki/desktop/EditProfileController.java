package com.dv183222m.pki.desktop;

import com.dv183222m.pki.data.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;

public class EditProfileController {
    @FXML
    Label username;
    @FXML ImageView image;
    @FXML TextField firstName;
    @FXML TextField lastName;
    @FXML TextField address;
    @FXML TextField phoneNumber;

    private User user;

    @FXML
    public void initialize() {
        user = ProfileController.getUser();

        username.setText("Welcome, " + user.getUsername());

        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        address.setText(user.getAddress());
        phoneNumber.setText(user.getPhoneNumber());

        if (user.getImage() != null && !user.getImage().isEmpty()) {
            File file = new File(user.getImage());
            image.setImage(new Image(file.toURI().toString()));
        }
    }

    public void save(ActionEvent actionEvent) throws IOException {
        user.setFirstName(firstName.getText());
        user.setLastName(lastName.getText());
        user.setAddress(address.getText());
        user.setPhoneNumber(phoneNumber.getText());

        finishEdit();
    }

    public void cancel(ActionEvent actionEvent) throws IOException {
        finishEdit();
    }

    private void finishEdit() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("profile.fxml"));
        Main.PRIMARY_STAGE.setScene(new Scene(root, 800, 600));
        Main.PRIMARY_STAGE.show();
    }
}
