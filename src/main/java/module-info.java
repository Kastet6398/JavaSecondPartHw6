module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.sql;
    requires java.jwt;
    requires freemarker;

    // Lib
    exports com.example.demo.lib.exceptions;
    exports com.example.demo.lib;

    // GUI
    exports com.example.demo.gui.auth;
    opens com.example.demo.gui.auth to javafx.fxml;

    exports com.example.demo.gui.main;
    opens com.example.demo.gui.main to javafx.fxml;
    exports com.example.demo.gui;
    opens com.example.demo.gui to javafx.fxml;
}