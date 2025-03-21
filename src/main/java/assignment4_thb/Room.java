package assignment4_thb;

import javafx.scene.image.Image;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Room {
    private String type;
    private double price;
    private boolean availability;
    private List<Image> roomImages; // Using a list to handle multiple images

    // Constructor initializing all fields
    public Room(String type, double price, boolean availability) {
        this.type = type;
        this.price = price;
        this.availability = availability;
        this.roomImages = new ArrayList<>(); // Initialize with an empty list
    }

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

    public boolean isAvailable() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
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

    // Method to check availability
    public boolean checkAvailability(LocalDate checkIn, LocalDate checkOut) {
        // Add logic to determine if the room is available between checkIn and checkOut
        return availability;
    }

    // Method to update price (could be more complex based on business rules)
    public void updatePrice(double newPrice) {
        this.price = newPrice;
    }
}
