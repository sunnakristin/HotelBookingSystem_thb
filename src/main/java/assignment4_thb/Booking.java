package assignment4_thb;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Booking {
    private int bookingId;
    private static int totalBookings = 0;
    private Date checkIn;
    private Date checkOut;
    private Room room;
    private Customer customer;
    private Payment payment;

    public Booking(Customer customer, Room room){
        this.customer = customer;
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    public void confirm(LocalDate checkIn, LocalDate checkOut){
        Date checkInD = Date.from(checkIn.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date checkOutD = Date.from(checkOut.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        if(room.checkAvailability(checkIn,checkOut)){
            bookingId = 100000000 + totalBookings++;
            this.checkIn = checkInD;
            this.checkOut = checkOutD;
            customer.bookings.add(this);
            sendConfirmation();
            payment = new Payment(this);
        } else {
            System.out.println("Unavailable");
        }
    }
    public void cancel(){
        room.setAvailability(checkIn, checkOut);
        customer.bookings.remove(this);
        if (payment.isProcessed()) {
            payment.refund();
        }
    }
    public void sendConfirmation(){
        System.out.println(this.bookingId +"\n"+ this.room +"\n"+ this.checkIn +"\n"+ this.checkOut +"\n"+ this.customer);
    }

    public Payment getPayment() {
        return payment;
    }

    public int getBookingId() {
        return bookingId;
    }
}
