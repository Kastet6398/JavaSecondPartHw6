open module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.sql;
    requires java.jwt;
    requires freemarker;

    // Lib
    exports com.example.demo.lib.exceptions;
    exports com.example.demo.lib.models;
    exports com.example.demo.lib;

    // GUI
    exports com.example.demo.gui.auth;
    exports com.example.demo.gui.main;
    exports com.example.demo.gui;
}