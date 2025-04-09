package assignment4_thb;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;

public class BookingController {
    @FXML private Label hotelLabel;
    @FXML private Label typeLabel;
    @FXML private Label priceLabel;
    @FXML private Label checkInLabel;
    @FXML private Label checkOutLabel;
    @FXML private Button confirmButton;
    @FXML private TextField cardNr;
    @FXML private TextField cvc;
    @FXML private TextField expiryMonth;
    @FXML private TextField expiryYear;
    @FXML private Button backButton;
    @FXML private Label paymentInfo;
    @FXML private SearchController searchController;

    private Booking booking;

    public BookingController() {
    }

    @FXML
    public void initialize() {
        if (booking != null) {
            confirmButton.setOnAction(e -> confirm());
            backButton.setOnAction(e -> back());
            hotelLabel.setText(booking.getHotel().getName());
            typeLabel.setText(booking.getRoom().getType());
            priceLabel.setText(String.format("%.1f $", booking.getTotalPrice()));
            checkInLabel.setText(booking.getCheckInDate().toString());
            checkOutLabel.setText(booking.getCheckOutDate().toString());
        }
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public void setSearchController(SearchController searchController) {
        this.searchController = searchController;
    }

    private boolean validCardNr() {
        return cardNr.getText().matches("\\d{4} \\d{4} \\d{4} \\d{4}");
    }

    private boolean validCVC() {
        return cvc.getText().matches("\\d{3}");
    }

    private boolean validExpiryMonth() {
        String month = expiryMonth.getText();
        String year = expiryYear.getText();

        // Validate month (01-12)
        if (!month.matches("^(0[1-9]|1[0-2])$")) {
            return false;
        }

        // Validate year (25-99 for 2025-2099)
        if (!year.matches("^(2[5-9]|[3-9][0-9])$")) {
            return false;
        }

        // Ensure the date is not in the past
        int monthNum = Integer.parseInt(month);
        int yearNum = Integer.parseInt(year) + 2000; // Convert "25" to 2025
        int currentYear = java.time.LocalDate.now().getYear();
        int currentMonth = java.time.LocalDate.now().getMonthValue();

        return !(yearNum < currentYear || (yearNum == currentYear && monthNum < currentMonth));
    }

    @FXML
    public void confirm()  {
        if (validCardNr() && validCVC() && validExpiryMonth()) {
            try {
                if (DatabaseManager.checkRoomAvailability(booking.getRoom().getRoomId(), booking.getCheckInDate())) {
                    String expiryDate = expiryMonth.getText() + "/" + expiryYear.getText();
                    booking.getPayment().setCardInfo(cardNr.getText(), cvc.getText(), expiryDate);
                    paymentInfo.setText(booking.confirmBooking());
                    confirmButton.setDisable(true);
                    cardNr.setDisable(true);
                    cvc.setDisable(true);
                    expiryMonth.setDisable(true);
                    expiryYear.setDisable(true);

                    // Update search
                    searchController.onSearchRooms();
                    searchController.updateBookings();
                } else {
                    paymentInfo.setText("room unavailable");
                }
            }
            catch (SQLException e) {
                System.out.println(e);
            }
        } else {
            paymentInfo.setText("Please fill out valid card info");
        }
        Stage stage = (Stage) paymentInfo.getScene().getWindow();
        stage.sizeToScene();
    }
    @FXML
    public void back(){
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }
}