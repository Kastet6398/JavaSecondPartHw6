package com.example.demo.gui.auth;

import com.example.demo.gui.BaseApplication;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginApplication extends BaseApplication {
    @Override
    public void start(Stage stage) throws IOException {
        launch(stage, "auth/login", "Login");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
