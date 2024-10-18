import java.util.*;

public class Room {
    public final String description;
    public final int healthChange; // Health impact when entering the room
    public Enemy enemy;
    public Item item;
    public final int roomNumber;
    public boolean masterDefeated;
    public boolean visited; // Track if the room has been visited
    public int visitCount = 0; // Track visits for each room
    public final String roomMaster;
    public final int[] connections; // Connections to other rooms

    // Constructor with connections
    public Room(String description, int healthChange, Enemy enemy, Item item, int roomNumber, int[] connections, String master) {
        this.description = description;
        this.healthChange = healthChange;
        this.enemy = enemy;
        this.item = item;
        this.roomNumber = roomNumber;
        this.masterDefeated = false;
        this.visited = false; // Initialize visited field
        this.visitCount = 0; // Initialize visit count
        this.roomMaster = master;
        this.connections = connections; // Store connections
    }

    public void visitRoom() {
        visitCount++; // Increment visit count each time the room is visited
        visited = true; // Mark room as visited
    }

    public int getVisitCount() {
        return visitCount;
    }

    public String getDescription() {
        return description;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    // Method to apply health change to the player
    public int applyHealthChange(int currentHealth) {
        return currentHealth + healthChange; // Update health based on the room's health change
    }

    // Method to reset master status
    public void resetMasterStatus() {
        masterDefeated = false;
    }

    // Method to check if there is an enemy in the room
    public boolean hasEnemy() {
        return enemy != null;
    }

    // Method to reset the room state if needed
    public void resetRoom() {
        this.visited = false; // Reset visit status
        this.visitCount = 0; // Reset visit count
        resetMasterStatus(); // Reset master status if applicable
        // Other reset logic can go here
    }

    // Static method to create a list of rooms
    public static List<Room> createRooms() {
        List<Room> roomList = new ArrayList<>();

        roomList.add(new Room(
            "You stumble through the dense forest and find a mysterious castle.",
            0, // healthChange (can adjust as needed)
            null, // no enemy in this room
            null, // no item in this room
            1, // room number
            new int[]{2, 0, 0, 0}, // connections to other rooms
            null // master (optional)
        ));

        roomList.add(new Room(
            "You are at the entrance of the dungeon.",
            0, // healthChange
            new Enemy("Goblin", 20, 5), // enemy in this room
            null, // no item in this room
            2, // room number
            new int[]{3, 1, 0, 0}, // connections
            null // master (optional)
        ));

        roomList.add(new Room(
            "Deeper in the dungeon, you feel danger lurking.",
            0, // healthChange
            new Enemy("Orc", 40, 10), // enemy in this room
            new Weapon("Sword", 5, 10), // item in this room
            3, // room number
            new int[]{0, 2, 0, 0}, // connections
            "Master Goblin" // optional master name
        ));

        return roomList;
    }
    
}
