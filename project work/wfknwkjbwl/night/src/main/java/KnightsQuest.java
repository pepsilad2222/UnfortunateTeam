import java.util.*;
//import java.util.concurrent.*;


public class KnightsQuest {
    private static Scanner scanner = new Scanner(System.in);
    private static Player player;
    private static Map<String, Room> rooms = new HashMap<>();
    private static boolean gameRunning = true;
    private static Timer timer = new Timer();
    private static boolean waitingForBattleInput = false;
    
    public static void main(String[] args) {
        System.out.println("Welcome, brave knight! What is your name?");
        String knightName = scanner.nextLine();
        System.out.println("Welcome to the adventure, " + knightName + "!");
        
        initializeGame();
        displayIntro();
    
        while (gameRunning) {
            System.out.print("> ");
            String input = scanner.nextLine().toLowerCase();
            processCommand(input);
        }
    }
    
    static class Player {
        private int hp;
        private Room previousRoom;
        private Room currentRoom;
        private List<Item> inventory;
        private int maxHp;
        private int stage;
        private int roomsCleared;
        private int damage;
        
        public Player(int hp) {
            this.hp = hp;
            this.previousRoom = null;
            this.maxHp = 100;
            this.inventory = new ArrayList<>();
            this.stage = 1;
            this.roomsCleared = 0;
            this.damage = 20;
        }
        
        public void addItem(Item item) {
            inventory.add(item);
            if (item.getName().equals("Sword")) {
                damage += 10;
                System.out.println("Your damage has increased!");
            }
        }
        
        public boolean hasItem(String itemName) {
            return inventory.stream().anyMatch(item -> item.getName().equalsIgnoreCase(itemName));
        }
        
        public List<Item> getInventory() {
            return inventory;
        }
        
        public int getHp() {
            return hp;
        }
        
        public void setHp(int hp) {
            this.hp = Math.min(hp, maxHp);
        }
        
        public Room getCurrentRoom() {
            return currentRoom;
        }
        
        public void setCurrentRoom(Room room) {
            this.currentRoom = room;
        }
        
        public int getStage() {
            return stage;
        }

        public Room getPreviousRoom() {
            return previousRoom;
        }
    
        public void setPreviousRoom(Room previousRoom) {
            this.previousRoom = previousRoom;
        }
        
        public void incrementRoomsCleared() {
            roomsCleared++;
            if (roomsCleared >= 10) {
                stage++;
                roomsCleared = 0;
                System.out.println("\n=== Stage " + stage + " Reached! ===");
                System.out.println("Your wounds heal slightly as you progress deeper...");
                setHp(Math.min(getHp() + 30, maxHp)); // Heal the player
            }
        }
        
        
        public int getDamage() {
            return damage;
        }
    }
    
    static class Room {
        private String name;
        private String description;
        private Map<String, Room> exits;
        private List<Item> items;
        private Enemy enemy;
        
        public Room(String name, String description) {
            this.name = name;
            this.description = description;
            this.exits = new HashMap<>();
            this.items = new ArrayList<>();
        }
        
        public String getName() {
            return name;
        }
        
        public void addExit(String direction, Room room) {
            exits.put(direction, room);
        }
        
        public Room getExit(String direction) {
            return exits.get(direction);
        }
        
        public void addItem(Item item) {
            items.add(item);
        }
        
        public void removeItem(Item item) {
            items.remove(item);
        }
        
        public List<Item> getItems() {
            return items;
        }
        
        public void setEnemy(Enemy enemy) {
            this.enemy = enemy;
        }
        
        public Enemy getEnemy() {
            return enemy;
        }
        
        public String getDescription() {
            StringBuilder sb = new StringBuilder(description);
            if (!items.isEmpty()) {
                sb.append("\nYou see:");
                for (Item item : items) {
                    sb.append("\n- ").append(item.getName());
                }
            }
            if (enemy != null) {
                sb.append("\nThere is a ").append(enemy.getName()).append(" here!");
            }
            if (!exits.isEmpty()) {
                sb.append("\nExits: ");
                exits.keySet().forEach(exit -> sb.append(exit).append(" "));
            }
            return sb.toString();
        }
    }
    
