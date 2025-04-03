package assignment4_thb;


public class Payment {
    private final double amount;
    private final int transactionId;
    private String cvc;
    private String cardNr;
    private String expiryMonth;

    public Payment(Booking booking){
        transactionId = booking.getBookingId()+100000000;
        amount = booking.getRoom().getPrice();
    }
    public void process(){
        System.out.println("transactionId: " + transactionId + "\nAmount:" + amount
                + "\nCardNr: "+cardNr+ "\nCVC: "+ cvc + "\nExpiryMonth: "+expiryMonth);
    }

    public void setCardInfo(String cardNr, String cvc, String expiryMonth) {
        this.cardNr = cardNr;
        this.cvc = cvc;
        this.expiryMonth = expiryMonth;
    }

    public void refund(){
        System.out.println("transactionId: " + transactionId +"\nAmount: " + amount + " refunded" + "\nCardNr: "+cardNr+ "\nCVC: "+ cvc + "\nExpiryMonth: "+expiryMonth);
    }
}
