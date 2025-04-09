package assignment4_thb;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Customer {
    private int userId;
    private String name;
    private String email;
    private String password;
    private ObservableList<BookingInfo> bookings;

    public void addBooking(Booking booking){
        int bookingId = 9; // todo: this should be loaded from the DB after saving
        bookings.add(
            new BookingInfo(bookingId, booking.getHotel().getName(), booking.getRoom().getType(), booking.getHotel().getLocation(),
                 booking.getRoom().getMaxGuests(), booking.getRoom().getPrice(), booking.getCheckInDate(), booking.getCheckOutDate()));
    }

    public ObservableList<BookingInfo> getBookings() {
        return bookings;
    }

    public void removeBooking(BookingInfo booking){
        bookings.remove(booking);
    }
    public Customer(int userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        bookings = FXCollections.observableArrayList();
    }

    public int getUserId() {
        return userId;
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
