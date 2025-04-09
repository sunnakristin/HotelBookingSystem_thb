package assignment4_thb;

import java.time.LocalDate;

public class BookingInfo {
    private final int bookingId;
    private final String hotelName;
    private final String roomType;
    private final String location;
    private final int numGuests;
    private double totalPrice;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;

    public BookingInfo(int bookingId, String hotelName, String roomType, String location, int numGuests,
                       double totalPrice, LocalDate checkInDate, LocalDate checkOutDate) {
        this.bookingId = bookingId;
        this.hotelName = hotelName;
        this.roomType = roomType;
        this.location = location;
        this.numGuests = numGuests;
        this.totalPrice = totalPrice;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getLocation() {
        return location;
    }

    public int getNumGuests() {
        return numGuests;
    }

    public double getTotalPrice() {
        totalPrice = (checkOutDate.getDayOfYear()-checkInDate.getDayOfYear())*totalPrice;
        return totalPrice;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }
}
