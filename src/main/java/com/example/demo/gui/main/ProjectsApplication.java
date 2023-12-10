package com.example.demo.gui.main;

import com.example.demo.gui.BaseApplication;
import javafx.stage.Stage;

public class ProjectsApplication extends BaseApplication {

    @Override
    public void start(Stage stage) {
        launch(stage,"main/projects", "My Projects");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
