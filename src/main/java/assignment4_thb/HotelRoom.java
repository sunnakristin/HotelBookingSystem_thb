package assignment4_thb;

public class HotelRoom {

    private final int roomId;
    private final String type;
    private final double price;
    private final int maxGuests;

    public HotelRoom(int roomId, String type, double price, int maxGuests //, LocalDate checkInDate, LocalDate checkOutDate
                      ) {
        this.roomId = roomId;
        this.type = type;
        this.price = price;
        this.maxGuests = maxGuests;
    }

    public int getRoomId() { return roomId; }
    public String getType() { return type; }
    public Double getPrice() { return price; }
    public int getMaxGuests() { return maxGuests; }
}
