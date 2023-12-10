package com.example.demo.gui.main;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.ResultSet;

public class ProjectsController extends BaseController {

    @FXML
    public ListView<Hyperlink> projectsList;
    @FXML
    public Label greetingLabel;

    @FXML
    private TextField nameTextField;

    @FXML
    private void createProject() {
        String projectName = nameTextField.getText();

        try {
            dao.createProject(projectName, user.getInt("id"));
        } catch (Throwable e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error: " + e.getMessage());
            alert.show();
        }

        updateUI();
    }

    @Override
    public void updateUI() {
        if (!getUser()) return;
        projectsList.getItems().clear();

        try (ResultSet resultSet = dao.fetchProjects(user.getInt("id"))) {
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int id = resultSet.getInt("id");

                Hyperlink project = new Hyperlink(name);
                project.setOnMouseClicked(event -> viewProject(id));
                projectsList.getItems().add(project);
            }
        } catch (Throwable e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error: " + e.getMessage());
            alert.show();
        }
    }

    private void viewProject(int id) {
        new ProjectApplication(id).start(stage);
    }
}
