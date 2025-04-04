package assignment4_thb;

import java.util.List;

public class Hotel {
    private final int hotelId;
    private final String name;
    private final String location;
    private List<HotelRoom> rooms;
    private final String description;

    public Hotel(int hotelId, String name, String location, String description, List<HotelRoom> rooms) {
        this.hotelId = hotelId;
        this.name = name;
        this.location = location;
        this.description = description;
        this.rooms = rooms;
    }

    public int getHotelId() {
        return hotelId;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public List<HotelRoom> getRooms() {
        return rooms;
    }

    public void setRooms(List<HotelRoom> rooms) {
        this.rooms = rooms;
    }
    public String getDescription() {
        return description;
    }
}
