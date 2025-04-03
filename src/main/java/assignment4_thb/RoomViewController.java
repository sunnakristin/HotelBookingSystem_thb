package assignment4_thb;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RoomViewController {
    @FXML
    private TableView<Hotel> hotelTable;

    @FXML
    private TableColumn<Hotel, String> nameColumn;

    @FXML
    private TableColumn<Hotel, String> locationColumn;

    @FXML
    private TextField searchField;

    @FXML
    private VBox hotelDetailsContainer;

    @FXML
    private Label selectedHotelLabel;

    @FXML
    private ComboBox<String> roomTypeComboBox;

    @FXML
    private TextField numberOfGuests;

    @FXML
    private DatePicker checkInDatePicker;

    @FXML
    private DatePicker checkOutDatePicker;

    @FXML
    private Button searchRoomsButton;

    @FXML
    private TableView<RoomData> availableRoomsTable;

    @FXML
    private TableColumn<RoomData, String> roomTypeColumn;

    @FXML
    private TableColumn<RoomData, Double> priceColumn;

    @FXML
    private TableColumn<RoomData, Integer> maxGuestsColumn;

    @FXML
    private TableColumn<RoomData, Void> bookColumn;

    @FXML
    private Label hotelNameLabel;

    // Current selected hotel
    private Hotel selectedHotel;

    // Inner class to represent room data
    public static class RoomData {
        private final int roomId;
        private final String type;
        private final double price;
        private final int maxGuests;

        public RoomData(int roomId, String type, double price, int maxGuests) {
            this.roomId = roomId;
            this.type = type;
            this.price = price;
            this.maxGuests = maxGuests;
        }

        public int getRoomId() {
            return roomId;
        }

        public String getType() {
            return type;
        }

        public double getPrice() {
            return price;
        }

        public int getMaxGuests() {
            return maxGuests;
        }
    }

    @FXML
    public void initialize() {
        // Set up the hotel table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        // Set up the available rooms table columns
        roomTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        maxGuestsColumn.setCellValueFactory(new PropertyValueFactory<>("maxGuests"));

        // Set up the book column with buttons
        bookColumn.setCellFactory(col -> new TableCell<RoomData, Void>() {
            private final Button bookButton = new Button("Book Now");
            {
                bookButton.setOnAction(event -> {
                    RoomData room = getTableView().getItems().get(getIndex());
                    bookRoom(room);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : bookButton);
            }
        });

        // Initially hide the hotel details container
        hotelDetailsContainer.setVisible(false);

        // Add selection listener to hotel table
        hotelTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedHotel = newSelection;
                selectedHotelLabel.setText("Details for: " + newSelection.getName());
                hotelDetailsContainer.setVisible(true);

                // Load room types for the selected hotel
                loadRoomTypesForHotel(newSelection.getHotelId());
            } else {
                hotelDetailsContainer.setVisible(false);
            }
        });

        // Populate the room type ComboBox with default values
        ObservableList<String> roomTypes = FXCollections.observableArrayList("Standard", "Deluxe", "Suite");
        roomTypeComboBox.setItems(roomTypes);

        // Load initial data
        loadHotelsFromDatabase("");
    }

    @FXML
    private void onSearchRooms() {
        if (selectedHotel != null) {
            String roomType = roomTypeComboBox.getValue();
            String guestsText = numberOfGuests.getText();
            LocalDate checkInDate = checkInDatePicker.getValue();
            LocalDate checkOutDate = checkOutDatePicker.getValue();

            // Validate inputs
            if (roomType == null || guestsText.isEmpty() || checkInDate == null || checkOutDate == null) {
                // Show error message
                return;
            }

            int guests = Integer.parseInt(guestsText);

            // Search for available rooms
            List<RoomData> availableRooms = searchAvailableRooms(
                    selectedHotel.getHotelId(),
                    roomType,
                    guests,
                    checkInDate,
                    checkOutDate);

            // Update the available rooms table
            ObservableList<RoomData> roomData = FXCollections.observableArrayList(availableRooms);
            availableRoomsTable.setItems(roomData);
        }
    }

    private void bookRoom(RoomData room) {
        // Implement booking logic here
        System.out.println("Booking room: " + room.getRoomId() + " for hotel: " + selectedHotel.getName());
    }

    public void setHotelName(String name) {
        hotelNameLabel.setText(name);
    }

    @FXML
    private void onSearch() {
        String searchText = searchField.getText().trim().toLowerCase();
        loadHotelsFromDatabase(searchText);
    }

    @FXML
    private void onBack() {
        try {
            // Load the profile and search view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assignment4_thb/profileAndSearch-view.fxml"));
            Parent profileView = loader.load();

            // Create and show the new scene
            Scene scene = new Scene(profileView);
            Stage stage = (Stage) hotelNameLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println("Error loading profile view: " + e.getMessage());
        }
    }

    private void loadHotelsFromDatabase(String searchText) {
        List<Hotel> hotels = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:hotel.db")) {
            String query;
            if (searchText.isEmpty()) {
                query = "SELECT hotel_id, name, location FROM hotels";
            } else {
                query = "SELECT hotel_id, name, location FROM hotels WHERE " +
                        "LOWER(name) LIKE ? OR LOWER(location) LIKE ?";
            }

            if (searchText.isEmpty()) {
                try (Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(query)) {
                    while (rs.next()) {
                        Hotel hotel = new Hotel(
                                rs.getInt("hotel_id"),
                                rs.getString("name"),
                                rs.getString("location"),
                                new ArrayList<>());
                        hotels.add(hotel);
                    }
                }
            } else {
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    String searchPattern = "%" + searchText + "%";
                    pstmt.setString(1, searchPattern);
                    pstmt.setString(2, searchPattern);

                    try (ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            Hotel hotel = new Hotel(
                                    rs.getInt("hotel_id"),
                                    rs.getString("name"),
                                    rs.getString("location"),
                                    new ArrayList<>());
                            hotels.add(hotel);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error loading hotels: " + e.getMessage());
        }

        ObservableList<Hotel> hotelData = FXCollections.observableArrayList(hotels);
        hotelTable.setItems(hotelData);
    }

    private void loadRoomTypesForHotel(int hotelId) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:hotel.db")) {
            String query = "SELECT DISTINCT type FROM hotel_rooms WHERE hotel_id = ? AND availability = 1";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, hotelId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    List<String> roomTypes = new ArrayList<>();
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

    private List<RoomData> searchAvailableRooms(int hotelId, String roomType, int guests,
            LocalDate checkInDate, LocalDate checkOutDate) {
        List<RoomData> availableRooms = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:hotel.db")) {
            // This is a simplified query - in a real application, you would need to check
            // for overlapping bookings
            String query = "SELECT room_id, type, price, number_of_guests FROM hotel_rooms " +
                    "WHERE hotel_id = ? AND type = ? AND number_of_guests >= ? AND availability = 1";

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, hotelId);
                pstmt.setString(2, roomType);
                pstmt.setInt(3, guests);

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        RoomData room = new RoomData(
                                rs.getInt("room_id"),
                                rs.getString("type"),
                                rs.getDouble("price"),
                                rs.getInt("number_of_guests"));
                        availableRooms.add(room);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error searching for available rooms: " + e.getMessage());
        }

        return availableRooms;
    }
}