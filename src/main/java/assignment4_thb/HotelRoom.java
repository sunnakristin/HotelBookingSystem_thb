package assignment4_thb;

import javafx.scene.image.Image;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HotelRoom {
    private String type;
    private double price;
    private int numberOfguests;
    private Hotel hotel;
    private List<Image> roomImages; // Using a list to handle multiple images

    public HotelRoom(String type, double price, int numberOfguests, Hotel hotel) {
        this.hotel = hotel;
        this.type = type;
        this.price = price;
        this.numberOfguests = numberOfguests;
        this.roomImages = new ArrayList<>(); // Initialize with an empty list
    }
    // Þurfum að bæta við hversu margir í hverju herbergi í gagnagrunninn
    // setjum frekar random tölu á það sem er available
    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public List<Image> getRoomImages() {
        return roomImages;
    }

    public void setRoomImages(List<Image> roomImages) {
        this.roomImages = roomImages;
    }

    public void addRoomImage(Image image) {
        this.roomImages.add(image);
    }

    public void setNumberOfguests() {
        this.numberOfguests = numberOfguests;
    }

    public int getNumberOfguests() {
        return numberOfguests;
    }


}
