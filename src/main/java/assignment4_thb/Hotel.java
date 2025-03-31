package assignment4_thb;
import java.util.List;

public class Hotel {
    private String name;
    private List<HotelRoom> rooms;
    private String location;



    public Hotel(String name, String location, List<HotelRoom> rooms) {
       this.name = name;
       this.location = location;
       this.rooms = rooms;
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
}
