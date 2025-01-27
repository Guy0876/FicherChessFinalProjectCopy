module com.example.ficherchess {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ficherchess to javafx.fxml;
    exports com.example.ficherchess;
    exports com.example.ficherchess.Pieces;
    opens com.example.ficherchess.Pieces to javafx.fxml;
}