package assignment4_thb;

import java.util.ArrayList;

public class Customer {
    private String name;
    private String email;
    private String password;
    private ArrayList<Booking> bookings;

    public void addBooking(Booking booking){
        bookings.add(booking);
    }
    public void removeBooking(Booking booking){
        booking.cancel();
        bookings.remove(booking);
    }
    public Customer(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        bookings = new ArrayList<>();
        //this.reviews = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }
}
