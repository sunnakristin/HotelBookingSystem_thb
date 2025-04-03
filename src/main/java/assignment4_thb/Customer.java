package assignment4_thb;

import java.util.ArrayList;

public class Customer {
    private ArrayList<Booking> bookings;

    public Customer(){
        bookings = new ArrayList<>();
    }

    public void addBooking(Booking booking){
        bookings.add(booking);
    }
    public void removeBooking(Booking booking){
        booking.cancel();
        bookings.remove(booking);
    }
}
