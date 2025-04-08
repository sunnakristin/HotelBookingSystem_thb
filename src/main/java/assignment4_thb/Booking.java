package assignment4_thb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class Booking {
    private final HotelRoom room;
    private final Customer customer;
    private final Payment payment;
    private final Hotel hotel;


    public Booking(Customer customer, Hotel hotel, HotelRoom room){
        this.customer = customer;
        this.room = room;
        this.hotel = hotel;
        payment = new Payment(this);
    }

    public HotelRoom getRoom() {
        return room;
    }

    public String confirmBooking() {
        try {
            DatabaseManager.confirmBooking(room.getRoomId());
        } catch (SQLException e) {
            System.out.println("Error booking room: " + e.getMessage());
            return "Failed to confirm booking due to an error.";
        }
        room.setAvailability(false);
        customer.addBooking(this);
        payment.process();
        return sendConfirmation();
    }


    public void cancel() {
        try {
            DatabaseManager.cancelBooking(room.getRoomId());
        } catch (SQLException e) {
            System.out.println("Error canceling booking: " + e.getMessage());
        }
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

    public Hotel getHotel() {
        return hotel;
    }
}
