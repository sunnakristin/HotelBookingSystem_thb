package assignment4_thb;

import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SearchController {
    @FXML private TableView<Hotel> hotelTable;
    @FXML private TableColumn<Hotel, String> nameColumn;
    @FXML private TableColumn<Hotel, String> locationColumn;
    @FXML private TableColumn<Hotel, String> descriptionColumn;
    @FXML private TextField searchField;
    @FXML private VBox hotelDetailsContainer;
    @FXML private Label selectedHotelLabel;
    @FXML private ComboBox<String> roomTypeComboBox;
    @FXML private TextField numberOfGuests;
    @FXML private DatePicker checkInDatePicker;
    @FXML private DatePicker checkOutDatePicker;
    @FXML private TableView<HotelRoom> availableRoomsTable;
    @FXML private TableColumn<HotelRoom, String> roomTypeColumn;
    @FXML private TableColumn<HotelRoom, Double> priceColumn;
    @FXML private TableColumn<HotelRoom, Integer> maxGuestsColumn;
    @FXML private TableColumn<HotelRoom, Void> bookColumn;
    @FXML private TableView<BookingInfo> bookingsTable;
    @FXML private TableColumn<BookingInfo, String> hotelNameColumn;
    @FXML private TableColumn<BookingInfo, String> typeColumn;
    @FXML private TableColumn<BookingInfo, String> hotelLocationColumn;
    @FXML private TableColumn<BookingInfo, Integer> guestsColumn;
    @FXML private TableColumn<BookingInfo, Double> priceColumnBooking;
    @FXML private TableColumn<BookingInfo, String> checkInDateColumn;
    @FXML private TableColumn<BookingInfo, String> checkOutDateColumn;
    @FXML private TableColumn<BookingInfo, Void> cancelColumn;
    @FXML private Label welcomeLabel;
    @FXML private Button logOutButton;

    private Hotel selectedHotel;
    private Customer currentCustomer;

    @FXML
    public void initialize() {
        // Set up the table columns to map to the properties of the Hotel and HotelRoom objects
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        roomTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        // Customize the display of the price to include currency symbol and format
        priceColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("%.1f $ per night", price)); //setja $ merki fyrir aftan
                }
            }
        });
        maxGuestsColumn.setCellValueFactory(new PropertyValueFactory<>("maxGuests"));

        // Define the "Book Now" button and its behavior in the table
        bookColumn.setCellFactory(col -> new TableCell<>() {
            private final Button bookButton = new Button("Book Now");
            {
                bookButton.setOnAction(event -> {
                    try {
                        bookRoom(getTableView().getItems().get(getIndex()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : bookButton);
            }
        });

        // Define the "Cancel" button and its behavior in the table
        cancelColumn.setCellFactory(col -> new TableCell<>() {
            private final Button cancelButton = new Button("Cancel");
            {
                cancelButton.setOnAction(event -> {
                    cancelBooking(getTableView().getItems().get(getIndex()));
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : cancelButton);
            }
        });

        // Configure the booking list table columns to display booking details
        hotelNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHotelName()));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRoomType()));
        hotelLocationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation()));
        guestsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNumGuests()).asObject());
        priceColumnBooking.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalPrice()).asObject());

        // Set up date columns to display formatted dates
        checkInDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCheckInDate().toString()));
        checkOutDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCheckOutDate().toString()));

        // Initially hide the hotel details container until a hotel is selected
        hotelDetailsContainer.setVisible(false);
        hotelTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedHotel = newSelection;
                selectedHotelLabel.setText("Details for: " + newSelection.getName());
                hotelDetailsContainer.setVisible(true);
                loadRoomTypesForHotel(newSelection.getHotelId());
                onSearchRooms();
                loadBookingsForHotel(selectedHotel.getHotelId());
            } else {
                hotelDetailsContainer.setVisible(false);
            }
        });

        // Populate the room type combo box and set default values
        ObservableList<String> roomTypes = FXCollections.observableArrayList("Any", "Standard", "Deluxe", "Suite");
        roomTypeComboBox.setItems(roomTypes);
        roomTypeComboBox.setValue("Any");
        numberOfGuests.setText("");
        checkInDatePicker.setValue(LocalDate.now().plusDays(1));
        checkOutDatePicker.setValue(LocalDate.now().plusDays(2));

        // Load initial data from the database
        loadHotelsFromDatabase("");
    }

    private void loadBookingsForHotel(int hotelId) {
        List<BookingInfo> bookings = DatabaseManager.LoadBookings(hotelId, currentCustomer.getUserId());
        bookingsTable.setItems(FXCollections.observableArrayList(bookings));

    }

    @FXML
    public void onSearchRooms() {
        if (selectedHotel == null) {
            System.out.println("No hotel selected.");
            return;
        }
        String roomType = roomTypeComboBox.getValue();
        String guestsText = numberOfGuests.getText().trim();
        LocalDate checkInDate = checkInDatePicker.getValue();
        LocalDate checkOutDate = checkOutDatePicker.getValue();

        if (roomType == null || checkInDate == null || checkOutDate == null) {
            System.out.println("Invalid search inputs: " + roomType + ", " + guestsText + ", " + checkInDate + ", " + checkOutDate);
            return;
        }

        Integer guests = null;
        if (!guestsText.isEmpty()) {
            try {
                guests = Integer.parseInt(guestsText);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number of guests: " + guestsText);
                return;
            }
        }

        List<HotelRoom> availableRooms = searchAvailableRooms(selectedHotel.getHotelId(), roomType, guests, checkInDate, checkOutDate);
        ObservableList<HotelRoom> observableRooms = FXCollections.observableArrayList(availableRooms);
        availableRoomsTable.setItems(observableRooms);
        System.out.println("Displayed " + availableRooms.size() + " rooms for hotel " + selectedHotel.getName());
    }

    //hér förum við í greiðslu/bókunar viðmótið
    private void bookRoom(HotelRoom room) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assignment4_thb/booking-view.fxml"));
        Parent bookingView = loader.load();
        BookingController controller = loader.getController();
        controller.setSearchController(this);
        Booking booking = new Booking(currentCustomer, selectedHotel, room, checkInDatePicker.getValue(),checkOutDatePicker.getValue()); // Búm til nýja bókun, hér þarf að tengja það saman
        controller.setBooking(booking);
        controller.initialize();

        Scene scene = new Scene(bookingView);
        Stage stage = new Stage();
        stage.setTitle("Booking Details");
        stage.setScene(scene);
        stage.show();
    }

    public void updateBookings(){
        bookingsTable.setItems(currentCustomer.getBookings());
    }

    public void cancelBooking(BookingInfo booking) {
        try {
            // Remove booking from the database
            DatabaseManager.DeleteBooking(booking.getBookingId());

            // Update UI after successful deletion
            currentCustomer.getBookings().remove(booking);
            updateBookings();
            onSearchRooms();
            showAlert("Booking Cancellation", "Booking has been successfully cancelled.", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            showAlert("Booking Cancellation", "Failed to cancel booking: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Unexpected Error", "An error occurred: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    @FXML private void onSearch() {
        loadHotelsFromDatabase(searchField.getText().trim().toLowerCase());
    }

    @FXML private void onBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assignment4_thb/customer.fxml"));
            Parent profileView = loader.load();
            Scene scene = new Scene(profileView);
            Stage stage = (Stage) hotelTable.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println("Error loading profile view: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        Stage stage = (Stage) logOutButton.getScene().getWindow();
        stage.close();
    }

    private void loadHotelsFromDatabase(String searchText) {
        List<Hotel> hotels = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:hotel.db")) {
            String query = searchText.isEmpty() ?
                    "SELECT hotel_id, name, location, description  FROM hotels" :
                    "SELECT hotel_id, name, location, description FROM hotels WHERE LOWER(name) LIKE ? OR LOWER(location) LIKE ?";
            if (searchText.isEmpty()) {
                try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                    while (rs.next()) {
                        hotels.add(new Hotel(rs.getInt("hotel_id"), rs.getString("name"), rs.getString("location"), rs.getString("description"), new ArrayList<>()));
                    }
                }
            } else {
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    String searchPattern = "%" + searchText + "%";
                    pstmt.setString(1, searchPattern);
                    pstmt.setString(2, searchPattern);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            hotels.add(new Hotel(rs.getInt("hotel_id"), rs.getString("name"), rs.getString("location"), rs.getString("location"), new ArrayList<>()));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error loading hotels: " + e.getMessage());
        }
        hotelTable.setItems(FXCollections.observableArrayList(hotels));
        System.out.println("Loaded " + hotels.size() + " hotels from the database."); //see how many hotels are put in hotels.db
    }

    private void loadRoomTypesForHotel(int hotelId) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:hotel.db")) {
            String query = "SELECT DISTINCT type FROM hotel_rooms WHERE hotel_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, hotelId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    List<String> roomTypes = new ArrayList<>();
                    roomTypes.add("Any");
                    while (rs.next()) {
                        roomTypes.add(rs.getString("type"));
                    }
                    roomTypeComboBox.setItems(FXCollections.observableArrayList(roomTypes));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error loading room types: " + e.getMessage());
        }
    }

    private List<HotelRoom> searchAvailableRooms(int hotelId, String roomType, Integer guests, LocalDate checkInDate, LocalDate checkOutDate) {
        List<HotelRoom> availableRooms = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:hotel.db")) {
            StringBuilder query = new StringBuilder(
                    "SELECT room_id, type, price, number_of_guests " +
                    "  FROM hotel_rooms" +
                    " WHERE hotel_id = ? " +
                    "   AND not exists (select b.booking_id from bookings b " +
                    "                    where b.room_id = hotel_rooms.room_id " +
                    "                      and b.check_in_date < ? " +
                    "                      and b.check_out_date > ?)");
            if (!"Any".equals(roomType)) {
                query.append(" AND type = ?");
            }
            if (guests != null) {
                query.append(" AND number_of_guests = ?");
            }
           // System.out.println(query.toString());
            try (PreparedStatement pstmt = conn.prepareStatement(query.toString())) {
                pstmt.setInt(1, hotelId);
                pstmt.setDate(2, Date.valueOf(checkOutDate));
                pstmt.setDate(3, Date.valueOf(checkInDate));
                int paramIndex = 4;
                if (!"Any".equals(roomType)) {
                    pstmt.setString(paramIndex++, roomType);
                }
                if (guests != null) {
                    pstmt.setInt(paramIndex, guests);
                }

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        int roomId = rs.getInt("room_id");
                        String type = rs.getString("type");
                        double price = rs.getDouble("price");
                        int maxGuests = rs.getInt("number_of_guests");

                        HotelRoom room = new HotelRoom(
                                roomId,
                                type,
                                price,
                                maxGuests
                        );
                        availableRooms.add(room);
                    }
                    System.out.println("Found " + availableRooms.size() + " rooms for hotel " + hotelId + ", type " + roomType + ", guests " + (guests != null ? guests : "any"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error searching for available rooms: " + e.getMessage());
        }
        return availableRooms;
    }

    public void setCurrentCustomer(Customer customer) {
        this.currentCustomer = customer;
        welcomeLabel.setText("Welcome, " + customer.getName());
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}