module com.gamesaver {
    requires javafx.controls;
    requires javafx.fxml;
    requires okhttp3;
    requires java.sql;
    requires com.fasterxml.jackson.databind;


    opens com.gamesaver to javafx.fxml;
    exports com.gamesaver.app;
    opens com.gamesaver.app to javafx.fxml;
    exports com.gamesaver.controller;
    opens com.gamesaver.controller to javafx.fxml;
}