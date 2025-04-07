package assignment4_thb;

public class Booking {
    private final HotelRoom room;
    private final Customer customer;
    private Payment payment;
    private Hotel hotel;


    public Booking(Customer customer, Hotel hotel, HotelRoom room){
        this.customer = customer;
        this.room = room;
        this.hotel = hotel;
        payment = new Payment(this);
    }

    public HotelRoom getRoom() {
        return room;
    }

    public String confirmBooking(){
        room.setAvailability(false);
        customer.addBooking(this);
        payment.process();
        return sendConfirmation();
    }
    public void cancel(){
        room.setAvailability(true);
        customer.removeBooking(this);
        payment.refund();
    }
    public String sendConfirmation(){
        return("An email confirmation has been sent to: " + customer.getEmail());
    }

    public Payment getPayment() {
        return payment;
    }

    public String getHotelName() {
        return hotel.getName();
    }
}
