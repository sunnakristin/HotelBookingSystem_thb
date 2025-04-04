package assignment4_thb;

public class Booking {
    private final int bookingId;
    private static int totalBookings = 0;
    private final HotelRoom room;
    private final Customer customer;
    private Payment payment;
    private Hotel hotel;


    public Booking(Customer customer, Hotel hotel, HotelRoom room){
        this.customer = customer;
        this.room = room;
        this.hotel = hotel;
        bookingId = 100000000 + totalBookings++;
        payment = new Payment(this);
    }

    public HotelRoom getRoom() {
        return room;
    }

    public void confirmBooking(){
        if(room.getAvailability()){
            room.setAvailability(false);
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

    public String getHotelName() {
        return hotel.getName();
    }
}
