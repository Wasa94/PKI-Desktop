package com.dv183222m.pki.desktop;

import com.dv183222m.pki.data.User;
import javafx.fxml.FXML;

public class WorkerDetailsController {
    private static User worker;

    public static void setWorker(User worker) {
        WorkerDetailsController.worker = worker;
    }

    @FXML
    public void initialize() {
        System.out.println(worker.getFullName());
    }
}
