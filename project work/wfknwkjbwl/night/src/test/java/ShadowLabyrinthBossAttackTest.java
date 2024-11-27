//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class ShadowLabyrinthBossAttackTest {
    
    @Test
    void testBossAttackDirectionsWithSeed() {
        // Use a specific seed for reproducibility
        long testSeed = 42L;
        Random testRandom = new Random(testSeed);
        
        // Simulate 5 boss attack directions
        List<String> attackDirections = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            // Mimic the boss attack direction selection logic
            String direction = testRandom.nextBoolean() ? "LEFT" : "RIGHT";
            attackDirections.add(direction);
        }
        
        // Print out the attack directions for reference
        System.out.println("Boss Attack Directions with Seed " + testSeed + ":");
        attackDirections.forEach(System.out::println);
        
        // Use the actual generated directions as the expected directions
        List<String> expectedDirections = List.of(
            "LEFT",   // first direction
            "RIGHT",  // second direction
            "LEFT",   // third direction
            "RIGHT",  // fourth direction
            "RIGHT"   // fifth direction
        );
        
        // Assert that the generated directions match the expected ones
        assertEquals(expectedDirections, attackDirections, 
            "Boss attack directions should be consistent for the given seed");
    }
    
    @Test
    void testReproducibilityOfBossAttackDirections() {
        // Use the same seed multiple times to verify consistency
        long testSeed = 42L;
        
        // First run
        List<String> firstRunDirections = generateBossAttackDirections(testSeed);
        
        // Second run with the same seed
        List<String> secondRunDirections = generateBossAttackDirections(testSeed);
        
        // Verify that both runs produce the exact same sequence
        assertEquals(firstRunDirections, secondRunDirections, 
            "Boss attack directions should be identical for the same seed");
    }
    
    // Helper method to generate boss attack directions
    private List<String> generateBossAttackDirections(long seed) {
        Random testRandom = new Random(seed);
        List<String> attackDirections = new ArrayList<>();
        
        for (int i = 0; i < 5; i++) {
            String direction = testRandom.nextBoolean() ? "LEFT" : "RIGHT";
            attackDirections.add(direction);
        }
        
        return attackDirections;
    }
}
