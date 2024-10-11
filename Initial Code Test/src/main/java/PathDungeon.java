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

class Enemy {
    String name;
    int health;
    int attackPower;

    public Enemy(String name, int health, int attackPower) {
        this.name = name;
        this.health = health;
        this.attackPower = attackPower;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            System.out.println(name + " has been defeated!");
        }
    }

    public boolean isAlive() {
        return health > 0;
    }
}

class Room {
    String description;
    int healthChange;
    Enemy enemy;
    Item item;
    int roomNumber;
    int[] doors; 
    boolean visited;
    String roomMaster;
    boolean masterDefeated;

    public Room(String description, int healthChange, Enemy enemy, Item item, int roomNumber, int[] doors, String roomMaster) {
        this.description = description;
        this.healthChange = healthChange;
        this.enemy = enemy; 
        this.item = item;
        this.roomNumber = roomNumber;
        this.doors = doors;
        this.visited = false;
        this.roomMaster = roomMaster;
        this.masterDefeated = false;
    }
}

public class DungeonPath {
    public Room currentRoom; 
    public int lifeChecker = 100;
    public int roomCount = 0;
    public String knightName;
    public Set<Integer> visitedRooms;
    public List<Room> rooms;
    public List<Item> inventory;
    public Set<Weapon> weapons; // Set to avoid duplicate weapons
    public List<Item> healingPotions; // List for healing potions
    public int inventoryWeightLimit = 50; // Maximum weight limit
    public int currentInventoryWeight = 0; // Current weight of the inventory
    private Scanner scanner;
    

    public DungeonPath(String knightName) {
        this.knightName = knightName;
        lifeChecker = 100;
        visitedRooms = new HashSet<>();
        rooms = createRooms();
        weapons = new HashSet<>(); // Initialize the weapons set
        healingPotions = new ArrayList<>(); // Initialize the healing potions list
        scanner = new Scanner(System.in);
    }
    
    private List<Room> createRooms() {
        List<Room> roomList = new ArrayList<>();
        HealingPotion healingPotion = new HealingPotion("Healing Potion", 5, 20);
        Weapon sword = new Weapon("Sword", 10, 15); // Example weapon
    
        roomList.add(new Room("You stumble through the dense forest and find a mysterious castle.", 0, null, null, 1, new int[]{2, 0, 0, 0}, "Forest Guardian"));
        roomList.add(new Room("You are at the entrance of the dungeon. In the corner of the room is a healing potion.", 0, null, healingPotion, 2, new int[]{3, 1, 0, 0}, "Dungeon Keeper"));
        roomList.add(new Room("Deeper in the dungeon, you see a gleaming sword in the corner.", 0, new Enemy("Goblin", 30, 5), sword, 3, new int[]{0, 2, 0, 0}, "Goblin King"));
        roomList.add(new Room("A dark room filled with eerie noises.", 0, new Enemy("Skeleton Warrior", 20, 3), null, 4, new int[]{0, 3, 0, 0}, "Skeleton Warrior"));
        roomList.add(new Room("The air is thick with the stench of decay.", 0, new Enemy("Vampire Lord", 40, 8), null, 5, new int[]{0, 4, 0, 0}, "Vampire Lord"));
        roomList.add(new Room("You see flickering lights in the distance.", 0, new Enemy("Zombie Overlord", 25, 4), null, 6, new int[]{0, 5, 0, 0}, "Zombie Overlord"));
        roomList.add(new Room("The walls are covered in strange symbols.", 0, new Enemy("Lich", 50, 6), null, 7, new int[]{0, 6, 0, 0}, "Lich"));
        roomList.add(new Room("You hear distant growls echoing in the chamber.", 0, new Enemy("Dragon", 80, 10), null, 8, new int[]{0, 7, 0, 0}, "Dragon"));
        roomList.add(new Room("A grand room with a throne at its center.", 0, new Enemy("Dark Sorcerer", 60, 12), null, 9, new int[]{0, 8, 0, 0}, "Dark Sorcerer"));
        roomList.add(new Room("You enter the final room, the lair of the ultimate evil.", 0, new Enemy("Shadow Titan", 100, 15), null, 10, new int[]{0, 9, 0, 0}, "Shadow Titan"));

        return roomList;
    }

    public void startAdventure() {
        System.out.println("Welcome, " + knightName + "!");
        System.out.println("Your adventure begins in the forest...");
        handleForestNavigation();
    }

