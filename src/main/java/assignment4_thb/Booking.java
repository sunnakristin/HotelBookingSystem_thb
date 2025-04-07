package assignment4_thb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    public String confirmBooking(){
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:hotel.db")) {
            String updateQuery = "UPDATE hotel_rooms SET availability = 0 WHERE room_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                pstmt.setInt(1, room.getRoomId());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error updating room availability: " + e.getMessage());
        }
        room.setAvailability(false);
        customer.addBooking(this);
        payment.process();
        return sendConfirmation();
    }
    public void cancel(){
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:hotel.db")) {
            String updateQuery = "UPDATE hotel_rooms SET availability = 1 WHERE room_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                pstmt.setInt(1, room.getRoomId());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error updating room availability: " + e.getMessage());
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

    public String getHotelName() {
        return hotel.getName();
    }
}
