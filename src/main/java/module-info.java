module com.example.billiard {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.billiard to javafx.fxml;
    exports com.example.billiard;

}