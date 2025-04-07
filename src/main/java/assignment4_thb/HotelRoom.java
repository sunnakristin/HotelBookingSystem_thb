package assignment4_thb;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class HotelRoom {

    private final int roomId;
    private final String type;
    private final double price;
    private final int maxGuests;
    private BooleanProperty availability = new SimpleBooleanProperty();
    public HotelRoom(int roomId, String type, double price, int maxGuests) {
        this.roomId = roomId;
        this.type = type;
        this.price = price;
        this.maxGuests = maxGuests;
        setAvailability(true);
    }

    public int getRoomId() { return roomId; }
    public String getType() { return type; }
    public Double getPrice() { return price; }
    public int getMaxGuests() { return maxGuests; }

    public boolean getAvailability() {
        return availability.get();
    }

    public void setAvailability(boolean availability) {
        this.availability.set(availability);
    }

}
