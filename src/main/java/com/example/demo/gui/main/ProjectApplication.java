package com.example.demo.gui.main;

import com.example.demo.gui.BaseApplication;
import javafx.stage.Stage;

public class ProjectApplication extends BaseApplication<Integer> {

    public ProjectApplication(int id) {
        super();
        this.arg = id;
    }

    @Override
    public void start(Stage stage) {
        System.out.println(arg);
        launch(stage,"main/project", "Project " + arg, arg);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
