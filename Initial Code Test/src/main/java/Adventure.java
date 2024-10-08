import java.util.*;

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

class Room {
    String description;
    int healthChange;
    String enemy;
    Item item; // Changed to use Item class
    int roomNumber;
    Map<String, Integer> doors;

    public Room(String description, int healthChange, String enemy, Item item, int roomNumber, Map<String, Integer> doors) {
        this.description = description;
        this.healthChange = healthChange;
        this.enemy = enemy;
        this.item = item;
        this.roomNumber = roomNumber;
        this.doors = doors;
    }
}

public class Adventure {
    public static int lifeChecker = 100;
    public int roomCount = 0;
    public String knightName;
    public Set<Integer> visitedRooms;
    private List<Room> rooms;
    private List<Item> inventory; // Inventory to hold items
    private int inventoryWeightLimit = 50; // Example weight limit
    private int currentInventoryWeight = 0; // Current weight of the inventory
    private Scanner scanner;

    public Adventure(String knightName) {
        this.knightName = knightName;
        lifeChecker = 100;
        visitedRooms = new HashSet<>();
        rooms = createRooms();
        inventory = new ArrayList<>(); // Initialize inventory
        scanner = new Scanner(System.in);
    }

    private List<Room> createRooms() {
        List<Room> roomList = new ArrayList<>();
        // Define items
        HealingPotion healingPotion = new HealingPotion("Healing Potion", 5, 20);
        Weapon mysteriousSword = new Weapon("Mysterious Sword", 10, 15);
        Item battleElixir = new HealingPotion("Battle Elixir", 5, 15);
        Item goldenKey = new Item("Golden Key", 1); // Key for a locked door
        Weapon enchantedBow = new Weapon("Enchanted Bow", 12, 20);
        HealingPotion superPotion = new HealingPotion("Super Healing Potion", 7, 50);
        Item magicalAmulet = new Item("Magical Amulet", 3); // Item for quest
        Item ancientScroll = new Item("Ancient Scroll", 2); // Lore item

        // Room 1
        Map<String, Integer> doorsRoom1 = new HashMap<>();
        doorsRoom1.put("North", 2);
        roomList.add(new Room("You are at the entrance of the castle. In the corner of the room is a healing potion.", 10, null, healingPotion, 1, doorsRoom1));

        // Room 2
        Map<String, Integer> doorsRoom2 = new HashMap<>();
        doorsRoom2.put("South", 1);
        doorsRoom2.put("East", 3);
        doorsRoom2.put("North", 4);
        roomList.add(new Room("A wild beast attacks you as you cross the room.", -15, "beast", null, 2, doorsRoom2));

        // Room 3
        Map<String, Integer> doorsRoom3 = new HashMap<>();
        doorsRoom3.put("West", 2);
        doorsRoom3.put("East", 4);
        roomList.add(new Room("The room is filled with treasure. In the corner of the room is a battle elixir.", 5, null, battleElixir, 3, doorsRoom3));

        // Room 4
        Map<String, Integer> doorsRoom4 = new HashMap<>();
        doorsRoom4.put("South", 2);
        doorsRoom4.put("North", 5);
        roomList.add(new Room("You stumble upon a hidden trap. In this room is a mysterious sword.", -20, null, mysteriousSword, 4, doorsRoom4));

        // Room 5
        Map<String, Integer> doorsRoom5 = new HashMap<>();
        doorsRoom5.put("South", 4);
        doorsRoom5.put("East", 6);
        roomList.add(new Room("You found a hidden chamber! A golden key glimmers on the floor.", 0, null, goldenKey, 5, doorsRoom5));

        // Room 6
        Map<String, Integer> doorsRoom6 = new HashMap<>();
        doorsRoom6.put("West", 5);
        doorsRoom6.put("East", 7);
        roomList.add(new Room("This room is dark and cold. You can hear faint whispers.", 0, null, enchantedBow, 6, doorsRoom6));

        // Room 7
        Map<String, Integer> doorsRoom7 = new HashMap<>();
        doorsRoom7.put("West", 6);
        doorsRoom7.put("East", 8);
        doorsRoom7.put("North", 9);
        roomList.add(new Room("You enter a library filled with ancient texts. There is a healing potion on the table.", 0, null, superPotion, 7, doorsRoom7));

        // Room 8
        Map<String, Integer> doorsRoom8 = new HashMap<>();
        doorsRoom8.put("West", 7);
        doorsRoom8.put("North", 10);
        roomList.add(new Room("A powerful enemy lurks here! Beware!", -30, "dark wizard", null, 8, doorsRoom8));

        // Room 9
        Map<String, Integer> doorsRoom9 = new HashMap<>();
        doorsRoom9.put("South", 7);
        doorsRoom9.put("East", 10);
        roomList.add(new Room("You find a magical amulet lying on the altar.", 0, null, magicalAmulet, 9, doorsRoom9));

        // Room 10
        Map<String, Integer> doorsRoom10 = new HashMap<>();
        doorsRoom10.put("South", 8);
        doorsRoom10.put("West", 9);
        roomList.add(new Room("You have reached the throne room. An ancient scroll rests on the throne.", 0, null, ancientScroll, 10, doorsRoom10));

        return roomList;
    }

