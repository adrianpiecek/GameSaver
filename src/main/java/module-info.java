module com.gamesaver {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.gamesaver to javafx.fxml;
    exports com.gamesaver;
}