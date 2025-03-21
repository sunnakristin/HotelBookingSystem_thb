package assignment4_thb;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoomController {


    @FXML
    private ComboBox<String> roomTypeComboBox;

    @FXML
    private TextField numberOfGuests;

    @FXML
    private DatePicker checkInDatePicker;

    @FXML
    private DatePicker checkOutDatePicker;

    @FXML
    private VBox resultsContainer;

    private List<Room> rooms = new ArrayList<>();

    // Initializes the controller class.
    @FXML
    private void initialize() {
        // Initialize your ComboBox with room types, if not already filled via FXML
        addSampleRooms();
        roomTypeComboBox.getItems().addAll("Single", "Double", "Suite", "Any"); // example room types
        // Set default values or constraints for DatePicker
        checkInDatePicker.setValue(LocalDate.now()); // default to today's date
        checkOutDatePicker.setValue(LocalDate.now().plusDays(1)); // default to tomorrow
    }

    // Method to handle the search for available rooms
    @FXML
    private void onSearch() {
        resultsContainer.getChildren().clear();

        String selectedRoomType = roomTypeComboBox.getValue();
        int numberOfGuestsInt = Integer.parseInt(numberOfGuests.getText());
        LocalDate checkInDate = checkInDatePicker.getValue();
        LocalDate checkOutDate = checkOutDatePicker.getValue();

        //verður að standast
        if (checkInDate == null || checkOutDate == null || checkInDate.isAfter(checkOutDate)) {
            showAlert("Check-in date must be before check-out date and dates must not be null.");
            return;
        }

        for (Room room : rooms) {
            if (room.getType().equals(selectedRoomType) || selectedRoomType.equals("Any")) {
                VBox roomBox = new VBox(10);
                roomBox.getChildren().add(new Label("Type: " + room.getType()));
                roomBox.getChildren().add(new Label("Price: $" + room.getPrice() + " per night"));
                Button bookButton = new Button("Book Now");
                //bookButton.setOnAction(event -> bookRoom(room));
                //roomBox.getChildren().add(bookButton);
                resultsContainer.getChildren().add(roomBox);
            }
        }
    }

    // Utility method to show alerts
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void addSampleRooms() {
        Room standardRoom = new Room("Standard", 100.0, true);
        Room deluxeRoom = new Room("Deluxe", 200.0, true);
        Room suiteRoom = new Room("Suite", 300.0, true);
        rooms.add(standardRoom);
        rooms.add(deluxeRoom);
        rooms.add(suiteRoom);
    }
}