    public void enterRoom() {
        roomCount++;
        visitedRooms.add(roomCount);

        Room currentRoom = rooms.get(roomCount - 1);
        System.out.println("\nRoom " + roomCount + ":\n");
        System.out.println(currentRoom.description);

        // Notify player of the item in the room
        if (currentRoom.item != null) {
            System.out.println("In the room, you see a " + currentRoom.item.getName() + " (Weight: " + currentRoom.item.getWeight() + ").");
        }

        // Choices based on the room
        if (currentRoom.healthChange != 0) {
            handleRoomEvent(currentRoom);
        }

        System.out.println("Your current health is: " + lifeChecker + " HP.");
        System.out.println("Current inventory weight: " + currentInventoryWeight + "/" + inventoryWeightLimit + " kg.");
        checkGameOver();
    }

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
                System.out.println("You can (1) take the " + currentRoom.item.getName() + " or (2) do nothing.");
            }

            if (currentRoom.healthChange > 0) {
                System.out.println("You can (3) drink from the well, or (4) explore the treasure.");
            }

            int actionChoice = getUserInput();
            switch (actionChoice) {
                case 1:
                    addToInventory(currentRoom.item);
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
                    handleRoomEvent(currentRoom);
            }
        }

        System.out.println("Available doors to other rooms:");
        for (Map.Entry<String, Integer> door : currentRoom.doors.entrySet()) {
            String direction = door.getKey();
            int nextRoom = door.getValue();
            System.out.println("Go " + direction + " to Room " + nextRoom);
        }

        System.out.println("Which direction would you like to go? (Enter direction)");
        String direction = scanner.nextLine().trim();
        if (currentRoom.doors.containsKey(direction)) {
            roomCount = currentRoom.doors.get(direction);
            enterRoom();
        } else {
            System.out.println("You cannot go that way.");
            enterRoom(); // Re-enter the room to try again
        }
    }

    private void battle(String enemy) {
        System.out.println("You fight the " + enemy + "!");
        lifeChecker += 20; // Simplified for the demo
        System.out.println("You gain 20 HP from the battle!");
    }

    private void flee(String enemy) {
        System.out.println("You successfully fled from the " + enemy + "!");
    }

    private void addToInventory(Item item) {
        if (item != null) {
            if (currentInventoryWeight + item.getWeight() <= inventoryWeightLimit) {
                inventory.add(item);
                currentInventoryWeight += item.getWeight();
                System.out.println("You have taken the " + item.getName() + "!");
            } else {
                System.out.println("You cannot carry that item. Exceeds weight limit!");
            }
        }
    }

    private void heal(int amount) {
        lifeChecker += amount;
        System.out.println("You healed " + amount + " HP!");
    }

    private void checkGameOver() {
        if (lifeChecker <= 0) {
            System.out.println("You have died. Game Over.");
            System.exit(0);
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
        Adventure adventure = new Adventure(knightName);
        adventure.enterRoom();
        input.close();
    }
}
