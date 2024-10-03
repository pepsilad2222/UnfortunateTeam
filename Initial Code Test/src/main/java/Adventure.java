import java.util.*;

class Room {
    String description; // Description of the room
    int healthChange; // Change in health when entering this room
    String enemy; // The enemy in the room 
    String item; // Item in the room 
    int roomNumber; // Room number
    Map<String, Integer> doors; // Doors to other rooms with direction

    public Room(String description, int healthChange, String enemy, String item, int roomNumber, Map<String, Integer> doors) {
        this.description = description;
        this.healthChange = healthChange;
        this.enemy = enemy;
        this.item = item;
        this.roomNumber = roomNumber;
        this.doors = doors;
    }
}

public class Adventure {
    public static int lifeChecker = 100; // Knight's health starts at 100
    public int roomCount = 0; // Tracks rooms completed
    public String knightName; // Name of the knight
    public Set<Integer> visitedRooms; // Set to track visited rooms
    private List<Room> rooms; // List of rooms in the dungeon
    private Scanner scanner; // Scanner for input

    public Adventure(String knightName) {
        this.knightName = knightName;
        lifeChecker = 100;
        visitedRooms = new HashSet<>();
        rooms = createRooms(); // Initialize the rooms
        scanner = new Scanner(System.in); 
    }

    // Method to create the rooms
    private List<Room> createRooms() {
        List<Room> roomList = new ArrayList<>();
        // Define the doors for each room and create Room instances
        Map<String, Integer> doorsRoom1 = new HashMap<>(); // Room 1 doors
        doorsRoom1.put("North", 2);
        roomList.add(new Room("You are at the entrance of the castle. In the corner of the room is a healing potion.", 10, null, "healing potion", 1, doorsRoom1));

        Map<String, Integer> doorsRoom2 = new HashMap<>(); // Room 2 doors
        doorsRoom2.put("South", 1);
        doorsRoom2.put("East", 3);
        doorsRoom2.put("North", 4);
        roomList.add(new Room("A wild beast attacks you as you cross the room.", -15, "beast", null, 2, doorsRoom2));

        Map<String, Integer> doorsRoom3 = new HashMap<>(); // Room 3 doors
        doorsRoom3.put("West", 2);
        doorsRoom3.put("East", 4);
        roomList.add(new Room("The room is filled with treasure. In the corner of the room is a battle elixir.", 5, null, "battle elixir", 3, doorsRoom3));

        Map<String, Integer> doorsRoom4 = new HashMap<>(); // Room 4 doors
        doorsRoom4.put("South", 2);
        roomList.add(new Room("You stumble upon a hidden trap. In this room is a mysterious sword.", -20, null, "mysterious sword", 4, doorsRoom4));

        Map<String, Integer> doorsRoom5 = new HashMap<>(); // Room 5 doors
        doorsRoom5.put("South", 6);
        roomList.add(new Room("You discover a magical herb that heals your wounds. In this room is an elixir.", 20, null, "healing elixir", 5, doorsRoom5));

        Map<String, Integer> doorsRoom6 = new HashMap<>(); // Room 6 doors
        doorsRoom6.put("North", 5);
        doorsRoom6.put("West", 7);
        roomList.add(new Room("A band of goblins ambushes you. You can fight or flee.", -25, "goblin", null, 6, doorsRoom6));

        Map<String, Integer> doorsRoom7 = new HashMap<>(); // Room 7 doors
        doorsRoom7.put("East", 6);
        doorsRoom7.put("South", 8);
        roomList.add(new Room("The room is eerily silent. You can proceed cautiously or explore.", 0, null, null, 7, doorsRoom7));

        Map<String, Integer> doorsRoom8 = new HashMap<>(); // Room 8 doors
        doorsRoom8.put("North", 7);
        doorsRoom8.put("East", 9);
        roomList.add(new Room("You encounter a dragon guarding the exit. You can fight or run.", -30, "dragon", null, 8, doorsRoom8));

        Map<String, Integer> doorsRoom9 = new HashMap<>(); // Room 9 doors
        doorsRoom9.put("West", 8);
        roomList.add(new Room("You find the fabled kingdom elixir! Victory is yours!", 0, null, null, 9, doorsRoom9));

        return roomList;
    }

    
    public void enterRoom() {
        roomCount++;
        visitedRooms.add(roomCount); // Mark this room as visited

        Room currentRoom = rooms.get(roomCount - 1); // Get the current room
        System.out.println("\nRoom " + roomCount + ":\n");
        System.out.println(currentRoom.description); // Describe the room

        // Notify player of the item in the room
        if (currentRoom.item != null) {
            System.out.println("In the room, you see a " + currentRoom.item + ".");
        }

        // Choices based on the room
        if (currentRoom.healthChange != 0) {
            handleRoomEvent(currentRoom);
        }

        System.out.println("Your current health is: " + lifeChecker + " HP.");
        checkGameOver();
    }