    private void handleForestNavigation() {
        System.out.println("You are in a dense forest. To find your way out, you must navigate through.");
        String correctPath = "north north east east west west north"; 
        String[] correctSteps = correctPath.split(" ");
        List<String> playerPath = new ArrayList<>(); 

        int progress = 0; 

        while (true) {
            System.out.println("Available directions: 1: North, 2: South, 3: East, 4: West");
            System.out.println("Choose a direction:");
            int direction = getUserInput();

            String directionStr = switch (direction) {
                case 1 -> "north";
                case 2 -> "south";
                case 3 -> "east";
                case 4 -> "west";
                default -> {
                    System.out.println("Invalid choice.");
                    yield "";
                }
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
                    System.out.println("Something feels wrong, you might be lost...");
                }

                if (progress == correctSteps.length) {
                    System.out.println("You have successfully navigated through the forest!");
                    roomCount = 1; 
                    enterRoom();
                    break; 
                }
            }
        }
    }

    public void enterRoom() {
        Room currentRoom = rooms.get(roomCount);
        
        if (visitedRooms.contains(currentRoom.roomNumber)) {
            System.out.println("You have already visited this room.");
        } else {
            visitedRooms.add(currentRoom.roomNumber); 
            System.out.println("\nRoom " + currentRoom.roomNumber + ":\n");
            System.out.println(currentRoom.description);
        
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
        
            if (currentRoom.enemy != null && !currentRoom.masterDefeated) {
                engageRoomMaster(currentRoom);
            } else {
                handleRoomActions(currentRoom);
            }
        }
    }
    
    public void engageRoomMaster(Room currentRoom) {
        System.out.println("You have encountered the " + currentRoom.enemy.name + "!");
        while (currentRoom.enemy.isAlive() && lifeChecker > 0) {
            System.out.println("What would you like to do?");
            System.out.println("1: Attack");
            System.out.println("2: Use Healing Potion");
            System.out.println("3: Flee");

            int choice = getUserInput();

            switch (choice) {
                case 1:
                    int damageDealt = 15; 
                    currentRoom.enemy.takeDamage(damageDealt);
                    System.out.println("You attacked the " + currentRoom.enemy.name + " for " + damageDealt + " damage!");
                    if (!currentRoom.enemy.isAlive()) {
                        currentRoom.masterDefeated = true; 
                        System.out.println("You defeated the " + currentRoom.enemy.name + "!");
                        break; 
                    }
                    lifeChecker -= currentRoom.enemy.attackPower;
                    System.out.println("The " + currentRoom.enemy.name + " attacked you for " + currentRoom.enemy.attackPower + " damage!");
                    break;
                case 2:
                    useHealingPotion();
                    break;
                case 3:
                    System.out.println("You fled from the battle!");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
            if (lifeChecker <= 0) {
                System.out.println("You have died. Game Over.");
                System.exit(0);
            }
            System.out.println("Your current health is: " + lifeChecker + " HP.");
        }
        handleRoomActions(currentRoom);
    }

    public void handleRoomActions(Room currentRoom) {
        if (currentRoom.item != null) {
            pickUpItem(currentRoom);
        }
        roomCount++;
        if (roomCount < rooms.size()) {
            enterRoom();
        } else {
            System.out.println("You have completed your adventure!");
        }
    }
    

    public void pickUpItem(Room currentRoom) {
        if (currentRoom.item instanceof HealingPotion) {
            if (currentInventoryWeight + currentRoom.item.getWeight() <= inventoryWeightLimit) {
                healingPotions.add(currentRoom.item);
                currentInventoryWeight += currentRoom.item.getWeight();
                System.out.println("You have picked up a " + currentRoom.item.getName() + ".");
                currentRoom.item = null; 
            } else {
                System.out.println("You can't carry any more weight!");
            }
        } else if (currentRoom.item instanceof Weapon) {
            Weapon weapon = (Weapon) currentRoom.item;
            if (weapons.size() < 1 && currentInventoryWeight + weapon.getWeight() <= inventoryWeightLimit) {
                weapons.add(weapon);
                currentInventoryWeight += weapon.getWeight();
                System.out.println("You have picked up a " + weapon.getName() + ".");
                currentRoom.item = null; 
            } else {
                System.out.println("You can't carry any more weight or already have a weapon!");
            }
        }
    }
    

    public void useHealingPotion() {
        for (Item item : inventory) {
            if (item instanceof HealingPotion) {
                HealingPotion potion = (HealingPotion) item;
                lifeChecker += potion.getHealAmount();
                System.out.println("You used a " + potion.getName() + " and healed for " + potion.getHealAmount() + " HP!");
                inventory.remove(item);
                currentInventoryWeight -= potion.getWeight();
                return;
            }
        }
        System.out.println("You have no healing potions in your inventory!");
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

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter your knight's name: ");
        String knightName = input.nextLine();
        DungeonPath game = new DungeonPath(knightName);
        game.startAdventure();
        input.close();
        
    }
}
