package assignment4_thb;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:hotel.db";
    private static Connection connection = null;

    private DatabaseManager() {}

    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
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

        String insertRooms = "INSERT INTO hotel_rooms (hotel_id, type, price, number_of_guests) VALUES " +
                "(1, 'Standard', 100.00, 1)," +
                "(1, 'Deluxe', 200.00, 2)," +
                "(1, 'Suite', 300.00, 3)," +
                "(2, 'Standard', 230.00, 3)," +
                "(2, 'Deluxe', 180.00, 4)," +
                "(2, 'Suite', 270.00, 6)," +
                "(3, 'Standard', 120.00, 3)," +
                "(3, 'Deluxe', 240.00, 1)," +
                "(3, 'Suite', 360.00, 2)," +
                "(4, 'Standard', 80.00, 19)," +
                "(4, 'Deluxe', 300.00, 4)," +
                "(4, 'Suite', 450.00, 6)," +
                "(5, 'Standard', 120.00, 2)," +
                "(5, 'Deluxe', 240.00, 4)," +
                "(6, 'Standard', 360.00, 3)," +
                "(6, 'Standard', 400.00, 4)," +
                "(7, 'Deluxe', 550.00, 5)," +
                "(7, 'Suite', 700.00, 10);";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(insertUsers);
            stmt.execute(insertHotels);
            stmt.execute(insertRooms);
        }
    }

    // Method to update room availability
    public static boolean checkRoomAvailability(int roomId, LocalDate day) throws SQLException {
        try (Connection conn = getConnection()) {
            String qry = "select b.booking_id from bookings b " +
                    "where b.room_id = ? and b.check_in_date < ? and b.check_out_date > ?";
            try (PreparedStatement pstmt = conn.prepareStatement(qry)) {
                pstmt.setInt(1, roomId);
                pstmt.setDate(2, Date.valueOf(day));
                pstmt.setDate(3, Date.valueOf(day));
                var res = pstmt.executeQuery();
                if (res.next()) {
                    return false;
                }
            }
        }
        return true;
    }

    // Method to save a booking
    public static int saveBooking(int userId, int roomId, LocalDate checkInDate, LocalDate checkOutDate, int numGuests, double totalPrice) throws SQLException {
        int bookingId = 0;
        try (Connection conn = getConnection()) {
            String insertQuery = "INSERT INTO bookings (room_id, user_id, check_in_date, check_out_date, num_guests, total_price, booking_status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, 1)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) { //(insertQuery, Statement.RETURN_GENERATED_KEYS)
                pstmt.setInt(1, roomId);
                pstmt.setInt(2, userId);
                pstmt.setDate(3, Date.valueOf(checkInDate));
                pstmt.setDate(4, Date.valueOf(checkOutDate));
                pstmt.setInt(5, numGuests);
                pstmt.setDouble(6, totalPrice);
                pstmt.executeUpdate();

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        bookingId = rs.getInt(1); // Get generated booking ID
                    }
                }
            }
        }
        return bookingId;
    }

    public static List<BookingInfo> LoadBookings(int userId) {
        List<BookingInfo> bookings = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:hotel.db")) {
            String query =
                    "select b.booking_id, h.name as hotel_name, r.type as room_type, h.location, b.num_guests, b.total_price, b.check_in_date, b.check_out_date " +
                            "  from bookings b " +
                            "  join hotel_rooms r on r.room_id=b.room_id" +
                            "  join hotels h on h.hotel_id=r.hotel_id" +
                            "  join users u on u.user_id=b.user_id" +
                            " where b.user_id = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, userId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        bookings.add(new BookingInfo(
                                rs.getInt("booking_id"),
                                rs.getString("hotel_name"),
                                rs.getString("room_type"),
                                rs.getString("location"),
                                rs.getInt("num_guests"),
                                rs.getDouble("total_price"),
                                rs.getDate("check_in_date").toLocalDate(),
                                rs.getDate("check_out_date").toLocalDate()
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error loading hotels: " + e.getMessage());
        }

        System.out.println("Loaded " + bookings.size() + " hotels from the database."); //sjá hversu mörg hótel eru sett í db
        return bookings;
    }

    public static void DeleteBooking(int bookingId) throws SQLException {
        try (Connection conn = getConnection()) {
            String deleteQuery = "DELETE FROM bookings WHERE booking_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
                pstmt.setInt(1, bookingId);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error deleting booking: " + e.getMessage());
            throw e;
        }
    }


}