package com.example.demo.gui.main;

import com.example.demo.lib.models.Project;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
            dao.createProject(projectName, user.getId());
        } catch (Throwable e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error: " + e.getMessage());
            alert.show();
        }

        updateUI();
    }

    @Override
    public void updateUI() {
        if (getUser()) return;
        projectsList.getItems().clear();

        try {
            Project[] resultSet = dao.fetchProjects(user.getId());
            for (Project i : resultSet) {
                Hyperlink project = new Hyperlink(i.getName());
                project.setOnMouseClicked(event -> viewProject(i.getId()));
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
