import java.util.*;

public class DungeonPath {
    public final List<Weapon> weapons = new ArrayList<>();
    public final List<HealingPotion> healingPotions = new ArrayList<>();
    public Room currentRoom;
    public final String knightName;
    public final Set<Integer> visitedRooms;
    public final Inventory inventory;
    public final List<Room> rooms;
    public int lifeChecker = 100;
    public final Scanner scanner;
    public static final int ROOMS_PER_FLOOR = 10;
    public int currentFloor = 1;
    public int inventoryWeightLimit = 50;
    public int currentInventoryWeight = 0;

    public DungeonPath(String knightName) {
        this.knightName = knightName;
        this.visitedRooms = new HashSet<>();
        this.inventory = new Inventory(50); // Set default weight limit to 50
        this.rooms = createRooms();
        this.scanner = new Scanner(System.in);
    }

    private List<Room> createRooms() {
        List<Room> roomList = new ArrayList<>();
        
        // First floor rooms (10 rooms in linear progression)
        roomList.add(new Room("Floor 1, Room 1: The Entrance Hall - A grand chamber with ancient pillars.", 
            0, new Enemy("Guard Goblin", 20, 5), 
            new HealingPotion("Small Health Potion", 1, 20), // Added a healing potion to first room
            1, new int[]{2, 0, 0, 0}, null));
            
        roomList.add(new Room("Floor 1, Room 2: The Armory - Weapon racks line the walls.", 
            0, new Enemy("Skeleton Warrior", 25, 7), 
            new Weapon("Rusty Dagger", 2, 8), 
            2, new int[]{3, 1, 0, 0}, null));
            
        roomList.add(new Room("Floor 1, Room 3: The Training Ground - Practice dummies stand silently.", 
            0, new Enemy("Training Golem", 30, 8), 
            null, 3, new int[]{4, 2, 0, 0}, null));
            
        roomList.add(new Room("Floor 1, Room 4: The Barracks - Empty beds and scattered equipment.", 
            0, new Enemy("Undead Soldier", 35, 9), 
            new HealingPotion("Medium Health Potion", 2, 40), 
            4, new int[]{5, 3, 0, 0}, null));
            
        roomList.add(new Room("Floor 1, Room 5: The Mess Hall - Long tables covered in cobwebs.", 
            0, new Enemy("Hungry Troll", 40, 10), 
            null, 5, new int[]{6, 4, 0, 0}, null));
            
        roomList.add(new Room("Floor 1, Room 6: The Kitchen - Rusty utensils and cold hearths.", 
            0, new Enemy("Chef Goblin", 45, 11), 
            new Weapon("Kitchen Knife", 1, 12), 
            6, new int[]{7, 5, 0, 0}, null));
            
        roomList.add(new Room("Floor 1, Room 7: The Pantry - Rotting barrels and crates.", 
            0, new Enemy("Giant Rat", 50, 12), 
            new HealingPotion("Large Health Potion", 3, 60), 
            7, new int[]{8, 6, 0, 0}, null));
            
        roomList.add(new Room("Floor 1, Room 8: The Wine Cellar - Broken bottles everywhere.", 
            0, new Enemy("Drunken Orc", 55, 13), 
            null, 8, new int[]{9, 7, 0, 0}, null));
            
        roomList.add(new Room("Floor 1, Room 9: The Treasury - Empty chests and scattered coins.", 
            0, new Enemy("Treasure Guardian", 60, 14), 
            new Weapon("Golden Sword", 5, 25), 
            9, new int[]{10, 8, 0, 0}, null));
            
        roomList.add(new Room("Floor 1, Room 10: The Boss Chamber - A throne sits at the far end.", 
            0, new Enemy("Floor Guardian", 100, 20), 
            null, 10, new int[]{0, 9, 0, 0}, "Floor Master"));
        
        return roomList;
    }
    
    public void startAdventure() {
        System.out.println("Welcome, brave " + knightName + "!");
        System.out.println("Your adventure begins in the forest maze...");
        handleForestNavigation();
    }

