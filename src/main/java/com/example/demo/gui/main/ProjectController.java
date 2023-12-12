package com.example.demo.gui.main;

import com.example.demo.lib.fields.CustomCheckBox;
import com.example.demo.lib.models.Task;
import com.example.demo.lib.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class ProjectController extends BaseController<Integer> {

    @FXML
    public ListView<Hyperlink> tasksList;
    @FXML
    public Label greetingLabel;
    @FXML
    public Label projectNameLabel;
    public ListView<CustomCheckBox> usersWithAccess;

    @FXML
    private TextField nameTextField;

    @Override
    public void updateUI() {
        if (getUser()) return;
        tasksList.getItems().clear();


        try {
            Task[] resultSet = dao.fetchTasks(arg);
            for (Task i : resultSet) {
                Hyperlink project = new Hyperlink(i.getName());
                project.setOnMouseClicked(event -> viewProject(i.getId()));
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
            projectNameLabel.setText("Project: " + dao.getProjectById(arg).getName());
        } catch (Throwable e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error: " + e.getMessage());
            alert.show();
        }

        for (User i : dao.getProjectUsers(arg))
            usersWithAccess.getItems().add(new CustomCheckBox(i));
    }

    private void viewProject(int id) {
        new TaskApplication(id).start(stage);
    }

    public void createTask() {
        String taskName = nameTextField.getText();
        List<CustomCheckBox> choiceUsers = usersWithAccess.getItems().filtered(CustomCheckBox::isSelected).stream().toList();
        User[] users = new User[choiceUsers.size()];
        for (CustomCheckBox i : choiceUsers)
            users[choiceUsers.indexOf(i)] = i.getUser();

        try {
            dao.createTask(taskName, arg, users);
        } catch (Throwable e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error: " + e.getMessage());
            alert.show();
        }

        updateUI();
    }
}
