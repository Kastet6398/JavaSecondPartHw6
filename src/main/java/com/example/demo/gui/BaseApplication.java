package com.example.demo.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

public abstract class BaseApplication<I> extends Application {
    public I arg;

    @Override
    public abstract void start(Stage stage) throws IOException;

    public void launch(Stage stage, String template, String title, I arg) {
        this.arg = arg;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/demo/" + template + ".fxml"));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root, 400, 400);
            stage.setScene(scene);
            stage.setTitle(title);

            BaseController<I> controller = fxmlLoader.getController();
            controller.stage = stage;
            controller.initialize(arg);

            stage.show();
        } catch (Throwable e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error: " + e.getMessage());
            alert.show();
        }
    }
    public void launch(Stage stage, String template, String title) {
        try {
            if (isParameterizedWithGenerics()) throw new IllegalArgumentException("Must provide an additional argument");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/demo/" + template + ".fxml"));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root, 400, 400);
            stage.setScene(scene);
            stage.setTitle(title);

            BaseController<I> controller = fxmlLoader.getController();
            controller.stage = stage;
            controller.initialize(null);

            stage.show();
        } catch (Throwable e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error: " + e.getMessage());
            alert.show();
        }
    }

    private <T> boolean isParameterizedWithGenerics() {

        if (getClass().getGenericSuperclass() instanceof ParameterizedType parameterizedType)
            return parameterizedType.getActualTypeArguments().length > 0;

        return false;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
