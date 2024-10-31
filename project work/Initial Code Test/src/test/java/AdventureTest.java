import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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

// Test 31: Verify Player Health Does Not Go Negative
@Test
public void testPlayerHealthNonNegative() {
    Player player = new Player();
    Room dangerousRoom = new Room("Deadly Trap", -200, null, null, 1, new int[]{}, null); // High negative health impact
    player.enterRoom(dangerousRoom);
    assertEquals(0, player.getHealth()); // Health should not be negative
}

// Test 32: Verify Inventory Can Hold Multiple Potions
@Test
public void testAddMultiplePotionsToInventory() {
    DungeonPath dungeonPath = new DungeonPath("Knight");

    HealingPotion potion1 = new HealingPotion("Small Potion", 2, 10);
    HealingPotion potion2 = new HealingPotion("Large Potion", 3, 20);

    dungeonPath.addToInventory(potion1);
    dungeonPath.addToInventory(potion2);

    assertEquals(2, dungeonPath.getInventory().getHealingPotions().size());
}

// Test 33: Verify Player Cannot Enter Room if Health is Zero
@Test
public void testPlayerCannotEnterRoomWithZeroHealth() {
    Player player = new Player();
    player.enterRoom(new Room("Safe Room", -100, null, null, 1, new int[]{}, null)); // Reduce health to 0
    assertEquals(0, player.getHealth()); // Verify player cannot enter rooms with 0 health
}

// Test 34: Verify Player Inventory After Removing Multiple Items
@Test
public void testRemoveMultipleItemsFromInventory() {
    dungeonPath.addToInventory(testWeapon); // Add weapon
    dungeonPath.addToInventory(testPotion); // Add potion
    dungeonPath.removeItem(testWeapon); // Remove weapon
    dungeonPath.removeItem(testPotion); // Remove potion
    assertTrue(dungeonPath.getInventory().isEmpty()); // Inventory should be empty after removal
}

// Test 35: Verify Resetting Game Resets Inventory
@Test
public void testResetGameResetsInventory() {
    dungeonPath.addToInventory(testWeapon); // Add weapon to inventory
    dungeonPath.resetGame(); // Reset game
    assertTrue(dungeonPath.getInventory().isEmpty()); // Inventory should be empty after reset
}


// Test 36: Verify Health Potion Increases Player Health
@Test
public void testPotionIncreasesPlayerHealth() {
    Player player = new Player();
    HealingPotion potion = new HealingPotion("Small Potion", 5, 20);
    player.enterRoom(new Room("Test Room", -30, null, null, 1, new int[]{}, null)); // Reduce health
    assertEquals(70, player.getHealth()); // Health should be 70 after damage
    player.useHealingPotion(potion); // Use healing potion
    assertEquals(90, player.getHealth()); // Health should increase to 90
}

// Test 37: Verify Room Description Change After Master Defeat
@Test
public void testRoomDescriptionAfterMasterDefeat() {
    Room masterRoom = new Room("Master's Lair", 0, null, null, 1, new int[]{}, "Dungeon Master");
    masterRoom.masterDefeated = true; // Defeat master
    String newDescription = masterRoom.getDescription() + " The master has been defeated.";
    assertEquals("Master's Lair The master has been defeated.", newDescription); // Check updated description
}

// Test 38: Verify Enemy Attack Reduces Player Health
@Test
public void testEnemyAttackReducesPlayerHealth() {
    Player player = new Player();
    Enemy enemy = new Enemy("Orc", 30, 15); // Enemy deals 15 damage
    player.receiveDamage(enemy.attackPower); // Enemy attacks player
    assertEquals(85, player.getHealth()); // Health should decrease by 15
}

// Test 39: Verify Cannot Exceed Maximum Healing with Potion
@Test
public void testPotionDoesNotExceedMaxHealth() {
    Player player = new Player();
    HealingPotion potion = new HealingPotion("Large Potion", 5, 50); // Healing more than required
    player.useHealingPotion(potion); // Use potion
    assertEquals(100, player.getHealth()); // Health should not exceed 100
}

