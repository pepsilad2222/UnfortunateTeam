import java.util.*;

public class DungeonPath {
    public Room currentRoom;
    public final String knightName;
    public final Set<Integer> visitedRooms;
    public final Inventory inventory;
    public final List<Room> rooms;
    public final List<Weapon> weapons;
    public final List<HealingPotion> healingPotions;
    public int inventoryWeightLimit = 50;
    public int currentInventoryWeight = 0;
    public int lifeChecker = 100;
    public final Scanner scanner;

    public DungeonPath(String knightName) {
        this.knightName = knightName;
        this.visitedRooms = new HashSet<>();
        this.rooms = createRooms();
        this.weapons = new ArrayList<>();
        this.healingPotions = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.inventory = new Inventory(inventoryWeightLimit);
    }

    private List<Room> createRooms() {
        List<Room> roomList = new ArrayList<>();
        roomList.add(new Room("You stumble through the dense forest and find a mysterious castle.", 0, null, null, 1, new int[]{2, 0, 0, 0}, null));
        roomList.add(new Room("You are at the entrance of the dungeon.", 0, new Enemy("Goblin", 20, 5), null, 2, new int[]{3, 1, 0, 0}, null));
        roomList.add(new Room("Deeper in the dungeon, you feel danger lurking.", 0, new Enemy("Orc", 40, 10), new Weapon("Sword", 5, 10), 3, new int[]{0, 2, 0, 0}, "Master Goblin"));
        return roomList;
    }
    

    public void startAdventure() {
        System.out.println("Welcome, " + knightName + "!");
        System.out.println("Your adventure begins in the forest...");
        handleForestNavigation();
    }

    private void handleForestNavigation() {
        System.out.println("You are in a dense forest. Navigate to find your way out.");
        String correctPath = "north north east east west west north";
        String[] correctSteps = correctPath.split(" ");
        List<String> playerPath = new ArrayList<>();
        int progress = 0;

        while (true) {
            System.out.println("Choose a direction: 1: North, 2: South, 3: East, 4: West");
            int direction = getUserInput();

            String directionStr = switch (direction) {
                case 1 -> "north";
                case 2 -> "south";
                case 3 -> "east";
                case 4 -> "west";
                default -> { System.out.println("Invalid choice."); yield ""; }
            };

            if (!directionStr.isEmpty()) {
                if (playerPath.contains(directionStr)) {
                    System.out.println("You remember that you've already visited here before.");
                }

                if (directionStr.equals(correctSteps[progress])) {
                    System.out.println("You feel like you're heading in the right direction.");
                    playerPath.add(directionStr);
                    progress++;
                } else {
                    System.out.println("Something feels wrong...");
                }

                if (progress == correctSteps.length) {
                    System.out.println("You successfully navigated the forest!");
                    enterRoom(1);  // Start at Room 1
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
            currentRoom.visited = true; // Mark the room as visited
            System.out.println("\nRoom " + currentRoom.getRoomNumber() + ":");
            System.out.println(currentRoom.getDescription());
    
            // If there's an item, add it to the inventory
            if (currentRoom.item != null) {
                if (inventory.addToInventory(currentRoom.item)) {
                    System.out.println("You have acquired: " + currentRoom.item.getName());
                } else {
                    System.out.println("Could not acquire the item. Inventory is full.");
                }
            }
    
            System.out.println("Choose your next direction:");
        }
    }

    private int getUserInput() {
        while (true) {
            System.out.print("Enter your choice: ");
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input);
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
        visitedRooms.clear();
        lifeChecker = 100;
        currentInventoryWeight = 0;
        weapons.clear();
        healingPotions.clear();
        for (Room room : rooms) {
            room.resetRoom(); // Ensure this resets both visited and master status
        }
    }
    
    public boolean addToInventory(Item item) {
        return inventory.addToInventory(item);  // Delegate to Inventory
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
