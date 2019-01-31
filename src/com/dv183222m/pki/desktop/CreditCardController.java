package com.dv183222m.pki.desktop;

import com.dv183222m.pki.data.DbContext;
import com.dv183222m.pki.data.Request;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class CreditCardController {
    private static Request request;

    @FXML DatePicker expiration;
    @FXML TextField cvv;
    @FXML TextField holder;
    @FXML TextField cardNumber;

    @FXML
    public void initialize() {
        expiration.setValue(LocalDate.now());
    }

    public static void setRequest(Request request) {
        CreditCardController.request = request;
    }

    public void create(ActionEvent actionEvent) throws IOException {
        if (cardNumber.getText().isEmpty()
                || holder.getText().isEmpty()
                || cvv.toString().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.NONE,"All data is required.", ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
            return;
        }

        if(asDate(expiration.getValue()).before(Calendar.getInstance().getTime())){
            Alert alert = new Alert(Alert.AlertType.NONE,"Credit card is expired.", ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
            return;
        }

        DbContext.INSTANCE.submitRequest(request.getId());

        request = null;
        Parent root = FXMLLoader.load(getClass().getResource("worker_details.fxml"));
        Main.PRIMARY_STAGE.setScene(new Scene(root, 800, 600));
        Main.PRIMARY_STAGE.show();

        Alert alert = new Alert(Alert.AlertType.NONE,"Request created.", ButtonType.OK);
        alert.setTitle("Info");
        alert.show();
    }

    public void back(ActionEvent actionEvent) throws IOException {
        request = null;
        Parent root = FXMLLoader.load(getClass().getResource("create_request.fxml"));
        Main.PRIMARY_STAGE.setScene(new Scene(root, 800, 600));
        Main.PRIMARY_STAGE.show();
    }

    private Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
}