// Test 40: Verify DungeonPath Room Connections
@Test
public void testRoomConnections() {
    Room room1 = new Room("Room 1", 0, null, null, 1, new int[]{2}, null); // Connects to Room 2
    Room room2 = new Room("Room 2", 0, null, null, 2, new int[]{1}, null); // Connects back to Room 1
    dungeonPath.rooms.add(room1);
    dungeonPath.rooms.add(room2);
    assertEquals(2, room1.connections[0]); // Room 1 connects to Room 2
    assertEquals(1, room2.connections[0]); // Room 2 connects to Room 1
}
// Test 41: Verify Room 1 Description
@Test
public void testRoom1Description() {
    Room room1 = new Room("Floor 1, Room 1: The Entrance Hall - A grand chamber with ancient pillars.", 
                          0, new Enemy("Guard Goblin", 20, 5), 
                          new HealingPotion("Small Health Potion", 1, 20), 1, new int[]{2}, null);
    dungeonPath.rooms.add(room1);
    assertEquals("Floor 1, Room 1: The Entrance Hall - A grand chamber with ancient pillars.", room1.getDescription());
}

// Test 42: Verify Room 5 Description
@Test
public void testRoom5Description() {
    Room room5 = new Room("Floor 1, Room 5: The Mess Hall - Long tables covered in cobwebs.", 
                          0, new Enemy("Hungry Troll", 40, 10), 
                          null, 5, new int[]{6, 4}, null);
    dungeonPath.rooms.add(room5);
    assertEquals("Floor 1, Room 5: The Mess Hall - Long tables covered in cobwebs.", room5.getDescription());
}

// Test 43: Verify Room Connections on Floor 1, Room 3
@Test
public void testRoom3Connections() {
    Room room3 = new Room("Floor 1, Room 3: The Training Ground - Practice dummies stand silently.", 
                          0, new Enemy("Training Golem", 30, 8), 
                          null, 3, new int[]{4, 2}, null);
    dungeonPath.rooms.add(room3);
    assertEquals(4, room3.connections[0]); // Room 3 connects to Room 4
    assertEquals(2, room3.connections[1]); // Room 3 connects back to Room 2
}

// Test 44: Verify Enemy Attributes in Room 1
@Test
public void testRoom1EnemyAttributes() {
    Room room1 = new Room("Floor 1, Room 1: The Entrance Hall", 
                          0, new Enemy("Guard Goblin", 20, 5), 
                          null, 1, new int[]{2}, null);
    dungeonPath.rooms.add(room1);
    Enemy enemy = room1.getEnemy();
    assertEquals("Guard Goblin", enemy.getName());
    assertEquals(20, enemy.getHealth());
    assertEquals(5, enemy.getAttackPower());
}

// Test 45: Verify Boss Room Enemy Attributes in Room 10
@Test
public void testRoom10BossAttributes() {
    Room room10 = new Room("Floor 1, Room 10: The Boss Chamber", 
                           0, new Enemy("Floor Guardian", 100, 20), 
                           null, 10, new int[]{9}, "Floor Master");
    dungeonPath.rooms.add(room10);
    Enemy boss = room10.getEnemy();
    assertEquals("Floor Guardian", boss.getName());
    assertEquals(100, boss.getHealth());
    assertEquals(20, boss.getAttackPower());
}

// Test 46: Verify Weapon Attributes in Room 2
@Test
public void testRoom2WeaponAttributes() {
    Room room2 = new Room("Floor 1, Room 2: The Armory", 
                          0, new Enemy("Skeleton Warrior", 25, 7), 
                          new Weapon("Rusty Dagger", 2, 8), 2, new int[]{3, 1}, null);
    dungeonPath.rooms.add(room2);
    Weapon weapon = (Weapon) room2.getItem();
    assertEquals("Rusty Dagger", weapon.getName());
    assertEquals(2, weapon.getWeight());
    assertEquals(8, weapon.getAttackPower());
}

// Test 47: Verify Healing Potion Attributes in Room 4
@Test
public void testRoom4PotionAttributes() {
    Room room4 = new Room("Floor 1, Room 4: The Barracks", 
                          0, new Enemy("Undead Soldier", 35, 9), 
                          new HealingPotion("Medium Health Potion", 2, 40), 4, new int[]{5, 3}, null);
    dungeonPath.rooms.add(room4);
    HealingPotion potion = (HealingPotion) room4.getItem();
    assertEquals("Medium Health Potion", potion.getName());
    assertEquals(2, potion.getWeight());
    assertEquals(40, potion.getHealAmount());
}

// Test 48: Verify Room without Items in Room 3
@Test
public void testRoom3WithoutItems() {
    Room room3 = new Room("Floor 1, Room 3: The Training Ground", 
                          0, new Enemy("Training Golem", 30, 8), 
                          null, 3, new int[]{4, 2}, null);
    dungeonPath.rooms.add(room3);
    assertNull(room3.getItem());
}

