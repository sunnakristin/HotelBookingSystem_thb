package assignment4_thb;

import javafx.scene.image.Image;

public class Hotel {
    private String name;
    private String location;
    private String[] amenities;
    private int roomAvalability;
    private Image[] images;
    private Room[] rooms;
    private Review[] reviews;

    /**
     * getterar og setterar
     * @return annað hvort breytunni (get) eða void (set)
     */

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation(){
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String[] getAmenities(){
        return amenities;
    }

    public void setAmenities(String[] amenities){
        this.amenities = amenities;
    }

    public int getRoomAvalability(){
        return roomAvalability;
    }

    public void setRoomAvalability(int roomAvalability) {
        this.roomAvalability = roomAvalability;
    }

    public Image[] getImages(){
        return images;
    }

    public void setImages(Image[] images) {
        this.images = images;
    }

    public Room[] getRooms() {
        return rooms;
    }

    public void setRooms(Room[] rooms){
        this.rooms = rooms;
    }

    public void updateAvalability(){
        int availableRooms = 0;
        for(Room room: rooms){
            if(room.isAvailable()){ // isAvalabel er aðferð í Room klasanum
                availableRooms ++;
            }
        }
        this.roomAvalability = roomAvalability;
    }
}

