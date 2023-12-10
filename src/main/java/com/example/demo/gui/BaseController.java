package com.example.demo.gui;

import com.example.demo.lib.Dao;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public abstract class BaseController<I> {
    public I arg;

    public Stage stage;
    public Dao dao;

    public void initialize(I arg) {
        this.arg = arg;

        try {
            dao = new Dao();
        } catch (Throwable e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error: " + e.getMessage());
            alert.show();
        }
    }
}
