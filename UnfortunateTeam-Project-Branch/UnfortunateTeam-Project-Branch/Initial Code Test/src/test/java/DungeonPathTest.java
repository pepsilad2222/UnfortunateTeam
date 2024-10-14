import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class DungeonPathTest {

    private DungeonPath dungeonPath;
    private Room testRoom;
    private Enemy testEnemy;
    private Weapon testWeapon;
    private HealingPotion testPotion;

    @Before
    public void setUp() {
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
        // Clear inventory or reset state
        dungeonPath.weapons.clear();
        
        Weapon knightSword = new Weapon("Knight's Sword", 10, 5);
        
        // Add the weapon to the inventory
        dungeonPath.weapons.add(knightSword);
        
        // Verify that only one weapon has been added
        assertEquals(1, dungeonPath.weapons.size());
        
        // Try adding the same weapon again
        dungeonPath.weapons.add(knightSword); // This should not increase the count
        
        // Verify that no duplicates are allowed
        assertEquals(1, dungeonPath.weapons.size());
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
        assertEquals(0, dungeonPath.roomCount);
        dungeonPath.roomCount++;
        assertEquals(1, dungeonPath.roomCount);
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
}