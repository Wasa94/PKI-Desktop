package com.dv183222m.pki.desktop;

import com.dv183222m.pki.data.DbContext;
import com.dv183222m.pki.data.Request;
import com.dv183222m.pki.data.WorkerType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class CreateRequestController {
    @FXML ComboBox type;
    @FXML ComboBox payment;
    @FXML DatePicker dateFrom;
    @FXML DatePicker dateTo;
    @FXML TextField municipality;
    @FXML TextField address;
    @FXML TextField price;
    @FXML TextArea details;

    @FXML
    public void initialize(){
        initTypes();
        initPaymant();
        initDates();

        price.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    price.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    private void initTypes() {
        final ObservableList<String> types = FXCollections.observableArrayList();
        List<WorkerType> workerTypes = WorkerDetailsController.getWorker().getWorker().getTypes();
        for (WorkerType workerType : workerTypes) {
            types.add(workerType.getWorkType());
        }

        type.getItems().addAll(types);
    }

    private void initPaymant() {
        final ObservableList<String> paymentTypes = FXCollections.observableArrayList();
        paymentTypes.add("Credit Card");
        paymentTypes.add("Cash");

        payment.getItems().addAll(paymentTypes);
    }

    private void initDates() {
        dateFrom.setValue(LocalDate.now());
        dateTo.setValue(LocalDate.now());
    }

    public void back(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("worker_details.fxml"));
        Main.PRIMARY_STAGE.setScene(new Scene(root, 800, 600));
        Main.PRIMARY_STAGE.show();
    }

    public void createRequest(ActionEvent actionEvent) throws IOException {
        if (type.getSelectionModel().isEmpty()
                || payment.getSelectionModel().isEmpty()
                || price.getText().isEmpty()
                || details.getText().isEmpty()
                || municipality.getText().isEmpty()
                || address.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.NONE,"All data is required.", ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
            return;
        }

        if (asDate(dateFrom.getValue()).after(asDate(dateTo.getValue()))) {
            Alert alert = new Alert(Alert.AlertType.NONE,"Date \"TO\" must be after \"BEFORE\".", ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
            return;
        }

        String typeString = type.getSelectionModel().getSelectedItem().toString();
        int priceInt = 0;
        try {
            priceInt = Integer.parseInt(price.getText());
        }
        catch(NumberFormatException e)
        {
            return;
        }

        boolean creditCard = payment.getSelectionModel().getSelectedItem().toString().equals("Credit Card");

        Request request = new Request(ProfileController.getUser(), WorkerDetailsController.getWorker(), municipality.getText(),
                address.getText(), asDate(dateFrom.getValue()), asDate(dateTo.getValue()), WorkerType.getType(typeString),
                creditCard, priceInt, details.getText());
        DbContext.INSTANCE.createRequest(request);

        if (creditCard) {
            CreditCardController.setRequest(request);
            Parent root = FXMLLoader.load(getClass().getResource("credit_card.fxml"));
            Main.PRIMARY_STAGE.setScene(new Scene(root, 800, 600));
            Main.PRIMARY_STAGE.show();
        } else {
            DbContext.INSTANCE.submitRequest(request.getId());

            Parent root = FXMLLoader.load(getClass().getResource("worker_details.fxml"));
            Main.PRIMARY_STAGE.setScene(new Scene(root, 800, 600));
            Main.PRIMARY_STAGE.show();

            Alert alert = new Alert(Alert.AlertType.NONE,"Request created.", ButtonType.OK);
            alert.setTitle("Info");
            alert.show();
        }
    }

    private Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
}
