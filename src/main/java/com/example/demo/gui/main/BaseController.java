package com.example.demo.gui.main;

import com.example.demo.gui.auth.LoginApplication;
import com.example.demo.gui.auth.RegistrationApplication;
import com.example.demo.lib.Auth;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.ResultSet;

public abstract class BaseController<I> extends com.example.demo.gui.BaseController<I> {

    @FXML
    public VBox loggedIn;
    @FXML
    public VBox loggedOut;
    @FXML
    public Label greetingLabel;

    protected ResultSet user;

    @Override
    public void initialize(I arg) {
        super.initialize(arg);
        updateUI();
    }

    protected boolean getUser() {
        try {
            user = Auth.getUser(dao);
            greetingLabel.setText("Hello, " + user.getString("login") + "!");
            return true;
        } catch (Throwable e) {
            loggedIn.setVisible(false);
            loggedOut.setVisible(true);
            return false;
        }
    }

    public abstract void updateUI();

    @FXML
    protected void logout() throws IOException {
        Auth.logout();
        new RegistrationApplication().start(stage);
    }


    @FXML
    protected void login(ActionEvent event) throws IOException {
        new LoginApplication().start(stage);
    }

    @FXML
    protected void register(ActionEvent event) throws IOException {
        new RegistrationApplication().start(stage);
    }
}
