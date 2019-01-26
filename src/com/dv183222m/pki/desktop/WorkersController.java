package com.dv183222m.pki.desktop;

import com.dv183222m.pki.data.DbContext;
import com.dv183222m.pki.data.Worker;
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
import java.util.ArrayList;
import java.util.List;

public class WorkersController {
    @FXML TableView table;
    @FXML RangeSlider rangeSliderRating;
    @FXML RangeSlider rangeSliderExp;
    @FXML CheckComboBox checkComboBox;
    @FXML TextField firstName;
    @FXML TextField lastName;
    @FXML Button details;
    @FXML Pagination pagination;
    @FXML CheckBox checkBox;

    @FXML Label username;

    private List<WorkerAdapter> data;

    @FXML
    public void initialize() {
        if (username != null && ProfileController.getUser() != null) {
            username.setText("Welcome, " + ProfileController.getUser().getUsername());
        }

        TableColumn nameCol = new TableColumn("Name");
        TableColumn typesOfWorkCol = new TableColumn("Types of work");
        TableColumn experienceCol = new TableColumn("Experience");
        TableColumn ratingCol = new TableColumn("Rating");

        table.getColumns().addAll(nameCol, typesOfWorkCol, experienceCol, ratingCol);

        nameCol.setCellValueFactory(new PropertyValueFactory<WorkerAdapter, String>("name"));
        typesOfWorkCol.setCellValueFactory(new PropertyValueFactory<WorkerAdapter, String>("typesOfWork"));
        experienceCol.setCellValueFactory(new PropertyValueFactory<WorkerAdapter, String>("experience"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<WorkerAdapter, String>("Rating"));

        List<Worker> workerList = DbContext.INSTANCE.getWorkers();
        populateTable(workerList);

        table.setFixedCellSize(30);
        table.prefHeightProperty().bind(table.fixedCellSizeProperty().multiply(6.00));
        table.minHeightProperty().bind(table.prefHeightProperty());
        table.maxHeightProperty().bind(table.prefHeightProperty());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        initMultiRanges();
        initCheckComboBox();

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                details.setDisable(false);
            } else {
                details.setDisable(true);
            }
        });
    }

    private Node createPage(int pageIndex) {

        int rowsPerPage = 5;

        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, data.size());
        int carry = (data.size() % rowsPerPage) == 0 ? 0 : 1;
        int numOfPages = (data.size() - data.size() % rowsPerPage) / rowsPerPage + carry;
        pagination.setPageCount(numOfPages);

        table.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));

        return new BorderPane(table);
    }

    public void login(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Main.PRIMARY_STAGE.setScene(new Scene(root, 800, 600));
        Main.PRIMARY_STAGE.show();
    }

    private void initMultiRanges() {

        rangeSliderRating.setMax(5);
        rangeSliderRating.setMin(0);
        rangeSliderRating.setHighValue(5);
        rangeSliderRating.setLowValue(0);
        rangeSliderRating.setShowTickLabels(true);
        rangeSliderRating.setShowTickMarks(true);
        rangeSliderRating.setMajorTickUnit(1);
        rangeSliderRating.setMinorTickCount(4);
        rangeSliderRating.setSnapToTicks(true);

        rangeSliderExp.setMax(20);
        rangeSliderExp.setMin(0);
        rangeSliderExp.setHighValue(20);
        rangeSliderExp.setLowValue(0);
        rangeSliderExp.setShowTickLabels(true);
        rangeSliderExp.setShowTickMarks(true);
        rangeSliderExp.setMajorTickUnit(5);
        rangeSliderExp.setMinorTickCount(4);
        rangeSliderExp.setSnapToTicks(true);
    }

    private void populateTable(List<Worker> workerList) {
        data = new ArrayList<>();
        for (Worker worker : workerList) {
            data.add(new WorkerAdapter(worker));
        }

        pagination.setPageFactory(this::createPage);
    }

    private void initCheckComboBox() {
        final ObservableList<String> strings = FXCollections.observableArrayList();

        WorkerType[] workerTypes = WorkerType.values();
        for (WorkerType workerType : workerTypes) {
            strings.add(workerType.name());
        }

        checkComboBox.getItems().addAll(strings);
    }

    public void details(ActionEvent actionEvent) throws IOException {
        if (table.getSelectionModel() == null || table.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        WorkerAdapter workerAdapter = (WorkerAdapter) table.getSelectionModel().getSelectedItem();

        WorkerDetailsController.setWorker(DbContext.INSTANCE.getUser(workerAdapter.getUsername()));

        Parent root = FXMLLoader.load(getClass().getResource("worker_details.fxml"));
        Main.PRIMARY_STAGE.setScene(new Scene(root, 800, 600));
        Main.PRIMARY_STAGE.show();
    }

    public void filter(ActionEvent actionEvent) {
        List<WorkerType> workerTypes = new ArrayList<>();
        for (Object type : checkComboBox.getCheckModel().getCheckedItems()) {
            workerTypes.add(WorkerType.valueOf(type.toString()));
        }

        List<Worker> workers;
        if (ProfileController.getUser() == null || !checkBox.isSelected()) {
            workers = DbContext.INSTANCE.getWorkers(firstName.getText(), lastName.getText(), workerTypes,
                    (float) rangeSliderRating.getLowValue(), (float) rangeSliderRating.getHighValue(),
                    (float) rangeSliderExp.getLowValue(), (float) rangeSliderExp.getHighValue());
        } else {
            workers = DbContext.INSTANCE.getWorkersLogedin(firstName.getText(), lastName.getText(), workerTypes,
                    (float) rangeSliderRating.getLowValue(), (float) rangeSliderRating.getHighValue(),
                    (float) rangeSliderExp.getLowValue(), (float) rangeSliderExp.getHighValue(), checkBox.isSelected(), ProfileController.getUser().getUsername());
        }

        populateTable(workers);
    }

    public void profile(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("profile.fxml"));
        Main.PRIMARY_STAGE.setScene(new Scene(root, 800, 600));
        Main.PRIMARY_STAGE.show();
    }

    public void requests(ActionEvent actionEvent) {
    }
}
