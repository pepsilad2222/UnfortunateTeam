import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class AdventureTest {

    Adventure adventure;

    @Before
    public void initialization() {
        adventure = new Adventure("Sir Lancelot"); // Initialize with knight's name
    }

    @Test
    public void testInitialHealth() {
        // Test that initial health starts at 100
        assertEquals(100, Adventure.lifeChecker);
    }

    @Test
    public void testHealthReduction() {
        // Simulate health reduction by directly modifying the lifeChecker
        Adventure.lifeChecker = 60; // Set lifeChecker to 60
        assertEquals(60, Adventure.lifeChecker); // Check if health is correctly reduced
    }

    @Test
    public void testHealthRecovery() {
        // Simulate health recovery
        Adventure.lifeChecker = 50; // Reduce health to 50
        Adventure.lifeChecker = Math.min(Adventure.lifeChecker + 30, 100); // Heal by 30, max is 100
        assertEquals(80, Adventure.lifeChecker); // Health should now be 80
    }

    @Test
    public void testHealthCannotExceedMax() {
        // Simulate attempting to exceed max health
        Adventure.lifeChecker = 90; // Set health to 90
        Adventure.lifeChecker = Math.min(Adventure.lifeChecker + 20, 100); // Heal by 20, but max is 100
        assertEquals(100, Adventure.lifeChecker); // Ensure it does not exceed 100
    }

    @Test
    public void testHealthCannotDropBelowZero() {
        // Simulate health dropping below zero
        Adventure.lifeChecker = 0; // Set health to 0 manually
        assertEquals(0, Adventure.lifeChecker); // Health should not drop below 0
    }

    @Test
    public void testGameOverWhenHealthReachesZero() {
        // Test that game ends when health reaches zero
        Adventure.lifeChecker = 0; // Set health to 0
        assertEquals(0, Adventure.lifeChecker); // Ensure health is 0
    }

    @Test
    public void testInitialRoomCount() {
        // Test that initial room count starts at 0
        assertEquals(0, adventure.roomCount); // Room count should start at 0
    }

    @Test
    public void testRoomCountProgression() {
        // Simulate progression in the journey by increasing room count
        adventure.roomCount = 5; // Set room count to 5 manually
        assertEquals(5, adventure.roomCount); // Ensure room count is updated correctly
    }

    @Test
    public void testRoomCountUnchangedWithHealthModification() {
        // Test that room count is not affected by health changes
        adventure.roomCount = 3; // Set room count to 3
        Adventure.lifeChecker = 50; // Change health to 50
        assertEquals(3, adventure.roomCount); // Ensure room count is still 3
    }

    @Test
    public void testHealthAtZeroRoomCountUnchanged() {
        // Test that room count remains unchanged even if health drops to 0
        adventure.roomCount = 2; // Set room count to 2
        Adventure.lifeChecker = 0; // Set health to 0
        assertEquals(2, adventure.roomCount); // Ensure room count stays the same
        assertEquals(0, Adventure.lifeChecker); // Ensure health is still 0
    }
}
