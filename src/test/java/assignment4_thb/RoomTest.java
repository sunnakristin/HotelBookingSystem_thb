package assignment4_thb;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class RoomTest {
    private Room room;
    private static final String ROOM_TYPE = "Standard";
    private static final double ROOM_PRICE = 100.0;
    private static final int NUMBER_OF_GUESTS = 2;
    private static final boolean AVAILABILITY = true;

    @BeforeEach
    void setUp() {
       room = new Room(ROOM_TYPE, ROOM_PRICE, NUMBER_OF_GUESTS, AVAILABILITY);
    }

    @AfterEach
    void tearDown() {
        room = null;
    }

    @Test
    void testPrice() {
        double newPrice = 200.0;
        room.setPrice(newPrice);
        assertEquals(newPrice, room.getPrice());
    }

    @Test
    void testType() {
        String newType = "Deluxe";
        room.setType(newType);
        assertEquals(newType, room.getType());
    }

    @Test
    void testAvailability() {
        room.setAvailability(false);
        assertFalse(room.isAvailable());
    }

    @Test
    void testNumberOfguests() {
        room.setNumberOfguests();
        assertEquals(NUMBER_OF_GUESTS, room.getNumberOfguests());
    }

    @Test
    void testInitialValues() {
        assertEquals(ROOM_TYPE, room.getType());
        assertEquals(ROOM_PRICE, room.getPrice());
        assertEquals(NUMBER_OF_GUESTS, room.getNumberOfguests());
        assertTrue(room.isAvailable());
        assertNotNull(room.getRoomImages());
        assertTrue(room.getRoomImages().isEmpty());
    }

    @Test
    void testCheckAvailability() {
        // Test with valid dates
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = LocalDate.now().plusDays(1);
        assertTrue(room.checkAvailability(checkIn, checkOut));

        // Test with unavailable room
        room.setAvailability(false);
        assertFalse(room.checkAvailability(checkIn, checkOut));
    }
}