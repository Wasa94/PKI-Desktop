package com.dv183222m.pki.desktop;

import com.dv183222m.pki.data.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ProfileController {
    private static User user;

    @FXML Label username;
    @FXML ImageView image;
    @FXML Label firstName;
    @FXML Label lastName;
    @FXML Label address;
    @FXML Label phoneNumber;

    @FXML
    public void initialize() {
        username.setText("Welcome, " + user.getUsername());

        firstName.setText("First Name: " + user.getFirstName());
        lastName.setText("Last Name: " + user.getLastName());
        address.setText("Address: " + user.getAddress());
        phoneNumber.setText("Phone: " + user.getPhoneNumber());

        if(user.getImage() != null && !user.getImage().isEmpty()) {
            File file = new File(user.getImage());
            image.setImage(new Image(file.toURI().toString()));
        }
    }

    public static void setUser(User user) {
        ProfileController.user = user;
    }

    public static User getUser() {
        return user;
    }

    public void logout(ActionEvent actionEvent) throws IOException {
        user = null;
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Main.PRIMARY_STAGE.setScene(new Scene(root, 800, 600));
        Main.PRIMARY_STAGE.show();
    }

    public void changePassword(ActionEvent actionEvent) throws IOException {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(Main.PRIMARY_STAGE);

        Parent root = FXMLLoader.load(getClass().getResource("change_password.fxml"));
        dialog.setTitle("Change Password");
        dialog.sizeToScene();
        dialog.setScene(new Scene(root));
        dialog.setResizable(false);
        dialog.show();

        ChangePasswordController.CHANGE_PASSWORD = dialog;
    }

    public void edit(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("edit_profile.fxml"));
        Main.PRIMARY_STAGE.setScene(new Scene(root, 800, 600));
        Main.PRIMARY_STAGE.show();
    }

    public void requests(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("requests.fxml"));
        Main.PRIMARY_STAGE.setScene(new Scene(root, 800, 600));
        Main.PRIMARY_STAGE.show();
    }

    public void workers(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("workers.fxml"));
        Main.PRIMARY_STAGE.setScene(new Scene(root, 800, 600));
        Main.PRIMARY_STAGE.show();
    }
}
