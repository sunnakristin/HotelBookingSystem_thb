package assignment4_thb;


public class Payment {
    private final double amount;
    private String cvc;
    private String cardNr;
    private String expiryMonth;

    public Payment(Booking booking){
        amount = booking.getRoom().getPrice();
    }
    public void process(){
        System.out.println("\nAmount:\t\t\t" + amount
                + "\nCardNr:\t\t\t"+ cardNr + "\nCVC:\t\t\t"+ cvc + "\nExpiryMonth:\t"+expiryMonth);
    }

    public void setCardInfo(String cardNr, String cvc, String expiryMonth) {
        this.cardNr = cardNr;
        this.cvc = cvc;
        this.expiryMonth = expiryMonth;
    }

    public void refund(){
        System.out.println("\nRefunded:\t\t\t" + amount
                + "\nCardNr:\t\t\t"+ cardNr + "\nCVC:\t\t\t"+ cvc + "\nExpiryMonth:\t"+expiryMonth);
    }
}
