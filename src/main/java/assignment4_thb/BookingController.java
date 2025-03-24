package assignment4_thb;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

public class BookingController {
    Booking booking;
    @FXML
    private Label typeLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private DatePicker checkOut;
    @FXML
    private DatePicker checkIn;
    @FXML
    private Button payButton;

    public BookingController(Booking booking){
        this.booking = booking;
    }
    public void initialize(){
        if(booking != null){
            this.typeLabel.setText(booking.getRoom().getPrice());
            this.priceLabel.setText(booking.getRoom().getType());
        }
    }
    public void confirm(){
        if (checkIn.getValue() != null && checkOut.getValue() != null) {
            booking.confirm(checkIn.getValue(), checkOut.getValue());
            payButton.setDisable(false);
        } else {
            System.out.println("Please select both check-in and check-out dates");
        }
    }
    public void pay(){
        booking.getPayment().process();
    }
}
