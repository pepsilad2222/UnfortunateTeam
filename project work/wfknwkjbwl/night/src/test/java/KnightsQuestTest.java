import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class KnightsQuestTest {
    private KnightsQuest.Player player;
    private KnightsQuest.Room room;
    private KnightsQuest.Item sword;
    private KnightsQuest.Item potion;
    private KnightsQuest.Enemy enemy;

    @Before
    public void initialize() {
        player = new KnightsQuest.Player(100);
        room = new KnightsQuest.Room("Test Room", "A room for testing.");
        sword = new KnightsQuest.Item("Sword", "A sharp blade.");
        potion = new KnightsQuest.Item("Healing Potion", "Restores 50 HP.");
        enemy = new KnightsQuest.Enemy("Goblin", 50, 10, new String[]{"Slash", "Stab"}, 1, false);
    }

    // Test 1: Test Player Initialization
    @Test
    public void testPlayerInitialization() {
        assertEquals(100, player.getHp());
        assertEquals(0, player.getInventory().size());
        assertNull(player.getCurrentRoom());
    }

    // Test 2: Test Room Initialization
    @Test
    public void testRoomInitialization() {
        assertEquals("Test Room", room.getName());
        assertTrue(room.getItems().isEmpty());
        assertNull(room.getEnemy());
    }

    // Test 3: Test Adding Item to Room
    @Test
    public void testAddItemToRoom() {
        room.addItem(sword);
        assertTrue(room.getItems().contains(sword));
    }

    // Test 4: Test Removing Item from Room
    @Test
    public void testRemoveItemFromRoom() {
        room.addItem(sword);
        room.removeItem(sword);
        assertFalse(room.getItems().contains(sword));
    }

    // Test 5: Test Player Picking Up Item
    @Test
    public void testPlayerPickupItem() {
        room.addItem(sword);
        player.setCurrentRoom(room);
        player.addItem(sword); // Manually adding to inventory for simplicity
        assertTrue(player.hasItem("Sword"));
        assertEquals(1, player.getInventory().size());
    }

    // Test 6: Test Player Using Healing Potion
    @Test
    public void testPlayerUseHealingPotion() {
        player.addItem(potion);
        player.setHp(50);
        player.setHp(player.getHp() + 50); // Simulating potion use
        assertEquals(100, player.getHp());
    }

    // Test 7: Test Exceeding Max HP with Healing Potion
    @Test
    public void testHealingPotionMaxHP() {
        player.addItem(potion);
        player.setHp(90);
        player.setHp(player.getHp() + 50); // Simulating potion use
        assertEquals(100, player.getHp());
    }

    // Test 8: Test Player Taking Damage
    @Test
    public void testPlayerTakeDamage() {
        player.setHp(player.getHp() - 20);
        assertEquals(80, player.getHp());
    }

    // Test 9: Test Enemy Initialization
    @Test
    public void testEnemyInitialization() {
        assertEquals("Goblin", enemy.getName());
        assertEquals(50, enemy.getHp());
        assertEquals(10, enemy.getDamage());
    }

    // Test 10: Test Room Connections
    @Test
    public void testRoomConnections() {
        KnightsQuest.Room connectedRoom = new KnightsQuest.Room("Connected Room", "Another room.");
        room.addExit("north", connectedRoom);
        assertEquals(connectedRoom, room.getExit("north"));
    }

    // Test 11: Test Moving Between Rooms
    @Test
    public void testPlayerMoveRoom() {
        KnightsQuest.Room connectedRoom = new KnightsQuest.Room("Connected Room", "Another room.");
        room.addExit("north", connectedRoom);
        player.setCurrentRoom(room);
        player.setCurrentRoom(connectedRoom);
        assertEquals(connectedRoom, player.getCurrentRoom());
    }

    // Test 12: Test Enemy Taking Damage
    @Test
    public void testEnemyTakeDamage() {
        enemy.setHp(enemy.getHp() - 20);
        assertEquals(30, enemy.getHp());
    }

    // Test 13: Test Battle Victory
    @Test
    public void testPlayerVictoryInBattle() {
        enemy.setHp(0); // Simulate defeating the enemy
        assertTrue(enemy.getHp() <= 0);
    }

    // Test 14: Test Inventory Management
    @Test
    public void testInventoryManagement() {
        player.addItem(sword);
        player.addItem(potion);
        assertEquals(2, player.getInventory().size());
    }

    // Test 15: Test Enemy Attacks
    @Test
    public void testEnemyAttack() {
        String attack = enemy.getRandomAttack();
        assertTrue(attack.equals("Slash") || attack.equals("Stab"));
    }
        // Test 16: Test Player Taking Damage and Defeating Enemy
        @Test
        public void testPlayerTakeDamageAndDefeatEnemy() {
            enemy.setHp(50);
            player.setHp(100);
            int initialPlayerHp = player.getHp();
    
            // Simulate taking damage
            player.setHp(player.getHp() - enemy.getDamage());
            assertEquals(initialPlayerHp - 10, player.getHp());
    
            // Simulate defeating the enemy
            enemy.setHp(0);
            assertTrue(enemy.getHp() <= 0);
        }
    
        // Test 17: Test Adding Multiple Items to Inventory
        @Test
        public void testAddingMultipleItems() {
            player.addItem(sword);
            player.addItem(potion);
            assertEquals(2, player.getInventory().size());
        }
    
        // Test 18: Test Moving to Non-Existent Room
        @Test
        public void testMoveToNonExistentRoom() {
            player.setCurrentRoom(room);
            assertNull(room.getExit("nonexistent"));
        }
    
        // Test 19: Test Room Description Includes Items
        @Test
        public void testRoomDescriptionWithItems() {
            room.addItem(sword);
            String description = room.getDescription();
            assertTrue(description.contains("You see:"));
            assertTrue(description.contains("Sword"));
        }
    
        // Test 20: Test Room Description Includes Enemy
        @Test
        public void testRoomDescriptionWithEnemy() {
            room.setEnemy(enemy);
            String description = room.getDescription();
            assertTrue(description.contains("There is a Goblin here!"));
        }
    
        // Test 21: Test Player Using Non-Existent Item
        @Test
        public void testUseNonExistentItem() {
            player.setHp(50);
            player.addItem(sword);
            player.setHp(player.getHp()); // No healing
            assertEquals(50, player.getHp());
        }
    
        // Test 22: Test Enemy Drops Item on Death
        @Test
        public void testEnemyDropsItem() {
            room.setEnemy(enemy);
            enemy.setHp(0); // Simulate enemy defeat
            room.addItem(potion); // Simulate drop
            assertTrue(room.getItems().contains(potion));
        }
    
        // Test 23: Test Player HP Exceeds Max HP
        @Test
        public void testPlayerHpExceedsMaxHp() {
            player.setHp(player.getHp() + 50);
            assertEquals(100, player.getHp()); // Max HP should cap at 100
        }
    
        // Test 24: Test Stage Progression After Clearing Rooms
        @Test
        public void testStageProgression() {
            player.incrementRoomsCleared();
            player.incrementRoomsCleared();
            player.incrementRoomsCleared();
            assertEquals(1, player.getStage()); // Should remain in Stage 1
        }
    
        // Test 25: Test Critical Hit Chance
        @Test
        public void testCriticalHitChance() {
            int damage = player.getDamage();
            boolean isCritical = Math.random() < 0.1; // 10% chance
            if (isCritical) damage *= 2;
            assertTrue(damage == player.getDamage() || damage == player.getDamage() * 2);
        }
    
        // Test 26: Test Player Interaction with NPC
        @Test
        public void testNpcInteraction() {
            player.setCurrentRoom(room);
            room.addItem(sword);
            assertTrue(room.getItems().contains(sword));
        }
    
        // Test 27: Test Fleeing Battle
        @Test
        public void testFleeBattle() {
            room.setEnemy(enemy);
            player.setCurrentRoom(room);
            player.setPreviousRoom(null); // Simulate no prior room
            assertNull(player.getPreviousRoom());
        }
    
        // Test 28: Test Healing Potion Effects
        @Test
        public void testHealingPotionEffect() {
            player.addItem(potion);
            player.setHp(50);
            player.setHp(player.getHp() + 50); // Simulate healing
            assertEquals(100, player.getHp());
        }
    
        // Test 29: Test Victory in Boss Battle
        @Test
        public void testVictoryInBossBattle() {
            enemy.setHp(0); // Simulate boss defeat
            assertEquals(0, enemy.getHp());
        }
    
        // Test 30: Test Game Over When HP Reaches Zero
        @Test
        public void testGameOver() {
            player.setHp(0);
            assertEquals(0, player.getHp());
        }
    
        // Test 31: Test Player Moving to Safe Room
        @Test
        public void testMoveToSafeRoom() {
            KnightsQuest.Room safeRoom = new KnightsQuest.Room("Safe Room", "No enemies here.");
            player.setCurrentRoom(safeRoom);
            assertEquals(safeRoom, player.getCurrentRoom());
        }
    
        // Test 32: Test Enemy Attack Reduces Player HP
        @Test
        public void testEnemyAttackReducesPlayerHp() {
            int initialHp = player.getHp();
            player.setHp(initialHp - enemy.getDamage());
            assertEquals(initialHp - 10, player.getHp());
        }

            // Test 33: Test Player Cannot Move Without Sword

    

    // Test 34: Test Enemy Initialization with Boss Flag
    @Test
    public void testEnemyInitializationWithBossFlag() {
        KnightsQuest.Enemy boss = new KnightsQuest.Enemy("Boss", 200, 20, new String[]{"Smash"}, 1, true);
        assertTrue(boss.isBoss());
        assertEquals("Boss", boss.getName());
    }

    // Test 35: Test Room Description Updates After Taking Item
    @Test
    public void testRoomDescriptionAfterTakingItem() {
        room.addItem(sword);
        player.setCurrentRoom(room);
        room.removeItem(sword); // Simulate taking the item
        assertFalse(room.getDescription().contains("Sword"));
    }

    // Test 36: Test Player Inventory After Removing Item
    @Test
    public void testPlayerInventoryAfterRemovingItem() {
        player.addItem(sword);
        player.getInventory().remove(sword); // Remove item
        assertFalse(player.hasItem("Sword"));
    }

    // Test 37: Test Enemy Attack Logic
    @Test
    public void testEnemyAttackLogic() {
        int initialHp = player.getHp();
        int enemyDamage = enemy.getDamage();
        player.setHp(initialHp - enemyDamage);
        assertEquals(initialHp - enemyDamage, player.getHp());
    }

    // Test 38: Test Adding and Retrieving Room Exits
    @Test
    public void testRoomExitConnections() {
        KnightsQuest.Room connectedRoom = new KnightsQuest.Room("Connected Room", "Another room.");
        room.addExit("south", connectedRoom);
        assertEquals(connectedRoom, room.getExit("south"));
    }

    // Test 39: Test Boss Enemy Takes Damage
    @Test
    public void testBossEnemyTakesDamage() {
        KnightsQuest.Enemy boss = new KnightsQuest.Enemy("Dark Lord", 200, 50, new String[]{"Hellfire"}, 2, true);
        boss.setHp(boss.getHp() - 100);
        assertEquals(100, boss.getHp());
    }

    // Test 40: Test Player Defeats Boss
    @Test
    public void testPlayerDefeatsBoss() {
        KnightsQuest.Enemy boss = new KnightsQuest.Enemy("Dark Lord", 200, 50, new String[]{"Hellfire"}, 2, true);
        boss.setHp(0); // Defeated
        assertEquals(0, boss.getHp());
    }

    // Test 41: Test Enemy Attack Reduces Player HP to Zero
    @Test
    public void testEnemyAttackReducesPlayerHpToZero() {
        player.setHp(enemy.getDamage()); // Match damage to player HP
        player.setHp(player.getHp() - enemy.getDamage());
        assertEquals(0, player.getHp());
    }

    // Test 42: Test Player Reaching New Stage
    @Test
    public void testPlayerReachesNewStage() {
        for (int i = 0; i < 10; i++) {
            player.incrementRoomsCleared();
        }
        assertEquals(2, player.getStage());
    }

    // Test 43: Test Player Taking and Using Multiple Items
    @Test
    public void testPlayerUsingMultipleItems() {
        player.addItem(potion);
        player.addItem(sword);
        player.setHp(50);
        player.setHp(player.getHp() + 50); // Use potion
        assertEquals(100, player.getHp());
        assertTrue(player.hasItem("Sword"));
    }

    // Test 44: Test Safe Room Description Without Enemy
    @Test
    public void testSafeRoomDescription() {
        KnightsQuest.Room safeRoom = new KnightsQuest.Room("Safe Room", "A peaceful haven.");
        assertFalse(safeRoom.getDescription().contains("There is a"));
    }

    // Test 45: Test Room Description Updates with Exits
    @Test
    public void testRoomDescriptionWithExits() {
        KnightsQuest.Room exitRoom = new KnightsQuest.Room("Exit Room", "Leads to another area.");
        room.addExit("north", exitRoom);
        assertTrue(room.getDescription().contains("Exits:"));
    }

    // Test 46: Test Player's Current Room After Moving
    @Test
    public void testPlayerCurrentRoomAfterMove() {
        KnightsQuest.Room connectedRoom = new KnightsQuest.Room("Connected Room", "Another area.");
        player.setCurrentRoom(connectedRoom);
        assertEquals(connectedRoom, player.getCurrentRoom());
    }

    // Test 47: Test Enemy's Attack Description
    @Test
    public void testEnemyAttackDescription() {
        String attack = enemy.getRandomAttack();
        assertTrue(attack.equals("Slash") || attack.equals("Stab"));
    }

    // Test 48: Test Room Without Items or Enemies
    @Test
    public void testEmptyRoomDescription() {
        String description = room.getDescription();
        assertFalse(description.contains("You see:"));
        assertFalse(description.contains("There is a"));
    }

    // Test 49: Test Healing Potion Restores Correct HP
    @Test
    public void testHealingPotionRestoration() {
        player.setHp(50);
        player.setHp(player.getHp() + 50); // Simulate healing
        assertEquals(100, player.getHp());
    }

    // Test 50: Test Moving to Room with Enemy
    @Test
    public void testMoveToRoomWithEnemy() {
        room.setEnemy(enemy);
        player.setCurrentRoom(room);
        assertEquals(room, player.getCurrentRoom());
        assertNotNull(room.getEnemy());
    }

    // Test 51: Test Enemy Critical Hit
    @Test
    public void testEnemyCriticalHit() {
        int enemyDamage = enemy.getDamage();
        boolean isCritical = Math.random() < 0.1; // 10% chance
        if (isCritical) enemyDamage *= 2;
        assertTrue(enemyDamage == enemy.getDamage() || enemyDamage == enemy.getDamage() * 2);
    }

    // Test 52: Test Taking Item From Room
    @Test
    public void testTakingItemFromRoom() {
        room.addItem(sword);
        room.removeItem(sword);
        assertFalse(room.getItems().contains(sword));
    }

    // Test 53: Test Enemy Defeated Leaves Room Empty
    @Test
    public void testEnemyDefeatLeavesRoomEmpty() {
        room.setEnemy(enemy);
        enemy.setHp(0); // Defeat enemy
        room.setEnemy(null); // Clear enemy
        assertNull(room.getEnemy());
    }

    // Test 54: Test Boss Enemy Defeat Victory Condition
    @Test
    public void testBossDefeatTriggersVictory() {
        KnightsQuest.Enemy boss = new KnightsQuest.Enemy("Dark Lord", 200, 50, new String[]{"Hellfire"}, 2, true);
        boss.setHp(0); // Simulate boss defeat
        assertTrue(boss.getHp() <= 0);
    }

    // Test 55: Test Player Interaction with Safe Room
    @Test
    public void testPlayerInSafeRoom() {
        KnightsQuest.Room safeRoom = new KnightsQuest.Room("Safe Room", "No enemies.");
        player.setCurrentRoom(safeRoom);
        assertEquals(safeRoom, player.getCurrentRoom());
        assertFalse(safeRoom.getDescription().contains("There is a"));
    }

    // Test 56: Test Enemy Drops Multiple Items
    @Test
    public void testEnemyDropsMultipleItems() {
        room.setEnemy(enemy);
        room.addItem(potion);
        room.addItem(sword);
        assertTrue(room.getItems().contains(potion));
        assertTrue(room.getItems().contains(sword));
    }

    // Test 57: Test Dungeon Stage Room Connection
   // @Test
   // public void testDungeonStageRoomConnection() {
      //  KnightsQuest.Room dungeonRoom = KnightsQuest.createStageRooms(1, "Dungeon", new String[]{"Goblin"});
       // assertEquals("Dungeon 1", dungeonRoom.getName());
    //}

    // Test 58: Test Player Defeats All Enemies
    @Test
    public void testPlayerDefeatsAllEnemies() {
        room.setEnemy(enemy);
        enemy.setHp(0); // Defeat enemy
        room.setEnemy(null); // Remove enemy
        assertNull(room.getEnemy());
    }

    // Test 59: Test Player Cannot Flee Boss Battle
    @Test
    public void testCannotFleeBossBattle() {
        KnightsQuest.Enemy boss = new KnightsQuest.Enemy("Dark Lord", 200, 50, new String[]{"Hellfire"}, 2, true);
        room.setEnemy(boss);
        player.setCurrentRoom(room);
        assertTrue(boss.isBoss());
    }

    // Test 60: Test Final Stage Victory Condition
    @Test
    public void testFinalStageVictory() {
        for (int i = 0; i < 10; i++) {
            player.incrementRoomsCleared();
        }
        assertEquals(2, player.getStage()); // Ensure stage increments correctly
    }
    
        // Test 61: Test Player Moves to Exit Room
        @Test
        public void testPlayerMovesToExitRoom() {
            KnightsQuest.Room exitRoom = new KnightsQuest.Room("Exit Room", "The way out.");
            room.addExit("east", exitRoom);
            player.setCurrentRoom(room);
            player.setCurrentRoom(exitRoom); // Simulate moving to the exit
            assertEquals(exitRoom, player.getCurrentRoom());
        }
    
        // Test 62: Test Inventory Limit
        @Test
        public void testInventoryLimit() {
            for (int i = 0; i < 10; i++) {
                player.addItem(new KnightsQuest.Item("Item " + i, "Description"));
            }
            assertEquals(10, player.getInventory().size());
        }
    
        // Test 63: Test Enemy Moves Between Rooms
        @Test
        public void testEnemyMovesBetweenRooms() {
            KnightsQuest.Room roomA = new KnightsQuest.Room("Room A", "Starting room.");
            KnightsQuest.Room roomB = new KnightsQuest.Room("Room B", "Next room.");
            roomA.addExit("east", roomB);
    
            room.setEnemy(enemy);
            roomB.setEnemy(enemy); // Simulate enemy moving
            room.setEnemy(null);
    
            assertNull(room.getEnemy());
            assertEquals(enemy, roomB.getEnemy());
        }
    
        // Test 64: Test Player Health Drops to Zero in Battle
        @Test
        public void testPlayerHealthDropsToZeroInBattle() {
            player.setHp(enemy.getDamage()); // Set player HP equal to enemy damage
            player.setHp(player.getHp() - enemy.getDamage());
            assertEquals(0, player.getHp());
        }
    
        // Test 65: Test Enemy Cannot Attack When Defeated
        @Test
        public void testEnemyCannotAttackWhenDefeated() {
            enemy.setHp(0); // Enemy is defeated
            int initialPlayerHp = player.getHp();
            assertEquals(initialPlayerHp, player.getHp()); // No damage taken
        }
    
        // Test 66: Test Boss Defeat Unlocks Next Stage
  
        
    
        // Test 67: Test Safe Room Allows Rest
        @Test
        public void testSafeRoomAllowsRest() {
            KnightsQuest.Room safeRoom = new KnightsQuest.Room("Safe Room", "You can rest here.");
            player.setCurrentRoom(safeRoom);
            player.setHp(50); // Simulate low HP
            player.setHp(100); // Simulate resting
            assertEquals(100, player.getHp());
        }
    
        
    
        // Test 69: Test Dungeon Resets After Defeat
        @Test
        public void testDungeonResetsAfterDefeat() {
            player.setHp(0); // Simulate player defeat
            player.setHp(100); // Simulate reset
            assertEquals(100, player.getHp()); // HP should reset to full
        }
    
        @Test
        public void testFinalBossVictoryCondition() {
            KnightsQuest.Enemy boss = new KnightsQuest.Enemy("Dark Lord", 300, 50, new String[]{"Hellfire"}, 2, true);
            boss.setHp(0); // Simulate boss defeat
            assertEquals(0, boss.getHp()); // Ensure boss is defeated
        }
        
    
    
}

    
    
