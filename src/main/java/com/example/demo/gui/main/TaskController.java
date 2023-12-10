package com.example.demo.gui.main;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.sql.ResultSet;

public class TaskController extends BaseController<Integer> {
    @FXML
    public Label greetingLabel;
    @FXML
    public Label projectNameLabel;
    public TextField textTextField;
    public Label taskNameLabel;
    public ListView<Label> commentsList;

    @Override
    public void updateUI() {
        if (!getUser()) return;
        commentsList.getItems().clear();


        try (ResultSet resultSet = dao.fetchComments(arg)) {
            while (resultSet.next()) {
                String name = resultSet.getString("text");

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
        try (ResultSet task = dao.getTaskById(arg)) {
            projectNameLabel.setText("Project: " + dao.getProjectById(task.getInt("projectId")).getString("name"));
            taskNameLabel.setText("Task: " + task.getString("name"));
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
