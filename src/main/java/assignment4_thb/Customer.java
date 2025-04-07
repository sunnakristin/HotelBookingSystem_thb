package assignment4_thb;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Customer {
    private String name;
    private String email;
    private String password;
<<<<<<< HEAD
    private ArrayList<Booking> bookings;
=======
    private ObservableList<Booking> bookings;
    //private String preferredLanguage;
    //private String cardInfo;
    //private List<Review> reviews;
>>>>>>> 3b647d65790738cbda75afb8816627cb4ddf4c40

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
