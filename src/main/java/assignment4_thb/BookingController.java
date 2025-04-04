package assignment4_thb;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.text.MessageFormat;

public class BookingController {
    @FXML private Label hotelLabel;
    @FXML private Label typeLabel;
    @FXML private Label priceLabel;
    @FXML private Button confirmButton;
    @FXML private TextField cardNr;
    @FXML private TextField cvc;
    @FXML private TextField expiryMonth;
    @FXML private TextField expiryYear;

    private Booking booking;

    public BookingController() {
    }

    @FXML
    public void initialize() {
        if (booking != null) {
            confirmButton.setOnAction(e -> confirm());
            hotelLabel.setText(booking.getHotelName());
            typeLabel.setText(booking.getRoom().getType());
            priceLabel.setText(String.format("%.2f $", booking.getRoom().getPrice()));
        }
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    private boolean validCardNr() {
        return cardNr.getText().matches("\\d{4} \\d{4} \\d{4} \\d{4}");
    }

    private boolean validCVC() {
        return cvc.getText().matches("\\d{3}");
    }

    private boolean validExpiryDate() {
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

        if (yearNum < currentYear || (yearNum == currentYear && monthNum < currentMonth)) {
            return false;
        }
        return true;
    }

    @FXML
    public void confirm() {
        if (validCardNr() && validCVC() && validExpiryDate()) {
            String expiryDate = expiryMonth.getText() + "/" + expiryYear.getText();
            booking.getPayment().setCardInfo(cardNr.getText(), cvc.getText(), expiryDate);
            booking.confirmBooking();
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
        } else {
            System.out.println("Please fill out valid card info");
        }
    }
}