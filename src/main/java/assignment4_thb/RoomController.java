package assignment4_thb.vidmot;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RoomController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}