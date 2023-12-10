package com.example.demo.gui.main;

import com.example.demo.gui.BaseApplication;
import javafx.stage.Stage;

public class TaskApplication extends BaseApplication<Integer> {

    public TaskApplication(int id) {
        super();
        this.arg = id;
    }

    @Override
    public void start(Stage stage) {
        launch(stage,"main/task", "Task " + arg, arg);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
