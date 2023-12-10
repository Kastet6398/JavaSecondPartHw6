package com.example.demo.gui.auth;

import com.example.demo.gui.BaseController;
import com.example.demo.gui.main.ProjectsApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import static com.example.demo.lib.Auth.login;

public class LoginController extends BaseController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    public void loginUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        try {
            login(username, password, dao);
            new ProjectsApplication().start(stage);
        } catch (Throwable e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error: " + e.getMessage());
            alert.show();
        }
    }

    public void registerScreen() {
        try {
            new RegistrationApplication().start(stage);
        } catch (Throwable e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error: " + e.getMessage());
            alert.show();
        }
    }
}