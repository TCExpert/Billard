module com.example.billiard {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;


    opens com.example.billiard to javafx.fxml;
    exports com.example.billiard;

}