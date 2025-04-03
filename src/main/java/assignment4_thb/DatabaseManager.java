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
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String USER = "root";
    private static final String PASS = "root";

    public static void initializeDatabase() {
        // Set locale to British English for DD/MM/YYYY date format
        Locale.setDefault(Locale.forLanguageTag("en-GB"));

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
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
                "user_id INT AUTO_INCREMENT PRIMARY KEY," +
                "username VARCHAR(50) NOT NULL UNIQUE," +
                "password VARCHAR(255) NOT NULL," +
                "email VARCHAR(100) NOT NULL," +
                "user_type VARCHAR(20) NOT NULL," + // 'customer' or 'admin'
                "name VARCHAR(100) NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        // Hotels table (already exists in your schema)
        // Hotel rooms table (already exists in your schema)
        // Room images table (already exists in your schema)

        // Bookings table
        String createBookingsTable = "CREATE TABLE IF NOT EXISTS bookings (" +
                "booking_id INT AUTO_INCREMENT PRIMARY KEY," +
                "room_id INT NOT NULL," +
                "user_id INT NOT NULL," +
                "check_in_date DATE NOT NULL," +
                "check_out_date DATE NOT NULL," +
                "num_guests INT NOT NULL," +
                "total_price DECIMAL(10,2) NOT NULL," +
                "booking_status VARCHAR(20) NOT NULL," + // 'pending', 'confirmed', 'cancelled'
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (room_id) REFERENCES hotel_rooms(room_id)," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id)" +
                ");";

        // Reviews table
        String createReviewsTable = "CREATE TABLE IF NOT EXISTS reviews (" +
                "review_id INT AUTO_INCREMENT PRIMARY KEY," +
                "hotel_id INT NOT NULL," +
                "user_id INT NOT NULL," +
                "rating INT NOT NULL," + // 1-5
                "comment TEXT," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (hotel_id) REFERENCES hotels(hotel_id)," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id)" +
                ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
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

        // Sample bookings
        String insertBookings = "INSERT INTO bookings (room_id, user_id, check_in_date, check_out_date, num_guests, total_price, booking_status) VALUES "
                +
                "(1, 1, '2024-06-01', '2024-06-05', 2, 400.00, 'confirmed')," +
                "(2, 2, '2024-07-15', '2024-07-20', 4, 1000.00, 'pending')," +
                "(3, 1, '2024-08-01', '2024-08-03', 6, 900.00, 'confirmed');";

        // Sample reviews
        String insertReviews = "INSERT INTO reviews (hotel_id, user_id, rating, comment) VALUES " +
                "(1, 1, 5, 'Excellent stay at Grand Hotel! The staff was very friendly.')," +
                "(1, 2, 4, 'Great location and comfortable rooms. Would recommend.')," +
                "(2, 1, 5, 'Mountain View Hotel offers spectacular views and great service.');";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(insertUsers);
            stmt.execute(insertBookings);
            stmt.execute(insertReviews);
        }
    }
}