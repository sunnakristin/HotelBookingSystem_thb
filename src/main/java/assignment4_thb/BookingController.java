package assignment4_thb;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.ZoneId;
import java.util.Date;

public class BookingController {
    Booking booking;
    @FXML
    private Label typeLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Button confirmButton;
    @FXML
    private TextField cardNr;
    @FXML
    private TextField cvc;
    @FXML
    private TextField expiryMonth;

    public BookingController(Booking booking){
        this.booking = booking;
    }
    public void initialize(){
        if(booking != null){
            confirmButton.setOnAction(e -> confirm());
            this.typeLabel.setText("Type: " + booking.getRoom().getType());
            this.priceLabel.setText("Price: " + booking.getRoom().getPrice());
        }
    }
    private boolean validCardNr(){
        return cardNr.getText().matches("\\d{4} \\d{4} \\d{4}");
    }
    private boolean validCVC(){
        return cvc.getText().matches("\\d{3}");
    }
    private boolean validExpiryMonth(){
        return expiryMonth.getText().matches("^(0[1-9]|1[0-2])/(2[5-9]|[3-9][0-9])$");
    }
    public void confirm(){
        if(validCardNr() && validCVC() && validExpiryMonth()){
            booking.getPayment().setCardInfo(cardNr.getText(), cvc.getText(), expiryMonth.getText());
            booking.confirm();
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
        }else{
            System.out.println("Please fill out valid card info");
        }
    }
}
