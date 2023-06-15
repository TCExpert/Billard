module com.example.billiard {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.example.billiard to javafx.graphics;
    exports com.example.billiard;

}