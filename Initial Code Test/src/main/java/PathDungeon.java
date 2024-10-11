import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


class Enemy {
    String name;
    int health;
    int attackPower;

    public Enemy(String name, int health, int attackPower) {
        this.name = name;
        this.health = health;
        this.attackPower = attackPower;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
    }

    public int getAttackPower() {
        return attackPower;
    }
}


class Item {
    String name;
    int weight;

    public Item(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }
}

class Weapon extends Item {
    int attackPower;

    public Weapon(String name, int weight, int attackPower) {
        super(name, weight);
        this.attackPower = attackPower;
    }

    public int getAttackPower() {
        return attackPower;
    }
}

class HealingPotion extends Item {
    int healAmount;

    public HealingPotion(String name, int weight, int healAmount) {
        super(name, weight);
        this.healAmount = healAmount;
    }

    public int getHealAmount() {
        return healAmount;
    }
}

class Room {
    String description;
    int healthChange;
    Enemy enemy;
    Item item;
    int roomNumber;
    int[] doors; // Array to hold room connections (0 = no connection)
    boolean visited;

    public Room(String description, int healthChange, Enemy enemy, Item item, int roomNumber, int[] doors) {
        this.description = description;
        this.healthChange = healthChange;
        this.enemy = enemy;
        this.item = item;
        this.roomNumber = roomNumber;
        this.doors = doors;
        this.visited = false;
    }
}

public class PathDungeon {
    public static int lifeChecker = 100;
    public int roomCount = 0;
    public String knightName;
    public Set<Integer> visitedRooms;
    private List<Room> rooms;
    private List<Item> inventory;
    private int inventoryWeightLimit = 50;
    private int currentInventoryWeight = 0;
    private Scanner scanner;

    public PathDungeon(String knightName) {
        this.knightName = knightName;
        lifeChecker = 100;
        visitedRooms = new HashSet<>();
        rooms = createRooms();
        inventory = new ArrayList<>();
        scanner = new Scanner(System.in);

        // Start with a sword in the inventory
        Weapon startingSword = new Weapon("Knight's Sword", 10, 15);
        addToInventory(startingSword);
    }

    private List<Room> createRooms() {
        List<Room> roomList = new ArrayList<>();
        HealingPotion healingPotion = new HealingPotion("Healing Potion", 5, 20);

        Enemy orc = new Enemy("Orc", 30, 10); // Enemy with 30 health and 10 attack power


        // Room 1: Entrance to the castle
        roomList.add(new Room("You stumble through the dense forest and find a mysterious castle.", 0, null, null, 1, new int[]{2, 0, 0, 0}));

        // Room 2: Dungeon entrance after forced entry
        roomList.add(new Room("You are at the entrance of the dungeon. In the corner of the room is a healing potion.", 0, null, healingPotion, 2, new int[]{3, 1, 0, 0}));

        // Room 3: Inside the dungeon
        //roomList.add(new Room("Deeper in the dungeon, you see a gleaming sword in the corner.", 0, enemy:null, null, 3, new int[]{0, 2, 0, 0}));
        roomList.add(new Room("You are in the dungeon. In the corner of the room is an orc. He hits you, taking 5 HP away!", -5, orc, null, 3, new int[]{3, 1, 0, 0}));


        return roomList;
    }

    public void startAdventure() {
        System.out.println("Welcome, " + knightName + "!");
        System.out.println("Your adventure begins...");
        enterRoom();
    }

    public void enterRoom() {
        Room currentRoom = rooms.get(roomCount);
        if (!visitedRooms.contains(currentRoom.roomNumber)) {
            visitedRooms.add(currentRoom.roomNumber);
        }

        System.out.println("\nRoom " + currentRoom.roomNumber + ":\n");
        System.out.println(currentRoom.description);

        if (roomCount == 0) {
            // If the player is in the castle entrance, prompt them to enter the dungeon
            promptForDungeonEntry();
        }

        if (currentRoom.item != null) {
            System.out.println("In the room, you see a " + currentRoom.item.getName() + ".");
        }

        if (currentRoom.healthChange != 0) {
            lifeChecker += currentRoom.healthChange;
        }

        System.out.println("Your current health is: " + lifeChecker + " HP.");
        System.out.println("Current inventory weight: " + currentInventoryWeight + "/" + inventoryWeightLimit + " kg.");

        if (lifeChecker <= 0) {
            System.out.println("You have died. Game Over.");
            System.exit(0);
        }

        handleRoomActions(currentRoom);
    }

    private void promptForDungeonEntry() {
        System.out.println("You see a massive dungeon entrance before you. Will you enter? (1: Yes, 2: No)");

        // No matter what they choose, they are forced to enter the dungeon
        getUserInput(); // Removed the 'choice' variable
        System.out.println("You feel a mysterious force pull you into the dungeon...");
        roomCount = 1; // Move to the dungeon entrance (Room 2)
        enterRoom(); // Automatically enter the dungeon
    }

    private void handleRoomActions(Room currentRoom) {
        if (currentRoom.item != null) {
            System.out.println("Do you want to pick up the " + currentRoom.item.getName() + "? (1: Yes, 2: No)");
            int choice = getUserInput();
            if (choice == 1) {
                addToInventory(currentRoom.item);
                currentRoom.item = null;
            }
        }

        System.out.println("Available directions:");
        if (currentRoom.doors[0] != 0) System.out.println("1: North");
        if (currentRoom.doors[1] != 0) System.out.println("2: South");
        if (currentRoom.doors[2] != 0) System.out.println("3: East");
        if (currentRoom.doors[3] != 0) System.out.println("4: West");

        System.out.println("Choose a direction:");
        int direction = getUserInput();
        movePlayer(currentRoom, direction);
    }

    private void movePlayer(Room currentRoom, int direction) {
        int nextRoom = 0;
        switch (direction) {
            /*case 1 -> nextRoom = currentRoom.doors[0]; // North
            case 2 -> nextRoom = currentRoom.doors[1]; // South
            case 3 -> nextRoom = currentRoom.doors[2]; // East
            case 4 -> nextRoom = currentRoom.doors[3]; // West
            default -> System.out.println("Invalid choice."); */
            
                case 1:
                    nextRoom = currentRoom.doors[0]; // North
                    break;
                case 2:
                    nextRoom = currentRoom.doors[1]; // South
                    break;
                case 3:
                    nextRoom = currentRoom.doors[2]; // East
                    break;
                case 4:
                    nextRoom = currentRoom.doors[3]; // West
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            
            
        }

        if (nextRoom != 0) {
            roomCount = nextRoom - 1;
            enterRoom();
        } else {
            System.out.println("You cannot go that way.");
            enterRoom();
        }
    }

    private void addToInventory(Item item) {
        if (currentInventoryWeight + item.getWeight() <= inventoryWeightLimit) {
            inventory.add(item);
            currentInventoryWeight += item.getWeight();
            System.out.println("You picked up the " + item.getName() + ".");
        } else {
            System.out.println("You can't carry any more items.");
        }
    }

    private int getUserInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    public static void main(String[] args) {
        System.out.print("Enter your knight name: ");
        Scanner input = new Scanner(System.in);
        String knightName = input.nextLine();
        PathDungeon adventure = new PathDungeon(knightName);
        adventure.startAdventure();
        input.close();
    }
}
