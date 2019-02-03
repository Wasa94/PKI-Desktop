package com.dv183222m.pki.desktop;

import com.dv183222m.pki.data.DbContext;
import com.dv183222m.pki.data.Request;
import com.dv183222m.pki.data.RequestStatus;
import com.dv183222m.pki.data.WorkerType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.RangeSlider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RequestsController {

    @FXML TableView table;
    @FXML TextField worker;
    @FXML CheckComboBox types;
    @FXML CheckComboBox status;
    @FXML RangeSlider rangeSliderPrice;
    @FXML DatePicker dateFrom;
    @FXML DatePicker dateTo;
    @FXML Button details;
    @FXML Pagination pagination;

    @FXML Label username;

    private List<RequestAdapter> data;

    @FXML
    public void initialize() {
        if (ProfileController.getUser() != null) {
            username.setText("Welcome, " + ProfileController.getUser().getUsername());
        }

        TableColumn typeCol = new TableColumn("Type");
        TableColumn workerCol = new TableColumn("Worker");
        TableColumn priceCol = new TableColumn("Price");
        TableColumn fromCol = new TableColumn("From");
        TableColumn toCol = new TableColumn("To");
        TableColumn statusCol = new TableColumn("Status");

        table.getColumns().addAll(typeCol, workerCol, priceCol, fromCol, toCol, statusCol);

        typeCol.setCellValueFactory(new PropertyValueFactory<RequestAdapter, String>("type"));
        workerCol.setCellValueFactory(new PropertyValueFactory<RequestAdapter, String>("worker"));
        priceCol.setCellValueFactory(new PropertyValueFactory<RequestAdapter, String>("price"));
        fromCol.setCellValueFactory(new PropertyValueFactory<RequestAdapter, String>("from"));
        toCol.setCellValueFactory(new PropertyValueFactory<RequestAdapter, String>("to"));
        statusCol.setCellValueFactory(new PropertyValueFactory<RequestAdapter, String>("status"));

        List<Request> requestList = DbContext.INSTANCE.getRequestsClient(ProfileController.getUser().getUsername());
        populateTable(requestList);

        table.setFixedCellSize(30);
        table.prefHeightProperty().bind(table.fixedCellSizeProperty().multiply(6.00));
        table.minHeightProperty().bind(table.prefHeightProperty());
        table.maxHeightProperty().bind(table.prefHeightProperty());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        initMultiRange();
        initCheckComboBoxes();
        initDates();

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                details.setDisable(false);
            } else {
                details.setDisable(true);
            }
        });
    }

    private void initDates() {
        dateFrom.setValue(LocalDate.now());
        dateTo.setValue(LocalDate.now());
    }

    private Node createPage(int pageIndex) {

        int rowsPerPage = 5;

        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, data.size());
        int carry = (data.size() % rowsPerPage) == 0 ? 0 : 1;
        int numOfPages = (data.size() - data.size() % rowsPerPage) / rowsPerPage + carry;
        numOfPages = numOfPages <= 0 ? 1 : numOfPages;
        pagination.setPageCount(numOfPages);

        table.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));

        return new BorderPane(table);
    }

    private void initMultiRange() {
        rangeSliderPrice.setMax(20000);
        rangeSliderPrice.setMin(0);
        rangeSliderPrice.setHighValue(20000);
        rangeSliderPrice.setLowValue(0);
        rangeSliderPrice.setShowTickLabels(true);
        rangeSliderPrice.setShowTickMarks(true);
        rangeSliderPrice.setMajorTickUnit(5000);
        rangeSliderPrice.setMinorTickCount(4);
        rangeSliderPrice.setSnapToTicks(true);
    }

    private void populateTable(List<Request> requestList) {
        data = new ArrayList<>();
        for (Request request : requestList) {
            data.add(new RequestAdapter(request));
        }

        pagination.setPageFactory(this::createPage);
    }

    private void initCheckComboBoxes() {
        final ObservableList<String> typeStrings = FXCollections.observableArrayList();

        WorkerType[] workerTypes = WorkerType.values();
        for (WorkerType workerType : workerTypes) {
            typeStrings.add(workerType.name());
        }

        types.getItems().addAll(typeStrings);


        final ObservableList<String> statusStrings = FXCollections.observableArrayList();

        RequestStatus[] requestStatuses = RequestStatus.values();
        for (RequestStatus requestStatus : requestStatuses) {
            statusStrings.add(requestStatus.name());
        }

        status.getItems().addAll(statusStrings);
    }

    public void details(ActionEvent actionEvent) throws IOException {
        if (table.getSelectionModel() == null || table.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        RequestAdapter requestAdapter = (RequestAdapter) table.getSelectionModel().getSelectedItem();

        RequestDetailsController.setRequest(DbContext.INSTANCE.getRequest(requestAdapter.getId()));

        Parent root = FXMLLoader.load(getClass().getResource("request_details.fxml"));
        Main.PRIMARY_STAGE.setScene(new Scene(root, 800, 600));
        Main.PRIMARY_STAGE.show();
    }

    public void filter(ActionEvent actionEvent) {
        List<WorkerType> workerTypes = new ArrayList<>();
        for (Object type : types.getCheckModel().getCheckedItems()) {
            workerTypes.add(WorkerType.valueOf(type.toString()));
        }

        List<RequestStatus> requestStatuses = new ArrayList<>();
        for (Object status : status.getCheckModel().getCheckedItems()) {
            requestStatuses.add(RequestStatus.valueOf(status.toString()));
        }

        List<Request> requests = DbContext.INSTANCE.getRequestsClient(ProfileController.getUser().getUsername(),
                worker.getText(), workerTypes, requestStatuses,
                (int) rangeSliderPrice.getLowValue(), (int) rangeSliderPrice.getHighValue(),
                asDate(dateFrom.getValue()), asDate(dateTo.getValue()));

        populateTable(requests);
    }

    public void profile(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("profile.fxml"));
        Main.PRIMARY_STAGE.setScene(new Scene(root, 800, 600));
        Main.PRIMARY_STAGE.show();
    }

    public void workers(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("workers.fxml"));
        Main.PRIMARY_STAGE.setScene(new Scene(root, 800, 600));
        Main.PRIMARY_STAGE.show();
    }

    private Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
}
