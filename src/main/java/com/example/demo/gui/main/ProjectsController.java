package com.example.demo.gui.main;

import com.example.demo.lib.fields.CustomCheckBox;
import com.example.demo.lib.models.Project;
import com.example.demo.lib.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.Objects;

public class ProjectsController extends BaseController {

    @FXML
    public ListView<Hyperlink> projectsList;
    @FXML
    public Label greetingLabel;
    public ListView<CustomCheckBox> usersWithAccess;

    @FXML
    private TextField nameTextField;

    @FXML
    private void createProject() {
        String projectName = nameTextField.getText();
        List<CustomCheckBox> choiceUsers = usersWithAccess.getItems().filtered(CustomCheckBox::isSelected).stream().toList();
        User[] users = new User[choiceUsers.size() + 1];
        users[0] = user;
        for (CustomCheckBox i : choiceUsers)
            users[choiceUsers.indexOf(i)+1] = i.getUser();

        try {
            dao.createProject(projectName, users);
        } catch (Throwable e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error: " + e.getMessage());
            alert.show();
        }

        updateUI();
    }

    @Override
    public void initialize(Object arg) {
        super.initialize(arg);

        for (User i : dao.fetchUsers())
            if (!Objects.equals(i.getLogin(), user.getLogin()))
                usersWithAccess.getItems().add(new CustomCheckBox(i));
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
