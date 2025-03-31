package assignment4_thb;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        addSampleRooms();
        roomTypeComboBox.getItems().addAll("Standard", "Deluxe", "Suite", "Any"); // example room types
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

        //vil birta skilaboð/try-catch, fyrir ef ógilt slegið inn í numberOfPeople

        for (Room room : rooms) { // Þarf að birta skilaboð ef ekkert fannst
            if((room.checkAvailability(checkInDate, checkOutDate)) && (room.getNumberOfguests() == numberOfGuestsInt)){ //þarf að setja inn í þessa function, mögulega með book.checkavailabitly- skilar alltaf true núna og er í room
                VBox roomBox = new VBox(10);
                if (selectedRoomType.equals("Any")) {
                    if(numberOfGuestsInt == 1){
                        roomBox.getChildren().add(new Label("Type: " + room.getType() + ", price: $" + room.getPrice() + " per night, suitable for " + room.getNumberOfguests() +" person\n"));
                    }
                    else{
                        roomBox.getChildren().add(new Label("Type: " + room.getType() + ", price: $" + room.getPrice() + " per night, suitable for " + room.getNumberOfguests() + " people\n"));
                    }
                    Button bookButton = new Button("Book Now");
                    roomBox.getChildren().add(bookButton); //bætum við takka til að geta bókað. Vil fara þaðan í viðmótið í Booking
                    resultsContainer.getChildren().add(roomBox);
                    resultsContainer.getChildren().add(bookButton);
                    VBox.setMargin(bookButton, new Insets(0, 0, 10, 0));

                } else if (room.getType().equals(selectedRoomType)){
                    if(numberOfGuestsInt == 1){
                        roomBox.getChildren().add(new Label("Type: " + room.getType() + ", price: $" + room.getPrice() + " per night, suitable for " + room.getNumberOfguests() +" person\n"));
                    }
                    else{
                        roomBox.getChildren().add(new Label("Type: " + room.getType() + ", price: $" + room.getPrice() + " per night, suitable for " + room.getNumberOfguests() + " people\n"));
                    }
                    roomBox.getChildren().add(new Label("Suitable for " + room.getNumberOfguests() + "people\n"));
                    Button bookButton = new Button("Book Now");
                    roomBox.getChildren().add(bookButton);
                    resultsContainer.getChildren().add(roomBox);
                }
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
        Room standardRoom1 = new Room("Standard", 100.0, 1, true);
        Room standardRoom2 = new Room("Standard", 100.0, 2, true);
        Room standardRoom3 = new Room("Standard", 200.0, 3, true);
        Room standardRoom4 = new Room("Standard", 200.0, 4, true);
        Room deluxeRoom1 = new Room("Deluxe", 400.0,2, true);
        Room deluxeRoom2 = new Room("Deluxe", 200.0,4, true);
        Room deluxeRoom3 = new Room("Deluxe", 500.0,5, true);
        Room deluxeRoom4 = new Room("Deluxe", 200.0,1, true);
        Room suiteRoom1 = new Room("Suite", 300.0,1, true);
        Room suiteRoom2 = new Room("Suite", 500.0,3, true);
        Room suiteRoom3 = new Room("Suite", 300.0,2, true);
        Room suiteRoom4 = new Room("Suite", 300.0,3, true);

        rooms.add(standardRoom1);
        rooms.add(standardRoom2);
        rooms.add(standardRoom3);
        rooms.add(standardRoom4);
        rooms.add(deluxeRoom1);
        rooms.add(deluxeRoom2);
        rooms.add(deluxeRoom3);
        rooms.add(deluxeRoom4);
        rooms.add(suiteRoom1);
        rooms.add(suiteRoom2);
        rooms.add(suiteRoom3);
        rooms.add(suiteRoom4);
    }
}