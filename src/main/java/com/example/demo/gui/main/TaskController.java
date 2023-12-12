package com.example.demo.gui.main;

import com.example.demo.lib.models.Comment;
import com.example.demo.lib.models.Task;
import com.example.demo.lib.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class TaskController extends BaseController<Integer> {
    @FXML
    public Label greetingLabel;
    @FXML
    public Label projectNameLabel;
    public TextField textTextField;
    public Label taskNameLabel;
    public ListView<Label> commentsList;
    public Label taskExecutorsLabel;

    @Override
    public void updateUI() {
        if (getUser()) return;
        commentsList.getItems().clear();


        try {
            Comment[] resultSet = dao.fetchComments(arg);
            for (Comment i : resultSet) {
                String name = i.getText();

                Label project = new Label(name);
                commentsList.getItems().add(project);
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
            Task task = dao.getTaskById(arg);
            projectNameLabel.setText("Project: " + dao.getProjectById(task.getProjectId()).getName());
            taskNameLabel.setText("Task: " + task.getName());
            StringBuilder executors = new StringBuilder(10000);
            User[] executorsArray = dao.getTaskExecutors(arg);
            for (int i = 0; i < executorsArray.length; i++) {
                executors.append(executorsArray[i].getLogin()).append(" <").append(executorsArray[i].getEmail()).append(">");
                if (i < executorsArray.length - 1) executors.append(", ");
            }
            taskExecutorsLabel.setText("Executors: " + executors);
        } catch (Throwable e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error: " + e.getMessage());
            alert.show();
        }
    }

    public void createComment() {
        String commentText = textTextField.getText();
        try {
            dao.createComment(commentText, arg);
        } catch (Throwable e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error: " + e.getMessage());
            alert.show();
        }

        updateUI();
    }
}
