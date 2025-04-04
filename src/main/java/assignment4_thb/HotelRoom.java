package assignment4_thb;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class HotelRoom {

    private final int roomId;
    private final String type;
    private final double price;
    private final int maxGuests;
    private boolean availability;

    public HotelRoom(int roomId, String type, double price, int maxGuests) {
        this.roomId = roomId;
        this.type = type;
        this.price = price;
        this.maxGuests = maxGuests;
        this.availability = true;
    }

    public int getRoomId() { return roomId; }
    public String getType() { return type; }
    public Double getPrice() { return price; }
    public int getMaxGuests() { return maxGuests; }

    public boolean getAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

}
