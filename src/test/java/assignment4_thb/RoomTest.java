package assignment4_thb;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class RoomTest {
    private Room room;

    @BeforeEach
    public void setUp() {
        room = new Room("Single", 100.0, 1, true);
    }

    @AfterEach
    public void tearDown() {
        room = null;
    }

}
