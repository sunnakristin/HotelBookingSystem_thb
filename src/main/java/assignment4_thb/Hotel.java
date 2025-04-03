package assignment4_thb;

import java.util.List;

public class Hotel {
    private int hotelId;
    private String name;
    private String location;
    private List<HotelRoom> rooms;

    public Hotel(int hotelId, String name, String location, List<HotelRoom> rooms) {
        this.hotelId = hotelId;
        this.name = name;
        this.location = location;
        this.rooms = rooms;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<HotelRoom> getRooms() {
        return rooms;
    }

    public void setRooms(List<HotelRoom> rooms) {
        this.rooms = rooms;
    }
}
