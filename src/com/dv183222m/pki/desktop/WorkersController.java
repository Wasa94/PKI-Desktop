package com.dv183222m.pki.desktop;

import com.dv183222m.pki.data.DbContext;
import com.dv183222m.pki.data.Worker;
import com.dv183222m.pki.data.WorkerType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.RangeSlider;

import java.util.ArrayList;
import java.util.List;

public class WorkersController {
    @FXML
    TableView table;
    @FXML
    RangeSlider rangeSliderRating;
    @FXML
    RangeSlider rangeSliderExp;
    @FXML
    CheckComboBox checkComboBox;

    @FXML
    public void initialize() {
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

        table.setFixedCellSize(25);
        table.prefHeightProperty().bind(table.fixedCellSizeProperty().multiply(6.00).add(1.01));
        table.minHeightProperty().bind(table.prefHeightProperty());
        table.maxHeightProperty().bind(table.prefHeightProperty());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        initMultiRanges();
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
        List<WorkerAdapter> workerAdapters = new ArrayList<>();
        for (Worker worker : workerList) {
            workerAdapters.add(new WorkerAdapter(worker));
        }

        final ObservableList<WorkerAdapter> data = FXCollections.observableArrayList(workerAdapters);

        table.setItems(data);
        initCheckComboBox();
    }

    private void initCheckComboBox() {
        final ObservableList<String> strings = FXCollections.observableArrayList();

        WorkerType[] workerTypes = WorkerType.values();
        for (WorkerType workerType : workerTypes) {
            strings.add(workerType.name());
        }

        checkComboBox.getItems().addAll(strings);
    }

    public void details(ActionEvent actionEvent) {
        System.out.println("Rating: " + rangeSliderRating.getLowValue() + " " + rangeSliderRating.getHighValue());
        System.out.println("Exp: " + rangeSliderExp.getLowValue() + " " + rangeSliderExp.getHighValue());
        System.out.println("Types: " + checkComboBox.getCheckModel().getCheckedItems());
    }
}
