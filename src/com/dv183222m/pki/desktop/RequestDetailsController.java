package com.dv183222m.pki.desktop;

import com.dv183222m.pki.data.DbContext;
import com.dv183222m.pki.data.Request;
import com.dv183222m.pki.data.RequestStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.Rating;

import java.io.IOException;
import java.util.Calendar;
import java.util.Optional;

public class RequestDetailsController {
    private static Request request;

    @FXML Label types;
    @FXML Label worker;
    @FXML Label from;
    @FXML Label to;
    @FXML Label price;
    @FXML Label status;
    @FXML Label details;
    @FXML Rating rating;
    @FXML Label review;
    @FXML StackPane cardReview;
    @FXML Button cancelRequest;

    public static void setRequest(Request request) {
        RequestDetailsController.request = request;
    }

    public static Request getRequest() {
        return request;
    }

    @FXML
    public void initialize() {
        types.setText(request.getType());
        worker.setText("Worker: " + request.getWorker().getFullName());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(request.getFrom());
        from.setText("From: " + String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) + "." +
                String.format("%02d", calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR) + ".");

        calendar.setTime(request.getTo());
        to.setText("To: " + String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) + "." +
                String.format("%02d", calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR) + ".");

        String cashOrCreditCard = request.isCreditCard() ? "Credit Card" : "Cash";
        price.setText("Price: " + request.getPrice() + " RSD - " + cashOrCreditCard);

        status.setText("Status: " + request.getStatus().name());
        details.setText("Details: " + request.getDetails());

        rating.setRating(request.getRating());
        review.setText(request.getReview());

        if(request.getStatus() == RequestStatus.New) {
            cancelRequest.setVisible(true);
        } else if(request.getStatus() == RequestStatus.Successful || request.getStatus() == RequestStatus.Unsuccessful) {
            cardReview.setVisible(true);
        }
    }

    public void back(ActionEvent actionEvent) throws IOException {
        request = null;
        Parent root = FXMLLoader.load(getClass().getResource("requests.fxml"));
        Main.PRIMARY_STAGE.setScene(new Scene(root, 800, 600));
        Main.PRIMARY_STAGE.show();
    }

    public void share(ActionEvent actionEvent) {

    }

    public void review(MouseEvent mouseEvent) {

    }

    public void cancelRequest(ActionEvent actionEvent)  throws IOException {
        Alert alert = new Alert(Alert.AlertType.NONE, "Are you sure you want to cancel selected request?",
                ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Cancel Request");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            requestCancel();
        }
    }

    private void requestCancel()  throws IOException {
        boolean status = DbContext.INSTANCE.cancelRequest(RequestDetailsController.getRequest().getId());
        if (status) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Request canceled.", ButtonType.OK);
            alert.setTitle("Info");
            alert.show();

            RequestDetailsController.setRequest(null);
            Parent root = FXMLLoader.load(getClass().getResource("requests.fxml"));
            Main.PRIMARY_STAGE.setScene(new Scene(root, 800, 600));
            Main.PRIMARY_STAGE.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.NONE, "Could not cancel request.", ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
        }
    }
}
