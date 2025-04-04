module com.example.assignment4_thb {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;

    opens assignment4_thb to javafx.fxml;

    exports assignment4_thb;
}