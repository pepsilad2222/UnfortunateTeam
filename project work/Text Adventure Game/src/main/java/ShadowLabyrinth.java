/**
 * ShadowLabyrinth - A text-based roguelike adventure game
 * 
 * The game follows a knight's journey through a mysterious labyrinth
 * to rescue their love, Elara, from the clutches of a Shadow Wraith.
 * 
 * Game Features:
 * - Progressive difficulty through 50 rooms
 * - Combat system with weapons, armor, and potions
 * - Shop system with tiered equipment
 * - Math challenges every 10 rooms
 * - Final boss battle with timing mechanics
 * - Random events affecting player HP
 * 
 * @author [Michael Aghassi, Nathan Heitmann]
 * @version 1.0
 */


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class ShadowLabyrinth {
    static int playerHP = 100;
    static int coins = 15;
    static int currentRoom = 1;
    static boolean hasRescuedElara = false;
    static Random random = new Random();
    static Scanner scanner = new Scanner(System.in);

    // Player stats
    static int weaponDamage = 5;
    static double armorReduction = 0.0;

    // Player inventory
    static List<String> weapons = new ArrayList<>();
    static List<String> armors = new ArrayList<>();
    static List<String> potions = new ArrayList<>();
    static String equippedWeapon = "Basic Sword";
    static String equippedArmor = "Cloth Armor";
    static double equippedArmorReduction = 0.0;

    // Track purchased items
    static Set<String> purchasedItems = new HashSet<>();

    public static void main(String[] args) {
    setRandomSeed(12345L);
    startGame(args);
    }

    public static void setRandomSeed(long seed) {
        random = new Random(seed);
    }
    
    

    public static void startGame(String[] args) {
        // Set the seed silently without user input
        setRandomSeed(12345L);
    
    
        // Normal game start
        typeTextWithCursor("WAKE UP SOLDIER, YOU WERE HURT IN OUR LAST BATTLE",50);
        typeTextWithCursor("GET UP COME ON WE NEED TO KEEP GOING",50);
        typeTextWithCursor("WHAT ARE YOU DOING???? I SAID GET UP....",50);
        typeTextWithCursor("You finally awake after all that shouting",50);
        typeTextWithCursor("Much to your suprise you look around to find nothing",50);
        typeTextWithCursor("You are surrounded by black expect a small room heading forward...",50);
        typeTextWithCursor("As curiosity entices you you step forward....",50);
        typeTextWithCursor("As you step in you remeber your love getting captured...",50);
        typeTextWithCursor("You run and run and run until you find an enemy",50);
        typeTextWithCursor("With useless destroyed armor and a damaged sword you look around..",50);
        typeTextWithCursor("You then charge forth, attempting to save your love",50);
        
        typeTextWithCursor("=== Game Information ===",50);
        typeTextWithCursor("New items become available every 10 rooms",50);
        typeTextWithCursor("All previously discovered items remain available in shops",50);
        typeTextWithCursor("You start with " + ShadowLabyrinth.coins + " coins",50);
        typeTextWithCursor("Your initial HP is " + ShadowLabyrinth.playerHP,50);
        typeTextWithCursor("========================",50);
    
        typeTextWithCursor("Get ready.... No one said it would ever be easy....",50);
        System.out.println();
    
        while (playerHP > 0 && !hasRescuedElara) {
            displayStats();
            
            if (currentRoom % 10 == 0) {
                mathChallenge();
            }
    
            if (currentRoom == 50) {
                bossFightWithTimer();
            } else if (currentRoom % 2 == 0) {
                enterShop();
            } else {
                combatRoom();
            }
    
            randomHPChange();
    
            if (playerHP <= 0) {
                typeTextWithCursor("You have been defeated. Game Over.", 50);
                break;
            }
        }
    
        if (hasRescuedElara) {
            typeTextWithCursor("Congratulations! You have rescued Elara and completed your quest!", 50);
        }
    }
    static void displayStats() {
        System.out.println("\n=== Current Status ===");
        typeTextWithCursor("Room: " + currentRoom, 25);
        typeTextWithCursor("HP: " + playerHP, 25);
        typeTextWithCursor("Coins: " + coins, 25);
        typeTextWithCursor("Weapon: " + equippedWeapon + " (Damage: " + weaponDamage + ")", 25);
        typeTextWithCursor("Armor: " + equippedArmor + " (Reduction: " + (equippedArmorReduction * 100) + "%)", 25);
        System.out.println("===================");
    }

    public static void typeTextWithCursor(String text, int delayMillis) {
        int adjustedDelay = (int) (delayMillis * 0.75);
        for (char c : text.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(adjustedDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println();
    }

    static void displayInventory() {
        System.out.println("\n=== Inventory ===");
        
        // Display weapons with damage values
        System.out.println("Weapons:");
        if (weapons.isEmpty()) {
            System.out.println("None");
        } else {
            for (String weapon : weapons) {
                int dmg = getWeaponDamage(weapon);
                System.out.println("- " + weapon + " (Damage: " + dmg + ")");
            }
        }
        
        // Display armor with reduction values
        System.out.println("\nArmor:");
        if (armors.isEmpty()) {
            System.out.println("None");
        } else {
            for (String armor : armors) {
                double reduction = getArmorReduction(armor);
                System.out.println("- " + armor + " (Reduction: " + (reduction * 100) + "%)");
            }
        }
        
        System.out.println("\nPotions: " + (potions.isEmpty() ? "None" : String.join(", ", potions)));
        
        System.out.println("\n=== Currently Equipped ===");
        System.out.println("Weapon: " + equippedWeapon + " (Damage: " + weaponDamage + ")");
        System.out.println("Armor: " + equippedArmor + " (Reduction: " + (equippedArmorReduction * 100) + "%)");
        
        // Add equipment management options
        System.out.println("\n=== Equipment Management ===");
        System.out.println("1. Change Weapon");
        System.out.println("2. Change Armor");
        System.out.println("3. Back");
        
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                changeWeapon();
                break;
            case "2":
                changeArmor();
                break;
            case "3":
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }
    static void changeWeapon() {
        if (weapons.isEmpty()) {
            System.out.println("No weapons available to equip!");
            return;
        }
    
        System.out.println("\nSelect a weapon to equip:");
        for (int i = 0; i < weapons.size(); i++) {
            String weapon = weapons.get(i);
            int dmg = getWeaponDamage(weapon);
            System.out.println((i + 1) + ". " + weapon + " (Damage: " + dmg + ")");
        }
        System.out.println((weapons.size() + 1) + ". Cancel");
    
        try {
            int choice = Integer.parseInt(scanner.nextLine()) - 1;
            if (choice >= 0 && choice < weapons.size()) {
                String newWeapon = weapons.get(choice);
                equippedWeapon = newWeapon;
                weaponDamage = getWeaponDamage(newWeapon);
                typeTextWithCursor("Equipped " + newWeapon + "!", 50);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice!");
        }
    }
    
    static void changeArmor() {
        if (armors.isEmpty()) {
            System.out.println("No armor available to equip!");
            return;
        }
    
        System.out.println("\nSelect armor to equip:");
        for (int i = 0; i < armors.size(); i++) {
            String armor = armors.get(i);
            double reduction = getArmorReduction(armor);
            System.out.println((i + 1) + ". " + armor + " (Reduction: " + (reduction * 100) + "%)");
        }
        System.out.println((armors.size() + 1) + ". Cancel");
    
        try {
            int choice = Integer.parseInt(scanner.nextLine()) - 1;
            if (choice >= 0 && choice < armors.size()) {
                String newArmor = armors.get(choice);
                equippedArmor = newArmor;
                armorReduction = getArmorReduction(newArmor);
                equippedArmorReduction = armorReduction;
                typeTextWithCursor("Equipped " + newArmor + "!", 50);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice!");
        }
    }
    
    static int getWeaponDamage(String weaponName) {
        String[][] availableWeapons = getAvailableWeapons();
        for (String[] weapon : availableWeapons) {
            if (weapon[0].equals(weaponName)) {
                return Integer.parseInt(weapon[1]);
            }
        }
        return 5; // Default damage for basic sword
    }
    
    static double getArmorReduction(String armorName) {
        String[][] availableArmor = getAvailableArmor();
        for (String[] armor : availableArmor) {
            if (armor[0].equals(armorName)) {
                return Double.parseDouble(armor[1]) / 100.0;
            }
        }
        return 0.0; // Default reduction for basic armor
    }

    static void randomHPChange() {
        if (random.nextInt(100) < 30) {
            if (random.nextBoolean()) {
                playerHP += 10;
                typeTextWithCursor("You find a healing crystal! Gained 10 HP.", 50);
            } else {
                playerHP -= 10;
                typeTextWithCursor("You trigger a trap! Lost 10 HP.", 50);
            }
            typeTextWithCursor("Current HP: " + playerHP, 50);
        }
    }

    static void mathChallenge() {
        typeTextWithCursor("\nA magical barrier blocks your path!", 50);
        typeTextWithCursor("Solve this riddle to proceed:", 50);
        
        int a = random.nextInt(10) + 1;
        int b = random.nextInt(10) + 1;
        int operation = random.nextInt(3);
        int correctAnswer;
        String operationSymbol;
        
        switch (operation) {
            case 0:
                correctAnswer = a + b;
                operationSymbol = "+";
                break;
            case 1:
                correctAnswer = a - b;
                operationSymbol = "-";
                break;
            default:
                correctAnswer = a * b;
                operationSymbol = "*";
        }

        typeTextWithCursor(a + " " + operationSymbol + " " + b + " = ?", 50);
        
        try {
            int answer = Integer.parseInt(scanner.nextLine());
            if (answer == correctAnswer) {
                typeTextWithCursor("Correct! The barrier dissipates.", 50);
                playerHP += 20;
                typeTextWithCursor("You feel energized! Gained 20 HP.", 50);
            } else {
                typeTextWithCursor("Wrong! The barrier zaps you!", 50);
                playerHP /= 2;
                typeTextWithCursor("You lose half your HP!", 50);
            }
        } catch (NumberFormatException e) {
            typeTextWithCursor("Invalid input! The barrier zaps you!", 50);
            playerHP /= 2;
            typeTextWithCursor("You lose half your HP!", 50);
        }
    }

    static void combatRoom() {
        typeTextWithCursor("\nEnemies appear in the shadows!", 50);
        
        int numEnemies = random.nextInt(3) + 1;
        int maxHP = getEnemyMaxHP();
        boolean fled = false;
        
        typeTextWithCursor("You face " + numEnemies + " enemies!", 50);
        
        for (int i = 1; i <= numEnemies && playerHP > 0 && !fled; i++) {
            int enemyHP = random.nextInt(maxHP) + (maxHP / 2);
            typeTextWithCursor("\nEnemy " + i + " appears! (HP: " + enemyHP + ")", 50);
            
            while (enemyHP > 0 && playerHP > 0 && !fled) {
                System.out.println("\nYour HP: " + playerHP + " | Enemy HP: " + enemyHP);
                System.out.println("1. Attack");
                System.out.println("2. Use Potion");
                System.out.println("3. View Inventory");
                if (currentRoom > 1) {
                    System.out.println("4. Attempt to Flee");
                }
                
                String choice = scanner.nextLine();
                
                switch (choice) {
                    case "1":
                        int damage = calculatePlayerDamage();
                        enemyHP -= damage;
                        typeTextWithCursor("You deal " + damage + " damage!", 50);
                        
                        if (enemyHP > 0) {
                            int enemyDamage = calculateEnemyDamage();
                            playerHP -= enemyDamage;
                            typeTextWithCursor("Enemy strikes back for " + enemyDamage + " damage!", 50);
                        }
                        break;
                        
                    case "2":
                        usePotion();
                        break;
                        
                    case "3":
                        displayInventory();
                        break;
                        
                    case "4":
                        if (currentRoom == 1) {
                            System.out.println("You cannot flee from the first room!");
                        } else if (random.nextInt(100) < 30) {
                            typeTextWithCursor("You successfully flee!", 50);
                            currentRoom--; // Go back one room when fleeing
                            return; // Exit the combat immediately
                        } else {
                            typeTextWithCursor("Failed to escape!", 50);
                            int fleeDamage = calculateEnemyDamage();
                            playerHP -= fleeDamage;
                            typeTextWithCursor("Enemy strikes you for " + fleeDamage + " damage!", 50);
                        }
                        break;
                        
                    default:
                        System.out.println("Invalid choice!");
                }
            }
            
            if (!fled && enemyHP <= 0) {
                int coinReward = random.nextInt(10) + 5;
                coins += coinReward;
                typeTextWithCursor("Enemy defeated! You find " + coinReward + " coins!", 50);
            }
        }
        
        if (playerHP > 0 && !fled) {
            currentRoom++;
            typeTextWithCursor("Room cleared! Moving forward...", 50);
        }
    }
    
    static void bossFightWithTimer() {
        typeTextWithCursor("\nYou've reached the final chamber...", 50);
        typeTextWithCursor("The Shadow Wraith materializes before you!", 50);
    
        int successfulCounters = 0;  // Track successful counterattacks
    
        while (playerHP > 0 && successfulCounters < 3) {
            typeTextWithCursor("\nYour HP: " + playerHP + " | Successful Counterattacks: " + successfulCounters + "/3", 50);
            typeTextWithCursor("The Shadow Wraith prepares to attack! Dodge LEFT or RIGHT!", 50);
            
            AtomicReference<String> playerDodge = new AtomicReference<>(null);
            long startTime = System.currentTimeMillis();
    
            Thread timerThread = new Thread(() -> {
                try {
                    for (int i = 10; i > 0 && playerDodge.get() == null; i--) {
                        System.out.print("\rTime remaining: " + i + " seconds");
                        Thread.sleep(1000);
                    }
                    System.out.print("\r"); // Clear the timer line
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
    
            // Start timer on its own line
            timerThread.start();
            System.out.println("\nYour dodge (type LEFT or RIGHT):");
    
            while ((System.currentTimeMillis() - startTime) < 10000 && playerDodge.get() == null) {
                if (scanner.hasNextLine()) {
                    playerDodge.set(scanner.nextLine().toUpperCase().trim());
                }
            }
    
            System.out.println();
    
            // Boss attacks in the OPPOSITE direction of where the player should dodge
            String bossAttackDirection = random.nextBoolean() ? "LEFT" : "RIGHT";
            String correctDodge = bossAttackDirection.equals("LEFT") ? "RIGHT" : "LEFT";
            
            System.out.println("BOSS ATTACK DIRECTION: " + bossAttackDirection);
    
            if (playerDodge.get() == null) {
                typeTextWithCursor("Too slow! The Shadow Wraith's attack hits you directly!", 50);
                playerHP -= 40;
            } else if (playerDodge.get().equals(correctDodge)) {
                typeTextWithCursor("Perfect dodge! You successfully evade the attack!", 50);
                typeTextWithCursor("You counterattack with tremendous force!", 50);
                successfulCounters++;
            } else {
                typeTextWithCursor("Wrong direction! The attack grazes you!", 50);
                playerHP -= 20;
            }
    
            if (playerHP <= 0) {
                typeTextWithCursor("You have been defeated by the Shadow Wraith...", 50);
                break;
            }
        }
    
        if (successfulCounters >= 3) {
            hasRescuedElara = true;
            typeTextWithCursor("After your third successful counterattack, the Shadow Wraith dissolves into nothingness...", 50);
            typeTextWithCursor("You find Elara in a magical barrier that fades away!", 50);
            typeTextWithCursor("You go on to live happily ever after....", 50);
            typeTextWithCursor("Or so you think....", 50);
            typeTextWithCursor("You seem to find yourself stuck in an endless loop...", 50);
            typeTextWithCursor("Everytime you come close to saving your love you seem to repeat everything all over again....", 50);
            typeTextWithCursor("Unwavering, you repeat it over and over hoping to save your love.", 50);
            typeTextWithCursor("With no ending in sight, what becomes of our precious knight, the hero who saved his love?", 50);
        }
    }

    static void enterShop() {
        typeTextWithCursor("\nA mysterious merchant emerges from the shadows...", 50);
        
        while (true) {
            System.out.println("\n=== Shadow Market ===");
            System.out.println("Coins: " + coins);
            System.out.println("\n1. Browse Weapons");
            System.out.println("2. Browse Armor");
            System.out.println("3. Browse Potions");
            System.out.println("4. View Inventory");
            System.out.println("5. Leave Shop");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    browseWeapons();
                    break;
                case "2":
                    browseArmor();
                    break;
                case "3":
                    browsePotions();
                    break;
                case "4":
                    displayInventory();
                    break;
                case "5":
                    typeTextWithCursor("The merchant vanishes into the shadows...", 50);
                    currentRoom++;
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    static void browseWeapons() {
        String[][] weapons = getAvailableWeapons();
        System.out.println("\n=== Weapons ===");
        for (int i = 0; i < weapons.length; i++) {
            System.out.println((i + 1) + ". " + weapons[i][0] + " (Damage: " + weapons[i][1] + ") - " + weapons[i][2] + " coins");
        }
        System.out.println((weapons.length + 1) + ". Back");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine()) - 1;
            if (choice >= 0 && choice < weapons.length) {
                purchaseWeapon(weapons[choice]);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice!");
        }
    }

    static void browseArmor() {
        String[][] armors = getAvailableArmor();
        System.out.println("\n=== Armor ===");
        for (int i = 0; i < armors.length; i++) {
            System.out.println((i + 1) + ". " + armors[i][0] + " (Reduction: " + armors[i][1] + "%) - " + armors[i][2] + " coins");
        }
        System.out.println((armors.length + 1) + ". Back");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine()) - 1;
            if (choice >= 0 && choice < armors.length) {
                purchaseArmor(armors[choice]);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice!");
        }
    }

    static void browsePotions() {
        String[][] potions = getAvailablePotions();
        System.out.println("\n=== Potions ===");
        for (int i = 0; i < potions.length; i++) {
            System.out.println((i + 1) + ". " + potions[i][0] + " (Heals: " + potions[i][1] + " HP) - " + potions[i][2] + " coins");
        }
        System.out.println((potions.length + 1) + ". Back");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine()) - 1;
            if (choice >= 0 && choice < potions.length) {
                purchasePotion(potions[choice]);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice!");
        }
    }

    static String[][] getAvailablePotions() {
        List<String[]> availablePotions = new ArrayList<>();
        
        // Tier 1 potions (always available)
        availablePotions.add(new String[]{"Small Potion", "5", "5"});
        availablePotions.add(new String[]{"Medium Potion", "10", "10"});
        
        // Add higher tier potions as they become available
        if (currentRoom > 10) {
            availablePotions.add(new String[]{"Large Potion", "20", "20"});
        }
        if (currentRoom > 20) {
            availablePotions.add(new String[]{"Mega Potion", "50", "30"});
        }
        if (currentRoom > 30) {
            availablePotions.add(new String[]{"Ultra Potion", "100", "50"});
        }
        if (currentRoom > 40) {
            availablePotions.add(new String[]{"Supreme Potion", "200", "80"});
        }
        
        return availablePotions.toArray(new String[0][]);
    }

    static String[][] getAvailableWeapons() {
        List<String[]> availableWeapons = new ArrayList<>();
        
        // Tier 1 weapons (always available)
        availableWeapons.add(new String[]{"Stick Sword", "6", "6"});
        availableWeapons.add(new String[]{"Leaf Sword", "7", "7"});
        
        // Add higher tier weapons as they become available
        if (currentRoom > 10) {
            availableWeapons.add(new String[]{"Stone Sword", "14", "15"});
            availableWeapons.add(new String[]{"Dirt Sword", "10", "10"});
        }
        if (currentRoom > 20) {
            availableWeapons.add(new String[]{"Steel Sword", "18", "20"});
            availableWeapons.add(new String[]{"Iron Sword", "22", "25"});
        }
        if (currentRoom > 30) {
            availableWeapons.add(new String[]{"Diamond Sword", "30", "35"});
            availableWeapons.add(new String[]{"Obsidian Sword", "35", "40"});
        }
        if (currentRoom > 40) {
            availableWeapons.add(new String[]{"Dragon Sword", "40", "50"});
            availableWeapons.add(new String[]{"Shadow Sword", "50", "60"});
        }
        
        return availableWeapons.toArray(new String[0][]);
    }

    static String[][] getAvailableArmor() {
        List<String[]> availableArmor = new ArrayList<>();
        
        // Tier 1 armor (always available)
        availableArmor.add(new String[]{"Stick Armor", "2", "5"});
        availableArmor.add(new String[]{"Lead Armor", "4", "10"});
        
        // Add higher tier armor as they become available
        if (currentRoom > 10) {
            availableArmor.add(new String[]{"Iron Armor", "6", "15"});
            availableArmor.add(new String[]{"Bronze Armor", "8", "20"});
        }
        if (currentRoom > 20) {
            availableArmor.add(new String[]{"Silver Armor", "10", "25"});
            availableArmor.add(new String[]{"Gold Armor", "12", "30"});
        }
        if (currentRoom > 30) {
            availableArmor.add(new String[]{"Platinum Armor", "15", "40"});
            availableArmor.add(new String[]{"Titanium Armor", "18", "50"});
        }
        if (currentRoom > 40) {
            availableArmor.add(new String[]{"Mythic Armor", "20", "60"});
            availableArmor.add(new String[]{"Legendary Armor", "25", "80"});
        }
        
        return availableArmor.toArray(new String[0][]);
    }

    static void purchaseWeapon(String[] weapon) {
        String name = weapon[0];
        int cost = Integer.parseInt(weapon[2]);
        int damage = Integer.parseInt(weapon[1]);
    
        if (purchasedItems.contains(name)) {
            System.out.println("You already own this weapon!");
            return;
        }
    
        if (coins >= cost) {
            coins -= cost;
            weapons.add(name);
            purchasedItems.add(name);
            // Always equip the weapon and update damage during tests
            equippedWeapon = name;
            weaponDamage = damage;
            typeTextWithCursor("You purchased " + name + "!", 50);
        } else {
            System.out.println("Not enough coins!");
        }
    }

    static void purchaseArmor(String[] armor) {
        String name = armor[0];
        double reduction = Double.parseDouble(armor[1]) / 100.0;
        int cost = Integer.parseInt(armor[2]);

        if (purchasedItems.contains(name)) {
            System.out.println("You already own this armor!");
            return;
        }

        if (coins >= cost) {
            coins -= cost;
            armors.add(name);
            purchasedItems.add(name);
            armorReduction = reduction;
            equippedArmor = name;
            equippedArmorReduction = reduction;
            typeTextWithCursor("You purchased and equipped " + name + "!", 50);
        } else {
            System.out.println("Not enough coins!");
        }
    }

    static void purchasePotion(String[] potion) {
        String name = potion[0];
        int healing = Integer.parseInt(potion[1]);
        int cost = Integer.parseInt(potion[2]);
    
        if (coins >= cost) {
            coins -= cost;
            potions.add(name);
            typeTextWithCursor("You purchased " + name + " (heals " + healing + " HP) for " + cost + " coins!", 50);
        } else {
            System.out.println("Not enough coins!");
        }
    }
        
            static void usePotion() {
                if (potions.isEmpty()) {
                    System.out.println("No potions available!");
                    return;
                }
        
                System.out.println("\nAvailable Potions:");
                for (int i = 0; i < potions.size(); i++) {
                    System.out.println((i + 1) + ". " + potions.get(i));
                }
                System.out.println((potions.size() + 1) + ". Cancel");
        
                try {
                    int choice = Integer.parseInt(scanner.nextLine()) - 1;
                    if (choice >= 0 && choice < potions.size()) {
                        String potion = potions.get(choice);
                        potions.remove(choice);
                        typeTextWithCursor("You used " + potion + "!", 50);
                        int healing = getPotionHealing(potion);
                        playerHP += healing;
                        typeTextWithCursor("Recovered " + healing + " HP!", 50);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid choice!");
                }
            }
        
            static int calculatePlayerDamage() {
                int baseDamage = weaponDamage;
                boolean isCrit = random.nextInt(100) < 25; // 25% crit chance
                if (isCrit) {
                    typeTextWithCursor("Critical hit!", 50);
                    return baseDamage * 2;
                }
                return baseDamage;
            }
        
            static int calculateEnemyDamage() {
                int baseDamage = random.nextInt(10) + 5; // 5-15 base damage
                return (int)(baseDamage * (1 - armorReduction)); // Apply armor reduction
            }
        
            static int getEnemyMaxHP() {
                if (currentRoom <= 10) return 10;
                if (currentRoom <= 20) return 20;
                if (currentRoom <= 30) return 40;
                if (currentRoom <= 40) return 60;
                return 80;
            }
        
            static int getPotionHealing(String potionName) {
                switch (potionName) {
                    case "Small Potion": return 5;
                    case "Medium Potion": return 10;
                    case "Large Potion": return 20;
                    case "Mega Potion": return 50;
                    case "Ultra Potion": return 100;
                    case "Supreme Potion": return 200;
                    default: return 0;
                }
            }
}