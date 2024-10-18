import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class AdventureTest {

    private DungeonPath dungeonPath;
    private Room testRoom;
    private Enemy testEnemy;
    private Weapon testWeapon;
    private HealingPotion testPotion;


    @Before
    public void initialize() {
        dungeonPath = new DungeonPath("Sir Bloo");
        // Clear any previous state to ensure tests run independently
        dungeonPath.weapons.clear();
        dungeonPath.healingPotions.clear();
        dungeonPath.currentInventoryWeight = 0; // Reset inventory weight
        dungeonPath.inventoryWeightLimit = 50; // Reset inventory weight limit
        testEnemy = new Enemy("Goblin", 50, 10);
        testWeapon = new Weapon("Sword", 10, 15);
        testPotion = new HealingPotion("Small Potion", 5, 20);
        testRoom = new Room("A dark, cold room", -10, testEnemy, testWeapon, 1, new int[]{0, 1, 0, 0}, "RoomMaster1");
    }

    // Test 1: Verifying room creation
    @Test
    public void testRoomCreation() {
        assertEquals("A dark, cold room", testRoom.description);
        assertEquals(-10, testRoom.healthChange);
        assertEquals(1, testRoom.roomNumber);
    }

    // Test 2: Verify room has an enemy
    @Test
    public void testRoomHasEnemy() {
        assertEquals("Goblin", testRoom.enemy.name);
        assertEquals(50, testRoom.enemy.health);
    }

    // Test 3: Verify room has a weapon
    @Test
    public void testRoomHasWeapon() {
        assertTrue(testRoom.item instanceof Weapon);
        assertEquals("Sword", testRoom.item.getName());
    }

    // Test 4: Verify enemy takes damage
    @Test
    public void testEnemyTakeDamage() {
        testEnemy.takeDamage(20);
        assertTrue(testEnemy.isAlive());
        testEnemy.takeDamage(40);
        assertTrue(!testEnemy.isAlive());
    }

    // Test 5: Test adding weapon to inventory
    @Test
    public void testAddWeaponToInventory() {
        Inventory inventory = new Inventory(50);
        Weapon sword = new Weapon("Sword", 10, 15); // Name: Sword, Weight: 10, Damage: 15
        assertTrue(inventory.addToInventory(sword)); // Adding weapon to inventory
        assertEquals(1, inventory.weapons.size()); // Check if the weapon was added
    }

    // Test 6: Test adding healing potion to inventory
    @Test
    public void testAddPotionToInventory() {
        dungeonPath.healingPotions.add(testPotion); // Directly add potion
        assertEquals(1, dungeonPath.healingPotions.size()); // Check if only one potion is added
    }

    // Test 7: Test life checker initialization
    @Test
    public void testLifeCheckerInitialization() {
        assertEquals(100, dungeonPath.lifeChecker);
    }

    // Test 8: Test room visit count
    @Test
    public void testRoomVisitCount() {
        Room room = new Room("Test Room", 0, null, null, 1, new int[]{}, null);
        assertEquals(0, room.getVisitCount());  // Initially, visit count should be 0
        room.visitRoom();  // Visit the room
        assertEquals(1, room.getVisitCount());  // Now it should be 1
    }
    

    // Test 9: Test adding to visited rooms
    @Test
    public void testAddToVisitedRooms() {
        dungeonPath.visitedRooms.add(1);
        assertTrue(dungeonPath.visitedRooms.contains(1));
    }

    // Test 10: Test inventory weight limit
    @Test
    public void testInventoryWeightLimit() {
        assertEquals(50, dungeonPath.inventoryWeightLimit);
    }

    // Test 11: Verify adding an item to the inventory
    @Test
    public void testAddToInventory() {
        Item testItem = new Item("Sword", 5); // Example item with weight
        dungeonPath.addToInventory(testItem); // Add the item to the inventory
        assertTrue(dungeonPath.getInventory().contains(testItem)); // Check if the item is in the inventory
    }

    // Test 12: Verify calculating total inventory weight
    @Test
    public void testGetInventoryWeight() {
        Item item1 = new Item("Sword", 5); // Item with weight 5
        Item item2 = new Item("Shield", 3); // Item with weight 3
        dungeonPath.addToInventory(item1); // Add item1 to the inventory
        dungeonPath.addToInventory(item2); // Add item2 to the inventory
        int expectedWeight = item1.getWeight() + item2.getWeight(); // Expected weight
        assertEquals(expectedWeight, dungeonPath.getInventoryWeight()); // Check if the total weight is correct
    }


    // Test 13: Test weapon attack power
    @Test
    public void testWeaponAttackPower() {
        assertEquals(15, testWeapon.getAttackPower());
    }

    // Test 14: Test potion heal amount
    @Test
    public void testPotionHealAmount() {
        assertEquals(20, testPotion.getHealAmount());
    }

    // Test 15: Test enemy attack power
    @Test
    public void testEnemyAttackPower() {
        assertEquals(10, testEnemy.attackPower);
    }

    // Test 16: Test item name
    @Test
    public void testItemName() {
        assertEquals("Sword", testWeapon.getName());
    }

    // Test 17: Test enemy death
    @Test
    public void testEnemyDeath() {
        testEnemy.takeDamage(60);
        assertTrue(!testEnemy.isAlive());
    }

    // Test 18: Test room master
    @Test
    public void testRoomMaster() {
        assertEquals("RoomMaster1", testRoom.roomMaster);
    }

    // Test 19: Test defeating room master
    @Test
    public void testRoomMasterDefeated() {
        testRoom.masterDefeated = true;
        assertTrue(testRoom.masterDefeated);
    }

    // Test 20: Test visited room status
    @Test
    public void testRoomVisitedStatus() {
        testRoom.visited = true;
        assertTrue(testRoom.visited);
    }

    // Test 21: Verify Room Health Impact on Player Life
    @Test
    public void testRoomHealthImpact() {
        // Setup
        Player player = new Player();
        Room room = new Room(
            "A dark room that drains your energy.", // Description
            -10, // Health change (negative impact)
            null, // No enemy in this room
            null, // No item in this room
            1, // Room number
            new int[]{}, // No connections for this test
            null // No master
        );

        // Act
        player.enterRoom(room);

        // Assert
        assertEquals(90, player.getHealth()); // Expecting health to be 90 after entering the room
    }


// Test 22: Verify Player Cannot Move Without Defeating Room Master
@Test
public void testCannotMoveWithoutDefeatingMaster() {
    testRoom.masterDefeated = false; // Master not yet defeated
    boolean canMove = dungeonPath.canMoveToNextRoom(testRoom);
    assertTrue(!canMove); // Should not allow movement
}

// Test 23: Verify Player Can Move After Defeating Room Master
@Test
public void testCanMoveAfterDefeatingMaster() {
    testRoom.masterDefeated = true; // Master defeated
    boolean canMove = dungeonPath.canMoveToNextRoom(testRoom);
    assertTrue(canMove); // Movement should be allowed
}

// Test 24: Verify Adding Visited Room to History
@Test
public void testAddVisitedRoom() {
    dungeonPath.addVisitedRoom(1); // Mark room 1 as visited
    assertTrue(dungeonPath.visitedRooms.contains(1)); // Verify it's recorded
}

// Test 25: Verify Duplicate Room Visits are Handled Correctly
@Test
public void testDuplicateRoomVisits() {
    dungeonPath.addVisitedRoom(1); // Visit room 1
    dungeonPath.addVisitedRoom(1); // Visit again
    assertEquals(1, dungeonPath.visitedRooms.size()); // No duplicate entries
}

// Test 26: Verify Room Master Status Resets on Restart
@Test
public void testRoomMasterReset() {
    Room testRoom = dungeonPath.rooms.get(0); // Get the specific room
    testRoom.masterDefeated = true; // Set master as defeated initially
    dungeonPath.resetGame(); // Reset the game
    assertFalse(testRoom.masterDefeated); // Verify that masterDefeated is reset to false
}

// Test 27: Verify Inventory Weight After Adding Weapon
@Test
public void testInventoryWeightAfterAddingWeapon() {
    dungeonPath.addToInventory(testWeapon); // Add weapon with weight 10
    assertEquals(10, dungeonPath.getInventoryWeight()); // Verify weight
}

// Test 28: Verify Player Health Resets on Restart
@Test
public void testPlayerHealthReset() {
    dungeonPath.lifeChecker = 50; // Health reduced
    dungeonPath.resetGame(); // Reset game state
    assertEquals(100, dungeonPath.lifeChecker); // Verify health reset
}

// Test 29: Verify Player Cannot Add Items Exceeding Weight Limit
@Test
public void testCannotAddItemExceedingWeightLimit() {
    Inventory inventory = new Inventory(10); // Limit set to 10
    Weapon axe = new Weapon("Axe", 12, 20); // Name: Axe, Weight: 12, Damage: 20
    assertFalse(inventory.addToInventory(axe)); // This should fail because the weight exceeds the limit
}

// Test 30: Verify Room Description is Correct
@Test
public void testRoomDescription() {
    assertEquals("A dark, cold room", testRoom.getDescription()); // Verify description
}

}
