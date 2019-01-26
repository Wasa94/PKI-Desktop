package com.dv183222m.pki.desktop;

import com.dv183222m.pki.data.User;
import com.dv183222m.pki.data.WorkerType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.Rating;

import java.io.File;
import java.io.IOException;

public class WorkerDetailsController {
    private static User worker;

    @FXML ImageView image;
    @FXML Label firstName;
    @FXML Label lastName;
    @FXML Label address;
    @FXML Label phoneNumber;
    @FXML Label types;
    @FXML Rating rating;
    @FXML Label experience;

    @FXML Button login;
    @FXML Button createRequest;

    @FXML
    public void initialize() {
        if(ProfileController.getUser() == null) {
            login.setVisible(true);
        } else {
            createRequest.setVisible(true);
        }

        firstName.setText("First Name: " + worker.getFirstName());
        lastName.setText("Last Name: " + worker.getLastName());
        address.setText("Address: " + worker.getAddress());
        phoneNumber.setText("Phone: " + worker.getPhoneNumber());

        StringBuilder sb = new StringBuilder();
        for (WorkerType type: worker.getWorker().getTypes()) {
            if(sb.toString().isEmpty() == false) {
                sb.append("    ");
            }
            sb.append(type.name());
        }
        types.setText(sb.toString());

        rating.setRating(worker.getWorker().getRating());
        String years = worker.getWorker().getExperience() == 1 ? " year" : " years";
        experience.setText(worker.getWorker().getExperience() + years + " of experience");

        if(worker.getImage() != null && !worker.getImage().isEmpty()) {
            File file = new File(worker.getImage());
            image.setImage(new Image(file.toURI().toString()));
        }
    }

    public static void setWorker(User worker) {
        WorkerDetailsController.worker = worker;
    }

    public void login(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Main.PRIMARY_STAGE.setScene(new Scene(root, 800, 600));
        Main.PRIMARY_STAGE.show();
    }

    public void back(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("workers.fxml"));
        Main.PRIMARY_STAGE.setScene(new Scene(root, 800, 600));
        Main.PRIMARY_STAGE.show();
    }
}
