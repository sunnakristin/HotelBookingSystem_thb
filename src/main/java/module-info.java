module com.example.assignment4_thb {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.assignment4_thb to javafx.fxml;
    exports com.example.assignment4_thb;
}