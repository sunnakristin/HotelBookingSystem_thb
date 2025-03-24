package assignment4_thb;

public class Payment {
    private double amount;
    private boolean withCard;
    private String transactionId;
    private Booking booking;
    private boolean processed = false;

    public Payment(Booking booking){
        this.booking = booking;
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
