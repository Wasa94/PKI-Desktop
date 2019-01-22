package com.dv183222m.pki.desktop;

import com.dv183222m.pki.data.DbContext;
import com.dv183222m.pki.data.Worker;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import multirange.MultiRange;

import java.util.ArrayList;
import java.util.List;

public class WorkersController {
    @FXML
    TableView table;
    @FXML
    MultiRange multiRangeRating;
    @FXML
    MultiRange multiRangeExp;

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

        multiRangeRating.setMax(5);
        multiRangeRating.setMin(0);
        multiRangeRating.setShowTickLabels(true);
        multiRangeRating.setShowTickMarks(true);
        multiRangeRating.setMajorTickUnit(1);
        multiRangeRating.setMinorTickCount(4);
        multiRangeRating.setSnapToTicks(true);

        multiRangeExp.setMax(20);
        multiRangeExp.setMin(0);
        multiRangeExp.setShowTickLabels(true);
        multiRangeExp.setShowTickMarks(true);
        multiRangeExp.setMajorTickUnit(5);
        multiRangeExp.setMinorTickCount(4);
        multiRangeExp.setSnapToTicks(true);
    }

    private void populateTable(List<Worker> workerList) {
        List<WorkerAdapter> workerAdapters = new ArrayList<>();
        for (Worker worker : workerList) {
            workerAdapters.add(new WorkerAdapter(worker));
        }

        final ObservableList<WorkerAdapter> data = FXCollections.observableArrayList(workerAdapters);

        table.setItems(data);
    }

    public void details(ActionEvent actionEvent) {
        System.out.println(multiRangeRating.getLowValue() + " " + multiRangeRating.getHighValue());
        System.out.println(multiRangeExp.getLowValue() + " " + multiRangeExp.getHighValue());
    }
}