// Test 49: Verify Room Master Presence in Room 10
@Test
public void testRoom10RoomMasterPresence() {
    Room room10 = new Room("Floor 1, Room 10: The Boss Chamber", 
                           0, new Enemy("Floor Guardian", 100, 20), 
                           null, 10, new int[]{9}, "Floor Master");
    dungeonPath.rooms.add(room10);
    assertEquals("Floor Master", room10.getRoomMaster());
}

// Test 50: Verify Adjacent Room Connections
@Test
public void testAdjacentRoomConnections() {
    Room room5 = new Room("Floor 1, Room 5: The Mess Hall", 
                          0, new Enemy("Hungry Troll", 40, 10), 
                          null, 5, new int[]{6, 4}, null);
    Room room6 = new Room("Floor 1, Room 6: The Kitchen", 
                          0, new Enemy("Chef Goblin", 45, 11), 
                          new Weapon("Kitchen Knife", 1, 12), 6, new int[]{7, 5}, null);
    dungeonPath.rooms.add(room5);
    dungeonPath.rooms.add(room6);
    assertEquals(6, room5.connections[0]); // Room 5 connects to Room 6
    assertEquals(5, room6.connections[1]); // Room 6 connects back to Room 5
}

// Test 51: Verify Room 1 Connection Forward Only
@Test
public void testRoom1SingleConnection() {
    Room room1 = new Room("Floor 1, Room 1: The Entrance Hall", 
                          0, new Enemy("Guard Goblin", 20, 5), 
                          null, 1, new int[]{2}, null);
    dungeonPath.rooms.add(room1);
    assertEquals(2, room1.connections[0]); // Room 1 connects to Room 2
}

// Test 52: Verify Room 7 Healing Potion Weight
@Test
public void testRoom7PotionWeight() {
    Room room7 = new Room("Floor 1, Room 7: The Pantry", 
                          0, new Enemy("Giant Rat", 50, 12), 
                          new HealingPotion("Large Health Potion", 3, 60), 7, new int[]{8, 6}, null);
    dungeonPath.rooms.add(room7);
    HealingPotion potion = (HealingPotion) room7.getItem();
    assertEquals(3, potion.getWeight());
}

// Test 53: Verify Room with No Enemy
@Test
public void testRoomWithNoEnemy() {
    Room room8 = new Room("Floor 1, Room 8: The Wine Cellar", 
                          0, null, 
                          null, 8, new int[]{9, 7}, null);
    dungeonPath.rooms.add(room8);
    assertNull(room8.getEnemy());
}

// Test 54: Verify Room 3 Enemy Attack Power
@Test
public void testRoom3EnemyAttackPower() {
    Room room3 = new Room("Floor 1, Room 3: The Training Ground", 
                          0, new Enemy("Training Golem", 30, 8), 
                          null, 3, new int[]{4, 2}, null);
    dungeonPath.rooms.add(room3);
    assertEquals(8, room3.getEnemy().getAttackPower());
}

// Test 55: Verify Room Master Absence in Non-Boss Room
@Test
public void testNoRoomMasterInNonBossRoom() {
    Room room5 = new Room("Floor 1, Room 5: The Mess Hall", 
                          0, new Enemy("Hungry Troll", 40, 10), 
                          null, 5, new int[]{6, 4}, null);
    dungeonPath.rooms.add(room5);
    assertNull(room5.getRoomMaster());
}

// Test 56: Verify Room Health Impact for Trap Room
@Test
public void testTrapRoomHealthImpact() {
    Room trapRoom = new Room("Trap Room", -20, null, null, 1, new int[]{}, null);
    dungeonPath.rooms.add(trapRoom);
    assertEquals(-20, trapRoom.getHealthChange());
}

// Test 57: Verify Room 10 Enemy Health After Defeat
@Test
public void testBossRoomEnemyHealthAfterDefeat() {
    Room room10 = new Room("Floor 1, Room 10: The Boss Chamber", 
                           0, new Enemy("Floor Guardian", 100, 20), 
                           null, 10, new int[]{9}, "Floor Master");
    dungeonPath.rooms.add(room10);

    Enemy boss = room10.enemy; // Get the enemy from the room

    // Assert that the boss is alive before taking damage
    assertTrue("Boss should be alive initially", boss.isAlive());

    // Simulate defeating the boss
    boss.takeDamage(100); // Inflict damage equal to the boss's health

    // Assert that the boss is not alive after taking damage
    assertFalse("Boss should be defeated after taking 100 damage", boss.isAlive());

    // Assert that the boss's health is now 0
    assertEquals("Boss health should be 0 after defeat", 0, boss.health);
}

}
