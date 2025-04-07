module com.gamesaver {
    requires javafx.controls;
    requires javafx.fxml;
    requires okhttp3;
    requires java.sql;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires java.desktop;


    opens com.gamesaver to javafx.fxml;
    opens com.gamesaver.app to javafx.fxml;
    opens com.gamesaver.controller to javafx.fxml;
    opens com.gamesaver.model to javafx.base;
    exports com.gamesaver.app;
    exports com.gamesaver.controller;
    exports  com.gamesaver.model to com.fasterxml.jackson.databind;
}