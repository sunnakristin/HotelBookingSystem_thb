package assignment4_thb;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;


public class BookingController {
    Booking booking;
    @FXML
    private Label typeLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Button confirmButton;
    @FXML
    private Button backButton;
    @FXML
    private TextField cardNr;
    @FXML
    private TextField cvc;
    @FXML
    private TextField expiryMonth;
    @FXML
    private Label info;

    public BookingController(Booking booking){
        this.booking = booking;
    }
    public void initialize(){
        if(booking != null){
            confirmButton.setOnAction(e -> confirm());
            backButton.setOnAction(e -> back());
            this.typeLabel.setText("Type: " + booking.getRoom().getType());
            this.priceLabel.setText("Price: " + booking.getRoom().getPrice());
        }
    }
    private boolean validCardNr(){
        return cardNr.getText().matches("\\d{4} \\d{4} \\d{4} \\d{4}");
    }
    private boolean validCVC(){
        return cvc.getText().matches("\\d{3}");
    }
    private boolean validExpiryMonth(){
        return expiryMonth.getText().matches("^(0[1-9]|1[0-2])/(2[5-9]|[3-9][0-9])$");
    }
    public void confirm(){
        if(validCardNr() && validCVC() && validExpiryMonth()){
            if(booking.getRoom().getAvailability()) {
                booking.getPayment().setCardInfo(cardNr.getText(), cvc.getText(), expiryMonth.getText());
                info.setText(booking.confirm());
                confirmButton.setDisable(true);
                cardNr.setDisable(true);
                cvc.setDisable(true);
                expiryMonth.setDisable(true);
            } else {
                info.setText("Unavailable");
            }
        }else{
            info.setText("Please fill out valid card info");
        }
    }
    public void back(){
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }
}
