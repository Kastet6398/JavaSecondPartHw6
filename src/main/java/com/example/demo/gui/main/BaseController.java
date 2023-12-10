package com.example.demo.gui.main;

import com.example.demo.gui.auth.LoginApplication;
import com.example.demo.gui.auth.RegistrationApplication;
import com.example.demo.lib.Auth;
import com.example.demo.lib.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public abstract class BaseController<I> extends com.example.demo.gui.BaseController<I> {

    @FXML
    public VBox loggedIn;
    @FXML
    public VBox loggedOut;
    @FXML
    public Label greetingLabel;

    protected User user;

    @Override
    public void initialize(I arg) {
        super.initialize(arg);
        updateUI();
    }

    protected boolean getUser() {
        try {
            user = Auth.getUser(dao);
            greetingLabel.setText("Hello, " + user.getLogin() + "!");
            return false;
        } catch (Throwable e) {
            loggedIn.setVisible(false);
            loggedOut.setVisible(true);
            return true;
        }
    }

    public abstract void updateUI();

    @FXML
    protected void logout() throws IOException {
        if (Auth.logout())
            new RegistrationApplication().start(stage);
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error logging out: cannot delete token file.");
            alert.show();
        }
    }


    @FXML
    protected void login() throws IOException {
        new LoginApplication().start(stage);
    }

    @FXML
    protected void register() throws IOException {
        new RegistrationApplication().start(stage);
    }
}