    private void handleForestNavigation() {
        System.out.println("You are in a dense forest maze. Navigate to find your way out.");
        String correctPath = "north north east east west west north";
        String[] correctSteps = correctPath.split(" ");
        List<String> playerPath = new ArrayList<>();
        int progress = 0;

        while (true) {
            System.out.println("\nChoose a direction:");
            System.out.println("1: North");
            System.out.println("2: South");
            System.out.println("3: East");
            System.out.println("4: West");
            System.out.println("5: Check inventory");
            
            int choice = getUserInput();
            
            if (choice == 5) {
                inventory.showInventory();
                continue;
            }

            String directionStr = switch (choice) {
                case 1 -> "north";
                case 2 -> "south";
                case 3 -> "east";
                case 4 -> "west";
                default -> { System.out.println("Invalid choice."); yield ""; }
            };

            if (!directionStr.isEmpty()) {
                if (directionStr.equals(correctSteps[progress])) {
                    System.out.println("You feel like you're heading in the right direction.");
                    playerPath.add(directionStr);
                    progress++;
                } else {
                    System.out.println("Something feels wrong... Maybe try another direction.");
                }

                if (progress == correctSteps.length) {
                    System.out.println("\nCongratulations! You've successfully navigated the forest maze!");
                    System.out.println("You find a magnificent sword lying against an ancient tree!");
                    
                    // Create the Forest Sword with appropriate weight and attack power
                    Weapon forestSword = new Weapon("Forest Sword", 4, 15);
                    if (inventory.addToInventory(forestSword)) {
                        System.out.println("The Forest Sword has been added to your inventory!");
                        System.out.println("Stats: Weight: 4, Attack Power: 15");
                    }
                    
                    // Ask if player wants to enter the dungeon
                    System.out.println("\nA massive dungeon looms before you.");
                    System.out.println("Would you like to enter?");
                    System.out.println("1: Yes - Enter the dungeon");
                    System.out.println("2: No - End the adventure");
                    
                    choice = getUserInput();
                    if (choice == 1) {
                        System.out.println("\nYou step into the dungeon...");
                        enterRoom(1);  // Start at first room
                    } else {
                        System.out.println("You decide to return another day. Farewell, brave adventurer!");
                        System.exit(0);
                    }
                    break;
                }
            }
        }
    }

    public void enterRoom(int roomNumber) {
        currentRoom = rooms.get(roomNumber - 1);
        if (visitedRooms.contains(currentRoom.getRoomNumber())) {
            System.out.println("You have already visited this room.");
        } else {
            visitedRooms.add(currentRoom.getRoomNumber());
            currentRoom.visited = true;
            
            System.out.println("\n" + currentRoom.getDescription());
    
            // Handle enemy encounter
            if (currentRoom.enemy != null) {
                System.out.println("\nA " + currentRoom.enemy.name + " appears!");
                System.out.println("Enemy Stats - Health: " + currentRoom.enemy.health + 
                                 ", Attack Power: " + currentRoom.enemy.attackPower);
            }
    
            // Handle items in room
            if (currentRoom.item != null) {
                System.out.println("\nYou see a " + currentRoom.item.getName() + " in the room.");
                System.out.println("Would you like to pick it up? (1: Yes, 2: No)");
                int choice = getUserInput();
                if (choice == 1) {
                    if (inventory.addToInventory(currentRoom.item)) {
                        System.out.println("Added " + currentRoom.item.getName() + " to your inventory.");
                        currentRoom.item = null; // Remove item from room after picking up
                    }
                }
            }
    
            // Show available actions
            while (true) {
                System.out.println("\nWhat would you like to do?");
                System.out.println("1: Move to next room");
                System.out.println("2: View inventory");
                System.out.println("3: Check status");
                System.out.println("4: Use healing potion");
                
                int choice = getUserInput();
                switch (choice) {
                    case 1:
                        if (roomNumber < ROOMS_PER_FLOOR) {
                            enterRoom(roomNumber + 1);
                            return;
                        } else {
                            System.out.println("You've reached the end of this floor!");
                        }
                        break;
                    case 2:
                        inventory.showInventory();
                        break;
                    case 3:
                        System.out.println("\nStatus:");
                        System.out.println("Health: " + lifeChecker + "/100");
                        System.out.println("Current Floor: " + currentFloor);
                        System.out.println("Current Room: " + roomNumber);
                        break;
                    case 4:
                        List<HealingPotion> potions = inventory.getHealingPotions();
                        if (potions.isEmpty()) {
                            System.out.println("You don't have any healing potions!");
                        } else {
                            // Implement potion usage logic here
                        }
                        break;
                }
            }
        }
    }

    private int getUserInput() {
        while (true) {
            System.out.print("Enter your choice: ");
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }


    public void addVisitedRoom(int roomNumber) {
        visitedRooms.add(roomNumber);
    }

    public boolean hasVisitedRoom(int roomNumber) {
        return visitedRooms.contains(roomNumber);
    }

    public void resetGame() {
        this.lifeChecker = 100; // Reset player health
        inventory.resetInventory(); // Reset inventory
        this.weapons.clear(); // Clear weapon inventory
        this.healingPotions.clear(); // Clear healing potions
        for (Room room : rooms) {
            room.masterDefeated = false; // Reset all room master statuses
            room.visited = false; // Reset visited status if needed
        }
        // Other reset logic as necessary
    }
    
    
    public boolean addToInventory(Item item) {
        return inventory.addToInventory(item);  // Delegate to Inventory
    }

    public void removeItem(Item item) {
        inventory.removeItem(item); // Call the removeItem method from Inventory
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getInventoryWeight() {
        return inventory.getInventoryWeight();  // Delegate to Inventory
    }

    public boolean canMoveToNextRoom(Room room) {
        return room.masterDefeated; // Allow movement only if the master is defeated
    }
}
