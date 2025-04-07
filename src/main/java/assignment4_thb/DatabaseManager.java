package assignment4_thb;

import java.sql.*;
import java.time.LocalDate;
import java.util.Locale;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:hotel.db";
    private static Connection connection = null;

    // Private constructor to prevent instantiation
    private DatabaseManager() {}

    // Get a single connection instance (singleton pattern)
    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
            // Enable busy timeout to wait for the database to become available
            connection.createStatement().execute("PRAGMA busy_timeout = 30000;"); // 30 seconds
            // Enable WAL mode for better concurrency
            connection.createStatement().execute("PRAGMA journal_mode=WAL;");
        }
        return connection;
    }

    // Close the connection when the application shuts down
    public static synchronized void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            connection = null;
        }
    }

    public static void initializeDatabase() {
        Locale.setDefault(Locale.forLanguageTag("en-GB"));
        try (Connection conn = getConnection()) {
            // Drop existing tables to ensure a clean schema
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("DROP TABLE IF EXISTS bookings");
                stmt.execute("DROP TABLE IF EXISTS hotel_rooms");
                stmt.execute("DROP TABLE IF EXISTS hotels");
                stmt.execute("DROP TABLE IF EXISTS users");
            }
            createTables(conn);
            populateSampleData(conn);
        } catch (SQLException e) {
            System.out.println("Database initialization error: " + e.getMessage());
        }
    }

    private static void createTables(Connection conn) throws SQLException {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "password TEXT NOT NULL," +
                "email TEXT NOT NULL," +
                "name TEXT NOT NULL" +
                ");";

        String createHotelsTable = "CREATE TABLE IF NOT EXISTS hotels (" +
                "hotel_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "location TEXT NOT NULL," +
                "description TEXT NOT NULL" +
                ");";

        String createHotelRoomsTable = "CREATE TABLE IF NOT EXISTS hotel_rooms (" +
                "room_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "hotel_id INTEGER NOT NULL," +
                "type TEXT NOT NULL," +
                "price REAL NOT NULL," +
                "number_of_guests INTEGER NOT NULL," +
                "availability INTEGER NOT NULL DEFAULT 1," +
                "FOREIGN KEY (hotel_id) REFERENCES hotels(hotel_id)" +
                ");";

        String createBookingsTable = "CREATE TABLE IF NOT EXISTS bookings (" +
                "booking_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "room_id INTEGER NOT NULL," +
                "user_id INTEGER NOT NULL," +
                "check_in_date DATE NOT NULL," +
                "check_out_date DATE NOT NULL," +
                "num_guests INTEGER NOT NULL," +
                "total_price REAL NOT NULL," +
                "booking_status INTEGER NOT NULL DEFAULT 1," +
                "FOREIGN KEY (room_id) REFERENCES hotel_rooms(room_id)," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id)" +
                ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createHotelsTable);
            stmt.execute(createHotelRoomsTable);
            stmt.execute(createBookingsTable);
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
        System.out.println("Populating sample data...");
        String insertUsers = "INSERT INTO users (password, email, name) VALUES " +
                "('password123', 'katrin@gmail.com', 'Katrín')," +
                "('password123', 'silja@gmail.com', 'Silja Björk')," +
                "('password123', 'sunna@gmail.com', 'Sunna Kristín')," +
                "('password123', 'jonas@gmail.com', 'Jónas Bjarki');";

        String insertHotels = "INSERT INTO hotels (name, location, description) VALUES " +
                "('Grand Hotel', 'Reykjavik, Iceland', 'A sophisticated hotel is equipped with all you need for your stay in Reykjavík')," +
                "('Hótel 1001 Nótt', 'Egilsstaðir, Iceland', 'A family owned luxury hotel, beautifully situated in a quiet area by Lake Lagarfljót')," +
                "('Hótel Akureyri', 'Akureyri, Iceland', 'Cozy stay in Akureyri')," +
                "('Hótel Geysir', 'Geysir, Iceland', 'Near the famous Geysir hot springs')," +
                "('Hótel Borg', 'Reykjavik, Iceland', 'Historic hotel with modern amenities')," +
                "('Fosshótel', 'Húsavík, Iceland', 'Fosshotel Húsavík is a gem in this quaint northern Icelandic harbor town')," +
                "('EDITION', 'Reykjavík, Iceland', '5 star hotel in the center of Reykjavík');";

        String insertRooms = "INSERT INTO hotel_rooms (hotel_id, type, price, number_of_guests, availability) VALUES " +
                "(1, 'Standard', 100.00, 1, 1)," +
                "(1, 'Deluxe', 200.00, 2, 1)," +
                "(1, 'Suite', 300.00, 3, 1)," +
                "(2, 'Standard', 230.00, 3, 1)," +
                "(2, 'Deluxe', 180.00, 4, 1)," +
                "(2, 'Suite', 270.00, 6, 1)," +
                "(3, 'Standard', 120.00, 3, 1)," +
                "(3, 'Deluxe', 240.00, 1, 1)," +
                "(3, 'Suite', 360.00, 2, 1)," +
                "(4, 'Standard', 80.00, 19, 1)," +
                "(4, 'Deluxe', 300.00, 4, 1)," +
                "(4, 'Suite', 450.00, 6, 1)," +
                "(5, 'Standard', 120.00, 2, 1)," +
                "(5, 'Deluxe', 240.00, 4, 1)," +
                "(6, 'Standard', 360.00, 3, 1)," +
                "(6, 'Standard', 400.00, 4, 1)," +
                "(7, 'Deluxe', 550.00, 5, 1)," +
                "(7, 'Suite', 700.00, 10, 1);";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(insertUsers);
            stmt.execute(insertHotels);
            stmt.execute(insertRooms);
        }
    }

    // Method to update room availability
    public void updateRoomAvailability(int roomId, boolean availability) throws SQLException {
        try (Connection conn = getConnection()) {
            String updateQuery = "UPDATE hotel_rooms SET availability = ? WHERE room_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                pstmt.setInt(1, availability ? 1 : 0);
                pstmt.setInt(2, roomId);
                pstmt.executeUpdate();
            }
        }
    }

    // Method to save a booking
    public void saveBooking(int userId, int roomId, LocalDate checkInDate, LocalDate checkOutDate, int numGuests, double totalPrice) throws SQLException {
        try (Connection conn = getConnection()) {
            String insertQuery = "INSERT INTO bookings (room_id, user_id, check_in_date, check_out_date, num_guests, total_price, booking_status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, 1)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                pstmt.setInt(1, roomId);
                pstmt.setInt(2, userId);
                pstmt.setString(3, checkInDate.toString());
                pstmt.setString(4, checkOutDate.toString());
                pstmt.setInt(5, numGuests);
                pstmt.setDouble(6, totalPrice);
                pstmt.executeUpdate();
            }
        }
    }

    // Method to cancel a booking (update booking_status to 0)
    public void cancelBooking(int bookingId) throws SQLException {
        try (Connection conn = getConnection()) {
            String updateQuery = "UPDATE bookings SET booking_status = 0 WHERE booking_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                pstmt.setInt(1, bookingId);
                pstmt.executeUpdate();
            }
        }
    }
}