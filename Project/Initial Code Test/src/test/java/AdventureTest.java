import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class AdventureTest {

    Adventure adventure;

    @Before
    public void initialization() {
        adventure = new Adventure(); // Initialize the Adventure object
    }

    @Test
    public void testInitialHealth() {
        // Test the initial health of the player
        assertEquals(100, Adventure.lifeChecker);
        System.out.println("Initial health is 100 HP.");
    }

    @Test
    public void testHealthAfterHealing() {
        // Test the healing process
        adventure.damage(50); // Damage the player to 50 HP
        adventure.heal(20);   // Heal the player by 20 HP
        assertEquals(70, Adventure.lifeChecker);
        System.out.println("After healing, health is 70 HP.");
    }

    @Test
    public void testHealthAfterDamage() {
        // Test the damage process
        adventure.damage(30); // Damage the player by 30 HP
        assertEquals(70, Adventure.lifeChecker);
        System.out.println("After taking damage, health is 70 HP.");
    }

    @Test
    public void testNoHealthBelowZero() {
        // Test that health doesn't go below 0
        adventure.damage(150); // Deal more damage than the player's total health
        assertEquals(0, Adventure.lifeChecker); // Health should not go below 0
        System.out.println("Health should not go below 0, and it's currently: " + Adventure.lifeChecker + " HP.");
    }

    @Test
    public void testHealingNotExceedingMax() {
        // Test that healing doesn't exceed max health (100)
        adventure.damage(10);  // Reduce health to 90
        adventure.heal(20);    // Attempt to heal by 20, but max health is 100
        assertEquals(100, Adventure.lifeChecker); // Health should not exceed 100
        System.out.println("Healing should not exceed 100 HP, and it's currently: " + Adventure.lifeChecker + " HP.");
    }

    @Test
    public void testJourneyProgress() {
        // Test that the journey progresses
        int initialProgress = adventure.journeyProgress;
        adventure.journey(); // Go on a journey
        assertEquals(initialProgress + 1, adventure.journeyProgress); // Journey progress should increase by 1
        System.out.println("Journey progress increased to: " + adventure.journeyProgress);
    }

    @Test
    public void testBankruptcy() {
        // Test the bankruptcy scenario
        Adventure.bankrupcy(); // Declare bankruptcy
        assertEquals(0, Adventure.lifeChecker); // Life checker should be 0 after bankruptcy
        System.out.println("After declaring bankruptcy, health is 0 HP.");
    }
}
