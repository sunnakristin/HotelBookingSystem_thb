package assignment4_thb;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Customer {
    private String name;
    private String email;
    private String password;
    private ObservableList<Booking> bookings;

    public void addBooking(Booking booking){
        bookings.add(booking);
    }

    public ObservableList<Booking> getBookings() {
        return bookings;
    }

    public void removeBooking(Booking booking){
        bookings.remove(booking);
    }
    public Customer(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        bookings = FXCollections.observableArrayList();
        //this.reviews = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }

}
