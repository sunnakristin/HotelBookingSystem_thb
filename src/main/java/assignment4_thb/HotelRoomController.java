package assignment4_thb;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

import java.time.LocalDate;

public class HotelRoomController {
/*

ER EKKI AÐ  NOTA!!


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

    @FXML
    private Button backButton;

    private List<HotelRoom> rooms;
    private List<Hotel> hotels;
    private Random showAvailable = new Random(); //notað til að sýna hvaða herbergi eru laus


    // Initializes the controller class.
    @FXML
    private void initialize() {
        rooms = new ArrayList<>();
        hotels = new ArrayList<>();

        //addSampleData();
        roomTypeComboBox.getItems().addAll("Standard", "Deluxe", "Suite", "Any"); // example room types
        roomTypeComboBox.setValue("Any");
        checkInDatePicker.setValue(LocalDate.now()); // default to today's date
        checkOutDatePicker.setValue(LocalDate.now().plusDays(1)); // default to tomorrow

    }

    // Að leita af herbergi fyrir hótelið, þarf að taka inn hótel?
    @FXML
    private void onSearch() {
        resultsContainer.getChildren().clear();
        String selectedRoomType = roomTypeComboBox.getValue();
        boolean isNumberOfGuestsEmpty = numberOfGuests.getText().isEmpty();
        int numberOfGuestsInt = 0;
        if (!isNumberOfGuestsEmpty) {
            numberOfGuestsInt = Integer.parseInt(numberOfGuests.getText());
        }

        LocalDate checkInDate = checkInDatePicker.getValue();
        LocalDate checkOutDate = checkOutDatePicker.getValue();

        //verður að standast
        if (checkInDate == null || checkOutDate == null || checkInDate.isAfter(checkOutDate)) {
            showAlert();
            return;
        }

        for (HotelRoom room : rooms) { // Þarf að birta skilaboð ef ekkert fannst
            VBox roomBox = new VBox(10);
            if(showAvailable.nextBoolean()) {
                if ((selectedRoomType.equals("Any") || room.getType().equals(selectedRoomType)) && (isNumberOfGuestsEmpty || numberOfGuestsInt == room.getNumberOfguests())){
                    roomBox.getChildren().add(new Label("Type: " + room.getType() + ", price: $" + room.getPrice() + " per night, suitable for " + room.getNumberOfguests() + (room.getNumberOfguests() == 1 ? " person" : " people") + "\n"));
                    Button bookButton = new Button("Book Now");
                    bookButton.setOnAction(event -> {
                        try {
                            handleBooking(room);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    roomBox.getChildren().add(bookButton); //bætum við takka til að geta bókað. Vil fara þaðan í viðmótið í Booking
                    resultsContainer.getChildren().add(roomBox);
                    resultsContainer.getChildren().add(bookButton);
                    VBox.setMargin(bookButton, new Insets(0, 0, 10, 0));
                }
            }
        }
        if(resultsContainer.getChildren().isEmpty()) {
            VBox noAvailableBox = new VBox(10);
            Label noAvailableLabel = new Label("No available rooms found for the selected criteria.");
            noAvailableBox.getChildren().add(noAvailableLabel);
            resultsContainer.getChildren().add(noAvailableBox);
        }
    }

    @FXML
    private void onBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assignment4_thb/hello-view.fxml"));
        Parent root = loader.load();
        // Create a new Stage (window)
        Stage stage = new Stage();
        stage.setTitle("My Profile");
        stage.setScene(new Scene(root));
        stage.showAndWait(); //get lagfært þannig að ekki hægt að vinna í fyrri á meðan
    }

    //það sem gerist þegar ýtt á bókunartakkann, mun nota til að far í bókunargluggann
    private void handleBooking(HotelRoom room) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assignment4_thb/booking-view.fxml"));
        loader.setController(new BookingController(new Booking(new Customer("aaa", "aaa", "aaa"), room)));        Parent root = loader.load();

        // Create a new Stage (window)
        Stage stage = new Stage();
        stage.setTitle("Booking Details");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    // Utility method to show alerts
    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Check-in date must be before check-out date and dates must not be null.");
        alert.showAndWait();
    }

    /*private void addSampleData() {
        Hotel hotel1 = new Hotel("Hótel 1","Reykjavík", null);
        Hotel hotel2 = new Hotel("Hótel 2", "Akureyri,", null);
        Hotel hotel3 = new Hotel("Hótel 3", "Selfoss,", null);
        Hotel hotel4 = new Hotel("Hótel 4", "Hveragerði,", null);
        hotels.add(hotel1);
        hotels.add(hotel2);
        hotels.add(hotel3);
        hotels.add(hotel4);

        HotelRoom standardRoom1 = new HotelRoom("Standard", 100.0, 1, hotels.get(0),true);
        HotelRoom standardRoom2 = new HotelRoom("Standard", 100.0, 2, hotels.get(0),true);
        HotelRoom standardRoom3 = new HotelRoom("Standard", 200.0, 3, hotels.get(0),true);
        HotelRoom standardRoom4 = new HotelRoom("Standard", 200.0, 4, hotels.get(1),true);
        HotelRoom deluxeRoom1 = new HotelRoom("Deluxe", 400.0,2, hotels.get(1),true);
        HotelRoom deluxeRoom2 = new HotelRoom("Deluxe", 200.0,4, hotels.get(1),true);
        HotelRoom deluxeRoom3 = new HotelRoom("Deluxe", 500.0,5, hotels.get(2),true);
        HotelRoom deluxeRoom4 = new HotelRoom("Deluxe", 200.0,1, hotels.get(2),true);
        HotelRoom suiteRoom1 = new HotelRoom("Suite", 300.0,1, hotels.get(2),true);
        HotelRoom suiteRoom2 = new HotelRoom("Suite", 500.0,3, hotels.get(3),true);
        HotelRoom suiteRoom3 = new HotelRoom("Suite", 300.0,2, hotels.get(3),true);
        HotelRoom suiteRoom4 = new HotelRoom("Suite", 300.0,3, hotels.get(3),true);

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
    }*/
}