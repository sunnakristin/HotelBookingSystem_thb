package assignment4_thb;

public class Booking {
    private final int bookingId;
    private static int totalBookings = 0;
    private final HotelRoom room;
    private final Customer customer;
    private Payment payment;

    public Booking(Customer customer, HotelRoom room){
        this.customer = customer;
        this.room = room;
        bookingId = 100000000 + totalBookings++;
        payment = new Payment(this);
    }

    public HotelRoom getRoom() {
        return room;
    }

    public void confirm(){
        if(room.getAvailability()){
            sendConfirmation();
            customer.addBooking(this);
            payment.process();
        } else {
            System.out.println("Unavailable");
        }
    }
    public void cancel(){
        room.setAvailability(true);
        customer.removeBooking(this);
        payment.refund();
    }
    public void sendConfirmation(){
        System.out.println("bookingId: " + this.bookingId +"\nRoom: "+ this.room +"\nCustomer: "+ this.customer);
    }

    public Payment getPayment() {
        return payment;
    }

    public int getBookingId() {
        return bookingId;
    }
}