    // Handle the events in the room
    private void handleRoomEvent(Room currentRoom) {
        System.out.println();

        if (currentRoom.enemy != null) {
            System.out.println("You encounter a " + currentRoom.enemy + " in this room!");
            System.out.println("What do you want to do? (1: Fight, 2: Flee)");
            int choice = getUserInput();
            if (choice == 1) {
                battle(currentRoom.enemy);
            } else {
                flee(currentRoom.enemy);
            }
        } else {
            // No enemy, provide options based on room description
            if (currentRoom.item != null) {
                System.out.println("You can (1) take the " + currentRoom.item + " or (2) do nothing.");
            }

            if (currentRoom.healthChange > 0) {
                System.out.println("You can (3) drink from the well, or (4) explore the treasure.");
            }

            int actionChoice = getUserInput();
            switch (actionChoice) {
                case 1:
                    if (currentRoom.item.equals("battle elixir")) {
                        
                        System.out.println("You picked up the battle elixir!");
                    } else {
                        heal(20); 
                    }
                    break;
                case 2:
                    System.out.println("You chose to do nothing.");
                    break;
                case 3:
                    heal(currentRoom.healthChange);
                    break;
                case 4:
                    System.out.println("You explore the treasure and find gold!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select again.");
                    handleRoomEvent(currentRoom); // Re-prompt for valid choice
            }
        }

        // Show available doors to other rooms
        System.out.println("Available doors to other rooms:");
        for (Map.Entry<String, Integer> door : currentRoom.doors.entrySet()) {
            String direction = door.getKey();
            int nextRoom = door.getValue();
            System.out.println("- " + direction + " to Room " + nextRoom);
        }

        // Choose a door to continue
        System.out.println("Which direction would you like to go? (Type North, South, East, or West)");
        String directionChoice = scanner.nextLine().trim();
        if (currentRoom.doors.containsKey(directionChoice)) {
            roomCount = currentRoom.doors.get(directionChoice) - 1; // Adjust for room index
            enterRoom();
        } else {
            System.out.println("Invalid direction. You can only go in the directions shown.");
            handleRoomEvent(currentRoom); // Re-prompt for valid choice
        }
    }

    // Method for battle against an enemy
    private void battle(String enemy) {
        System.out.println("Prepare for battle!");
        System.out.println("How much HP do you want to bet for this battle? (1-50)");

        int enemyOdds = new Random().nextInt(20) + 10; // Simulating enemy odds between 10-30
        System.out.println("Enemy odds: " + enemyOdds + " HP");

        int bet = getUserInput(); // Assuming this returns user input
        if (bet < 1 || bet > 50) {
            System.out.println("Invalid bet. You cannot bet this amount.");
            return;
        }

        if (bet <= lifeChecker) {
            // Simulating battle outcome
            if (enemyOdds > bet) {
                System.out.println("The " + enemy + " attacks first and deals damage!");
                lifeChecker -= 15; // Arbitrary damage value
            } else {
                System.out.println("You defeated the " + enemy + "!");
                lifeChecker += 10; // Arbitrary victory healing
            }
        } else {
            System.out.println("You cannot bet more HP than you have!");
        }
    }

    // Method for fleeing from an enemy
    private void flee(String enemy) {
        System.out.println("You fled from the " + enemy + ". You lose 5 HP for running away.");
        lifeChecker -= 5; // Fleeing costs health
    }

    // Method for healing
    private void heal(int amount) {
        lifeChecker += amount;
        System.out.println("You heal for " + amount + " HP.");
    }

    // Method to get user input
    private int getUserInput() {
        return scanner.nextInt();
    }

    // Method to check if the game is over
    private void checkGameOver() {
        if (lifeChecker <= 0) {
            System.out.println("You have perished. Game over.");
            System.exit(0);
        }
    }

    private static void printLore() {
        System.out.println("### Welcome to the Dungeon Quest! ###\n");
        System.out.println("Once a noble knight, you have been honor-bound to this cursed dungeon, ");
        System.out.println("where legends say great treasures and terrible enemies await. ");
        System.out.println("Years ago, you took an oath to protect your kingdom from darkness, ");
        System.out.println("but the dungeon has trapped you in a never-ending quest. ");
        System.out.println("You can never leave until you face the greatest challenge and defeat the ancient evil lurking within.\n");
        System.out.println("Now, gather your courage, for your adventure begins!");
    }

    // Main method to start the adventure
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) { 
            System.out.print("Enter your knight's name: ");
            String knightName = input.nextLine();
            Adventure adventure = new Adventure(knightName);
            printLore();
    
            while (adventure.roomCount < 9) { 
                adventure.enterRoom();
            }
    
            System.out.println("Congratulations, " + knightName + "! You have completed your adventure!");
        } // Scanner will be automatically closed here
    }

    
}