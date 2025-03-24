package assignment4_thb;

public class Payment {
    private double amount;
    private String method;
    private int transactionId;

    public Payment(Booking booking){
        transactionId = booking.getBookingId()+100000000;
        amount = booking.getRoom().getPrice();
    }
    public void process(){
        System.out.println(transactionId+"\n"+amount+"\nBy: "+method);
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void refund(){
        System.out.println(transactionId +"\n"+amount+" refunded");
    }
}
