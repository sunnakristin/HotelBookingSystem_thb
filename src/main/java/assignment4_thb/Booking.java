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
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;
    private double totalPrice;


    public Booking(Customer customer, Hotel hotel, HotelRoom room, LocalDate checkInDate, LocalDate checkOutDate) {
        this.customer = customer;
        this.room = room;
        this.hotel = hotel;
        payment = new Payment(this);
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        calculateTotalPrice();//chat
    }

    private void calculateTotalPrice() {
        long nights = java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        this.totalPrice = nights * room.getPrice();
    }

    public HotelRoom getRoom() {
        return room;
    }

    public String confirmBooking() throws SQLException {
//        try {
//            DatabaseManager.confirmBooking(room.getRoomId());
//        } catch (SQLException e) {
//            System.out.println("Error booking room: " + e.getMessage());
//            return "Failed to confirm booking due to an error.";
//        }
//
        customer.addBooking(this);
        payment.process();
        //int bookingId = DatabaseManager.saveBooking(this.customer.getUserId(), room.getRoomId(), checkInDate, checkOutDate, room.getMaxGuests(), room.getPrice());
        //DatabaseManager.saveBooking(this.customer.getUserId(), room.getRoomId(), checkInDate, checkOutDate, room.getMaxGuests(), room.getPrice());
        return sendConfirmation();
    }

    public double getTotalPrice() {
        return totalPrice;
    }


//    public void cancel() {
////        try {
////            DatabaseManager.cancelBooking(room.getRoomId());
////        } catch (SQLException e) {
////            System.out.println("Error canceling booking: " + e.getMessage());
////        }
////        //room.setAvailability(true);
//
//
//
//
//        customer.removeBooking(this);
//        payment.refund();
//    }

    public String sendConfirmation(){
        return("An email confirmation has been sent to: " + customer.getEmail());
    }

    public Payment getPayment() {
        return payment;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }
}
