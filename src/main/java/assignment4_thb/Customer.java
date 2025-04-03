package assignment4_thb;

import java.util.ArrayList;

public class Customer {
    private String name;
    private String email;
    private String password;
    private ArrayList<Booking> bookings;
    //private String preferredLanguage;
    //private String cardInfo;
    //private List<Review> reviews;

    public void addBooking(Booking booking){
        bookings.add(booking);
    }
    public void removeBooking(Booking booking){
        booking.cancel();
        bookings.remove(booking);
    }
    public Customer(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        bookings = new ArrayList<>();
        //this.reviews = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    /*
    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(String cardInfo) {
        this.cardInfo = cardInfo;
    }

     */

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }

    /*
    public void addReview(Review review) {
        reviews.add(review);
    }

    public List<Review> getReviews() {
        return reviews;
    }

     */
}
