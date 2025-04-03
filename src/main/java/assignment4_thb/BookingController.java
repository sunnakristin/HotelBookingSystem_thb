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
    private ChoiceBox<String> cashOrCard;
    @FXML
    private Button confirmButton;

    public BookingController(Booking booking){
        this.booking = booking;
    }
    public void initialize(){
        if(booking != null){
            confirmButton.setOnAction(e -> confirm());
            this.typeLabel.setText("Type: " + booking.getRoom().getType());
            this.priceLabel.setText("Price: " + booking.getRoom().getPrice());
            cashOrCard.setItems(FXCollections.observableArrayList("Cash", "Card"));
        }
    }
    public void confirm(){
        if(cashOrCard.getValue() != null){
            booking.getPayment().setMethod(cashOrCard.getValue());
            booking.confirm();
            System.out.println("Please select both check-in and check-out dates");
        }else{
            System.out.println("Please select a payment method");
        }
    }
}
