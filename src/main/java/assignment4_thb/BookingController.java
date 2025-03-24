package assignment4_thb;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
    private Button confirmButton;
    @FXML
    private ChoiceBox<String> cashOrCard;

    public BookingController(Booking booking){
        this.booking = booking;
    }
    public void initialize(){
        if(booking != null){
            this.typeLabel.setText(String.valueOf(booking.getRoom().getPrice()));
            this.priceLabel.setText(booking.getRoom().getType());
            cashOrCard.setItems(FXCollections.observableArrayList("Cash", "Card"));
        }
    }
    public void confirm(){
        if(!cashOrCard.getValue().isEmpty()){
            booking.getPayment().setMethod(cashOrCard.getValue());
            booking.getPayment().process();
            if (checkIn.getValue() != null && checkOut.getValue() != null) {
                booking.confirm(checkIn.getValue(), checkOut.getValue());
                confirmButton.setDisable(true);
            } else {
                System.out.println("Please select both check-in and check-out dates");
            }
        }else{
            System.out.println("Please select a payment method");
        }
    }
}
