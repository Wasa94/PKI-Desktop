package com.dv183222m.pki.desktop;

import com.dv183222m.pki.data.Request;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;

import java.io.IOException;

public class RequestReviewController {
    public static Stage REQUEST_REVIEW;

    @FXML Rating rating;
    @FXML TextArea review;

    public void submit(ActionEvent actionEvent) throws IOException {
        Request request = RequestDetailsController.getRequest();
        request.setReview(review.getText());
        request.setRating((float)rating.getRating());

        Parent root = FXMLLoader.load(getClass().getResource("request_details.fxml"));
        Main.PRIMARY_STAGE.setScene(new Scene(root, 800, 600));
        Main.PRIMARY_STAGE.show();

        REQUEST_REVIEW.close();
    }

    public void cancel(ActionEvent actionEvent) {
        REQUEST_REVIEW.close();
    }
}