    static class Item {
        private String name;
        private String description;
        
        public Item(String name, String description) {
            this.name = name;
            this.description = description;
        }
        
        public String getName() {
            return name;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    static class Enemy {
        private String name;
        private int hp;
        private int damage;
        private String[] attacks;
        private int stage;
        private boolean isBoss;
        
        public Enemy(String name, int hp, int damage, String[] attacks, int stage, boolean isBoss) {
            this.name = name;
            this.hp = hp;
            this.damage = damage;
            this.attacks = attacks;
            this.stage = stage;
            this.isBoss = isBoss;
        }
        
        public String getName() {
            return name;
        }
        
        public int getHp() {
            return hp;
        }
        
        public void setHp(int hp) {
            this.hp = hp;
        }
        
        public int getDamage() {
            return damage;
        }
        
        public String getRandomAttack() {
            return attacks[new Random().nextInt(attacks.length)];
        }
        
        public boolean isBoss() {
            return isBoss;
        }
        
        public int getStage() {
            return stage;
        }
    }
    
    private static void initializeGame() {
        player = new Player(50);
    
        // Create the peaceful village
        Room bed = new Room("Your Bed", "You wake up in your bed, recovering from wounds sustained in battle. The village square lies to the south.");
        Room villageSquare = new Room("Village Square", "The bustling heart of the village, filled with merchants and villagers. The blacksmith's shop is to the west, and the village gate leads east.");
        Room blacksmith = new Room("Blacksmith", "The forge burns brightly here. The blacksmith looks at you with concern. Perhaps you should talk to him before venturing out.");
        Room villageGate = new Room("Village Gate", "The gate to the outside world. Beyond it lies the dangerous Forest Path. The guards warn that none should venture forth unarmed.");
    
        // Connect the village rooms
        bed.addExit("south", villageSquare);
        villageSquare.addExit("north", bed);
        villageSquare.addExit("west", blacksmith);
        villageSquare.addExit("east", villageGate);
        blacksmith.addExit("east", villageSquare);
        villageGate.addExit("west", villageSquare);
    
        // Add village rooms to the game map
        rooms.put("bed", bed);
        rooms.put("village square", villageSquare);
        rooms.put("blacksmith", blacksmith);
        rooms.put("village gate", villageGate);
    
        // Set player's starting position
        player.setCurrentRoom(bed);
        
        // Create and initialize the dangerous areas
        Room forestStart = createStageRooms(1, "Forest Path", new String[]{"Goblin", "Wolf", "Dark Elf"});
        rooms.put("forest start", forestStart);
        
        // Distribute items throughout the game world
        distributeItems();
    }
    
    
    private static void distributeItems() {
        if (rooms.isEmpty()) {
            System.out.println("No rooms available to distribute items!");
            return;
        }
    
        Random rand = new Random();
        List<Room> allRooms = new ArrayList<>(rooms.values());
    
        // Distribute healing potions, avoiding the starting village rooms
        for (int i = 0; i < 5; i++) {
            Room randomRoom;
            do {
                randomRoom = allRooms.get(rand.nextInt(allRooms.size()));
            } while (randomRoom.getName().startsWith("Village") || 
                    randomRoom.getName().equals("Your Bed") || 
                    randomRoom.getName().equals("Blacksmith"));
            
            randomRoom.addItem(new Item("Healing Potion", "Restores 50 HP"));
        }
    }
    
    
    
    private static Room createStageRooms(int stage, String area, String[] enemyTypes) {
        Random rand = new Random();
        Room previousRoom = null;
        Room startingRoom = null; // To hold the first room in the stage
    
        for (int i = 0; i < 10; i++) {
            String roomName = area + " " + (i + 1);
            String description = generateRoomDescription(area, i);
            Room room = new Room(roomName, description);
    
            String enemyType = enemyTypes[rand.nextInt(enemyTypes.length)];
            int enemyHp = 50 + (stage * 20);
            int enemyDamage = 10 + (stage * 5);
            String[] attacks = generateEnemyAttacks(enemyType);
            Enemy enemy = new Enemy(enemyType, enemyHp, enemyDamage, attacks, stage, false);
            room.setEnemy(enemy);
    
            if (previousRoom != null) {
                previousRoom.addExit("east", room);
                room.addExit("west", previousRoom);
            }
    
            if (i == 0) {
                startingRoom = room; // Save the first room
            }
    
            previousRoom = room;
            rooms.put(roomName.toLowerCase(), room);
        }
    
        return startingRoom; // Return the first room
    }
    
    
    private static String generateRoomDescription(String area, int roomNumber) {
        List<String> descriptions = new ArrayList<>();
        switch (area) {
            case "Forest Path":
                descriptions.addAll(Arrays.asList(
                    "Twisted trees loom overhead, their branches casting eerie shadows.",
                    "A fog-filled clearing surrounded by ancient trees.",
                    "A narrow path between thorny bushes and dark vegetation."
                ));
                break;
            case "Dark Cave":
                descriptions.addAll(Arrays.asList(
                    "Stalactites hang menacingly from the ceiling.",
                    "The cave walls glitter with mysterious crystals.",
                    "Water drips echo through this damp chamber."
                ));
                break;
            case "Ancient Ruins":
                descriptions.addAll(Arrays.asList(
                    "Crumbling stone pillars line the walls.",
                    "Ancient symbols glow faintly on the walls.",
                    "Broken statues and debris litter the floor."
                ));
                break;
            case "Underground Temple":
                descriptions.addAll(Arrays.asList(
                    "Dark altars emit an eerie purple glow.",
                    "Strange symbols are carved into the floor.",
                    "The air is thick with dark magic."
                ));
                break;
            case "Dark Lord's Lair":
                descriptions.addAll(Arrays.asList(
                    "The walls pulse with evil energy.",
                    "Shadows seem to move of their own accord.",
                    "The ground is stained with dark rituals."
                ));
                break;
        }
        return descriptions.get(roomNumber % descriptions.size());
    }
    
    private static String[] generateEnemyAttacks(String enemyType) {
        Map<String, String[]> attacksByType = new HashMap<>();
        attacksByType.put("Goblin", new String[]{"Slash", "Stab", "Club Strike"});
        attacksByType.put("Wolf", new String[]{"Bite", "Claw", "Howl"});
        attacksByType.put("Dark Elf", new String[]{"Shadow Strike", "Poison Dart", "Dark Magic"});
        attacksByType.put("Cave Troll", new String[]{"Smash", "Rock Throw", "Ground Pound"});
        attacksByType.put("Giant Bat", new String[]{"Screech", "Wing Slash", "Bite"});
        attacksByType.put("Stone Golem", new String[]{"Rock Slam", "Earth Shake", "Boulder Throw"});
        attacksByType.put("Skeleton Warrior", new String[]{"Bone Strike", "Shield Bash", "Ancient Blade"});
        attacksByType.put("Ghost", new String[]{"Spirit Touch", "Haunting Scream", "Possession"});
        attacksByType.put("Cursed Knight", new String[]{"Cursed Blade", "Dark Slash", "Shadow Shield"});
        attacksByType.put("Cultist", new String[]{"Dark Ritual", "Blood Magic", "Shadow Bolt"});
        attacksByType.put("Shadow Beast", new String[]{"Shadow Claw", "Void Bite", "Dark Roar"});
        attacksByType.put("Dark Priest", new String[]{"Dark Blessing", "Soul Drain", "Death Touch"});
        attacksByType.put("Demon Guard", new String[]{"Hellfire Slash", "Demon Roar", "Infernal Strike"});
        attacksByType.put("Chaos Knight", new String[]{"Chaos Blade", "Void Strike", "Dark Charge"});
        attacksByType.put("Soul Reaper", new String[]{"Soul Harvest", "Death Scythe", "Spirit Drain"});
        
        return attacksByType.getOrDefault(enemyType, new String[]{"Attack"});
    }
    
    private static void displayIntro() {
        System.out.println("\n=== Knight's Quest ===");
        System.out.println("\nYou wake up in your bed, still recovering from wounds sustained in your last battle.");
        System.out.println("A messenger bursts into your room: 'Sir Knight! The princess has fallen gravely ill!");
        System.out.println("The cure lies beyond the village, in the depths of the monster-filled forest.'");
        System.out.println("\nYou must prepare for the dangerous journey ahead.");
        System.out.println("First, seek out the blacksmith - you'll need a weapon before venturing forth.");
        System.out.println("\nCommands: go [direction], talk [person], check inventory, take [item], use [item]");
        System.out.println("\n" + player.getCurrentRoom().getDescription());
    }

    private static void goBack() {
        Room previousRoom = player.getPreviousRoom();
        if (previousRoom == null) {
            System.out.println("You can't go back from here!");
            return;
        }
    
        player.setCurrentRoom(previousRoom); // Move to the previous room
        System.out.println("\nYou go back to the previous room.");
        System.out.println("\n" + previousRoom.getDescription());
    }
    
    
    
    private static void processCommand(String input) {
        if (waitingForBattleInput) {
            processBattleCommand(input);
            return;
        }
    
        String[] words = input.split(" ");
        switch (words[0]) {
            case "go":
                if (words.length < 2) {
                    System.out.println("Go where?");
                    return;
                }
                if (words[1].equals("back")) {
                    goBack();
                } else {
                    movePlayer(words[1]);
                }
                break;
            
            case "flee":
                fleeBattle();
                break;
    
            case "check":
                if (words.length < 2) {
                    System.out.println("Check what? (inventory/hp)");
                    return;
                }
                switch (words[1]) {
                    case "inventory":
                        checkInventory();
                        break;
                    case "hp":
                        checkHP();
                        break;
                    default:
                        System.out.println("You can check: inventory, hp");
                }
                break;
    
            case "take":
                if (words.length < 2) {
                    System.out.println("Take what?");
                    return;
                }
                takeItem(words[1]);
                break;
    
            case "use":
                if (words.length < 2) {
                    System.out.println("Use what?");
                    return;
                }
                useItem(words[1]);
                break;
    
            case "talk":
                if (words.length < 2) {
                    System.out.println("Talk to whom?");
                    return;
                }
                if (words[1].equalsIgnoreCase("blacksmith") && player.getCurrentRoom().getName().equals("Blacksmith")) {
                    if (!player.hasItem("Sword")) {
                        System.out.println("\nBlacksmith: 'Ah, brave knight! I heard about your quest to save the princess.");
                        System.out.println("The forest beyond our village is full of dangerous creatures.");
                        System.out.println("Take this sword - you'll need it for the journey ahead.'");
                        System.out.println("\n[You received a Sword!]");
                        player.addItem(new Item("Sword", "A sharp, well-crafted blade"));
                    } else {
                        System.out.println("\nBlacksmith: 'Use that sword well, brave knight. May it serve you in your quest.'");
                    }
                } else {
                    System.out.println("There's no one here by that name to talk to.");
                }
                break;
    
            case "help":
                System.out.println("Available commands:");
                System.out.println("- go [direction]: Move in a direction (north, south, east, west)");
                System.out.println("- go back: Return to the previous room");
                System.out.println("- check inventory: View your items");
                System.out.println("- check hp: Check your health");
                System.out.println("- take [item]: Pick up an item");
                System.out.println("- use [item]: Use an item (like 'use healing potion')");
                System.out.println("- talk [person]: Talk to someone");
                System.out.println("- flee: Try to escape from battle");
                break;
    
            default:
                System.out.println("Unknown command. Type 'help' for available commands.");
        }
    }
    
    
    private static void movePlayer(String direction) {
        Room currentRoom = player.getCurrentRoom();
        Room nextRoom = currentRoom.getExit(direction);
        
        if (nextRoom == null) {
            System.out.println("You cannot go that way.");
            return;
        }
        
        // Check if player is trying to enter the Forest Path
        if (currentRoom.getName().equals("Village Gate") && direction.equals("east")) {
            if (!player.hasItem("Sword")) {
                System.out.println("\nGuard: 'Hold! You cannot leave the village unarmed.");
                System.out.println("Visit the blacksmith first - he'll ensure you're properly equipped.'");
                return;
            }
            // Connect to the forest path only when player has a sword
            nextRoom = rooms.get("forest start");
            if (nextRoom == null) {
                System.out.println("Error: Forest path not found!");
                return;
            }
        }
        
        player.setPreviousRoom(currentRoom);
        player.setCurrentRoom(nextRoom);
        System.out.println("\n" + nextRoom.getDescription());
        
        // Start battle only if in a room with an enemy
        if (nextRoom.getEnemy() != null) {
            startBattle(nextRoom.getEnemy());
        }
    }

     
    private static void startBattle(Enemy enemy) {
        System.out.println("\nBattle started with " + enemy.getName() + "!");
        System.out.println("Enemy HP: " + enemy.getHp());
        waitingForBattleInput = true;
    
        if (enemy.isBoss()) {
            startBossBattleTimer(); // Start the timer for boss battles
        }
    }
    
        
    private static void startBossBattleTimer() {
        TimerTask task = new TimerTask() {
            public void run() {
                if (waitingForBattleInput) {
                    System.out.println("\nYou took too long to act! The boss strikes you down!");
                    player.setHp(player.getHp() - 50); // Boss deals damage
                    System.out.println("Your HP: " + player.getHp());
    
                    // End game if HP <= 0
                    if (player.getHp() <= 0) {
                        gameOver();
                    }
                }
            }
        };
        timer.schedule(task, 10000); // 10 seconds per move
    }
        private static void processBattleCommand(String input) {
            Enemy enemy = player.getCurrentRoom().getEnemy();
            if (enemy == null) return;
            
            if (input.equals("attack")) {
                // Player attacks
                int playerDamage = player.getDamage();
                // 10% chance for critical hit
                if (new Random().nextDouble() < 0.1) {
                    playerDamage *= 2;
                    System.out.println("Critical hit!");
                }
                
                enemy.setHp(enemy.getHp() - playerDamage);
                System.out.println("You deal " + playerDamage + " damage to " + enemy.getName() + "!");
                System.out.println(enemy.getName() + " HP: " + enemy.getHp());
                
                // Check if enemy is defeated
                if (enemy.getHp() <= 0) {
                    System.out.println("You defeated the " + enemy.getName() + "!");
                    if (enemy.isBoss()) {
                        victory();
                        return;
                    }
                    player.getCurrentRoom().setEnemy(null);
                    waitingForBattleInput = false;
                    player.incrementRoomsCleared();
                    
                    // Chance to drop healing potion
                    if (new Random().nextDouble() < 0.3) {
                        Item healingPotion = new Item("Healing Potion", "Restores 50 HP");
                        player.getCurrentRoom().addItem(healingPotion);
                        System.out.println("The enemy dropped a Healing Potion!");
                    }
                    return;
                }
                
                // Enemy attacks back
                String enemyAttack = enemy.getRandomAttack();
                int enemyDamage = enemy.getDamage();
                // 10% chance for enemy critical hit
                if (new Random().nextDouble() < 0.1) {
                    enemyDamage *= 2;
                    System.out.println("Enemy critical hit!");
                }
                
                player.setHp(player.getHp() - enemyDamage);
                System.out.println(enemy.getName() + " uses " + enemyAttack + " and deals " + enemyDamage + " damage!");
                System.out.println("Your HP: " + player.getHp());
                
                // Check if player is defeated
                if (player.getHp() <= 0) {
                    System.out.println("You have been defeated...");
                    gameOver();
                }
            }
        }
        
        private static void fleeBattle() {
            if (!waitingForBattleInput) {
                System.out.println("You're not in battle!");
                return;
            }
            
            Enemy enemy = player.getCurrentRoom().getEnemy();
            Random rand = new Random();
            
            // 30% chance for critical hit when fleeing
            if (rand.nextDouble() < 0.3) {
                int criticalDamage = enemy.getDamage() * 2;
                player.setHp(player.getHp() - criticalDamage);
                System.out.println(enemy.getName() + " lands a critical hit as you flee! You take " + criticalDamage + " damage!");
                
                if (player.getHp() <= 0) {
                    System.out.println("You were struck down while trying to flee...");
                    gameOver();
                    return;
                }
            }
            
            // Only allow fleeing if not fighting a boss
            if (enemy.isBoss()) {
                System.out.println("You cannot flee from this battle!");
                return;
            }
            
            // Move player back one room if possible
            Room currentRoom = player.getCurrentRoom();
            Room previousRoom = currentRoom.getExit("west");
            
            if (previousRoom != null) {
                player.setCurrentRoom(previousRoom);
                waitingForBattleInput = false;
                System.out.println("You flee back to the previous room!");
                System.out.println("\n" + previousRoom.getDescription());
            } else {
                System.out.println("There's nowhere to flee to!");
            }
        }
        
        private static void checkInventory() {
            List<Item> inventory = player.getInventory();
            if (inventory.isEmpty()) {
                System.out.println("Your inventory is empty.");
                return;
            }
            System.out.println("Inventory:");
            for (Item item : inventory) {
                System.out.println("- " + item.getName());
            }
        }
        
        private static void checkHP() {
            System.out.println("Current HP: " + player.getHp());
            if (player.getCurrentRoom().getEnemy() != null) {
                System.out.println("Enemy HP: " + player.getCurrentRoom().getEnemy().getHp());
            }
        }
        
        private static void takeItem(String itemName) {
            List<Item> roomItems = player.getCurrentRoom().getItems();
            for (Item item : roomItems) {
                if (item.getName().equalsIgnoreCase(itemName)) {
                    player.addItem(item);
                    player.getCurrentRoom().removeItem(item);
                    System.out.println("Taken: " + item.getName());
                    return;
                }
            }
            System.out.println("There is no " + itemName + " here.");
        }
        
        private static void useItem(String itemName) {
            if (itemName.equalsIgnoreCase("healing potion")) {
                boolean hasPotion = false;
                Iterator<Item> iterator = player.getInventory().iterator();
                while (iterator.hasNext()) {
                    Item item = iterator.next();
                    if (item.getName().equalsIgnoreCase("healing potion")) {
                        int healAmount = 50;
                        int oldHp = player.getHp();
                        player.setHp(oldHp + healAmount);
                        iterator.remove();
                        System.out.println("You drink the healing potion and restore " + 
                            (player.getHp() - oldHp) + " HP!");
                        hasPotion = true;
                        break;
                    }
                }
                if (!hasPotion) {
                    System.out.println("You don't have any healing potions!");
                }
            } else {
                System.out.println("You can't use that item!");
            }
        }
        
        private static void victory() {
            System.out.println("\n=== Victory! ===");
            System.out.println("You have defeated the Dark Lord and obtained the healing potion!");
            System.out.println("The princess will be saved, and peace will return to the land.");
            gameRunning = false; // Stop the game loop
        }
        
        
        private static void gameOver() {
            System.out.println("\n=== Game Over ===");
            System.out.println("Your quest has ended in failure...");
            gameRunning = false;
            System.exit(0);
        }
    }
                