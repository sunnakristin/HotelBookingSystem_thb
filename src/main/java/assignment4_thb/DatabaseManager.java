package assignment4_thb;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:hotel.db";

    public static void initializeDatabase() {
        // Set locale to British English for DD/MM/YYYY date format
        Locale.setDefault(Locale.forLanguageTag("en-GB"));

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                createTables(conn);
                // Only populate with sample data if tables are empty
                if (isTableEmpty(conn, "hotels")) {
                    populateSampleData(conn);
                }
            }
        } catch (SQLException e) {
            System.out.println("Database initialization error: " + e.getMessage());
        }
    }

    private static void createTables(Connection conn) throws SQLException {
        // Users table
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL," +
                "email TEXT NOT NULL," +
                "user_type TEXT NOT NULL," + // 'customer' or 'admin'
                "name TEXT NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        // Hotels table
        String createHotelsTable = "CREATE TABLE IF NOT EXISTS hotels (" +
                "hotel_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "location TEXT NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        // Hotel rooms table
        String createHotelRoomsTable = "CREATE TABLE IF NOT EXISTS hotel_rooms (" +
                "room_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "hotel_id INTEGER NOT NULL," +
                "type TEXT NOT NULL," +
                "price REAL NOT NULL," +
                "number_of_guests INTEGER NOT NULL," +
                "availability INTEGER NOT NULL DEFAULT 1," + // SQLite doesn't have BOOLEAN
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (hotel_id) REFERENCES hotels(hotel_id)" +
                ");";

        // Room images table
        String createRoomImagesTable = "CREATE TABLE IF NOT EXISTS room_images (" +
                "image_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "room_id INTEGER NOT NULL," +
                "image_path TEXT NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (room_id) REFERENCES hotel_rooms(room_id)" +
                ");";

        // Bookings table
        String createBookingsTable = "CREATE TABLE IF NOT EXISTS bookings (" +
                "booking_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "room_id INTEGER NOT NULL," +
                "user_id INTEGER NOT NULL," +
                "check_in_date DATE NOT NULL," +
                "check_out_date DATE NOT NULL," +
                "num_guests INTEGER NOT NULL," +
                "total_price REAL NOT NULL," +
                "booking_status INTEGER NOT NULL DEFAULT 1," + // 1 = confirmed, 0 = cancelled
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (room_id) REFERENCES hotel_rooms(room_id)," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id)" +
                ");";

        // Reviews table
        String createReviewsTable = "CREATE TABLE IF NOT EXISTS reviews (" +
                "review_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "hotel_id INTEGER NOT NULL," +
                "user_id INTEGER NOT NULL," +
                "rating INTEGER NOT NULL," + // 1-5
                "comment TEXT," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (hotel_id) REFERENCES hotels(hotel_id)," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id)" +
                ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createHotelsTable);
            stmt.execute(createHotelRoomsTable);
            stmt.execute(createRoomImagesTable);
            stmt.execute(createBookingsTable);
            stmt.execute(createReviewsTable);
        }
    }

    private static boolean isTableEmpty(Connection conn, String tableName) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + tableName;
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
            return true;
        }
    }

    private static void populateSampleData(Connection conn) throws SQLException {
        // Sample users
        String insertUsers = "INSERT INTO users (username, password, email, user_type, name) VALUES " +
                "('customer1', 'password123', 'customer1@example.com', 'customer', 'John Doe')," +
                "('customer2', 'password123', 'customer2@example.com', 'customer', 'Jane Smith')," +
                "('admin1', 'password123', 'admin1@example.com', 'admin', 'Hotel Manager')," +
                "('admin2', 'password123', 'admin2@example.com', 'admin', 'Front Desk');";

        // Sample hotels
        String insertHotels = "INSERT INTO hotels (name, location) VALUES " +
                "('Grand Hotel', 'Reykjavik, Iceland')," +
                "('Mountain View Hotel', 'Akureyri, Iceland')," +
                "('Ocean Breeze Resort', 'Vik, Iceland');";

        // Sample rooms
        String insertRooms = "INSERT INTO hotel_rooms (hotel_id, type, price, number_of_guests, availability) VALUES " +
                "(1, 'Standard', 100.00, 2, 1)," +
                "(1, 'Deluxe', 200.00, 4, 1)," +
                "(1, 'Suite', 300.00, 6, 1)," +
                "(2, 'Standard', 90.00, 2, 1)," +
                "(2, 'Deluxe', 180.00, 4, 1)," +
                "(2, 'Suite', 270.00, 6, 1)," +
                "(3, 'Standard', 120.00, 2, 1)," +
                "(3, 'Deluxe', 240.00, 4, 1)," +
                "(3, 'Suite', 360.00, 6, 1);";

        // Sample bookings
        String insertBookings = "INSERT INTO bookings (room_id, user_id, check_in_date, check_out_date, num_guests, total_price, booking_status) VALUES "
                +
                "(1, 1, '2024-06-01', '2024-06-05', 2, 400.00, 1)," +
                "(2, 2, '2024-07-15', '2024-07-20', 4, 1000.00, 0)," +
                "(3, 1, '2024-08-01', '2024-08-03', 6, 900.00, 1);";

        // Sample reviews
        String insertReviews = "INSERT INTO reviews (hotel_id, user_id, rating, comment) VALUES " +
                "(1, 1, 5, 'Excellent stay at Grand Hotel! The staff was very friendly.')," +
                "(1, 2, 4, 'Great location and comfortable rooms. Would recommend.')," +
                "(2, 1, 5, 'Mountain View Hotel offers spectacular views and great service.');";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(insertUsers);
            stmt.execute(insertHotels);
            stmt.execute(insertRooms);
            stmt.execute(insertBookings);
            stmt.execute(insertReviews);
        }
    }
}
