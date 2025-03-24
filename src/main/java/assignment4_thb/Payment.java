package assignment4_thb;

public class Payment {
    private double amount;
    private boolean withCard;
    private int transactionId;
    private boolean processed = false;

    public Payment(Booking booking){
        transactionId = booking.getBookingId()+100000000;
        amount = booking.getRoom().getPrice();
    }
    public void process(){
        System.out.println(transactionId +"\n"+amount);
        processed = true;
    }
    public void refund(){
        System.out.println(transactionId +"\n"+amount+" refunded");
        processed = false;
    }

    public boolean isProcessed() {
        return processed;
    }
}
