package com.example.demo.gui.main;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.ResultSet;

public class ProjectController extends BaseController<Integer> {

    @FXML
    public ListView<Hyperlink> tasksList;
    @FXML
    public Label greetingLabel;
    @FXML
    public Label projectNameLabel;

    @FXML
    private TextField nameTextField;

    @Override
    public void updateUI() {
        if (!getUser()) return;
        tasksList.getItems().clear();


        try (ResultSet resultSet = dao.fetchTasks(arg)) {
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int id = resultSet.getInt("id");

                Hyperlink project = new Hyperlink(name);
                project.setOnMouseClicked(event -> viewProject(id));
                tasksList.getItems().add(project);
            }
        } catch (Throwable e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error: " + e.getMessage());
            alert.show();
        }
    }

    @Override
    public void initialize(Integer arg) {
        super.initialize(arg);
        try {
            projectNameLabel.setText("Project: " + dao.getProjectById(arg).getString("name"));
        } catch (Throwable e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error: " + e.getMessage());
            alert.show();
        }
    }

    private void viewProject(int id) {
        new TaskApplication(id).start(stage);
    }

    public void createTask() {
        String taskName = nameTextField.getText();
        try {
            dao.createTask(taskName, arg);
        } catch (Throwable e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error: " + e.getMessage());
            alert.show();
        }

        updateUI();
    }
}
