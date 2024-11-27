import java.util.*;
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
    startGame(args);
    }
    

    public static void startGame(String[] args) {
        typeTextWithCursor("Welcome to The Shadow's Labyrinth!", 50);
        typeTextWithCursor("Your mission: Rescue Elara from the Shadow Wraith!", 50);
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
        System.out.println("Weapons: " + (weapons.isEmpty() ? "None" : String.join(", ", weapons)));
        System.out.println("Armor: " + (armors.isEmpty() ? "None" : String.join(", ", armors)));
        System.out.println("Potions: " + (potions.isEmpty() ? "None" : String.join(", ", potions)));
        System.out.println("\n=== Currently Equipped ===");
        System.out.println("Weapon: " + equippedWeapon);
        System.out.println("Armor: " + equippedArmor);
        System.out.println("Damage Reduction: " + (equippedArmorReduction * 100) + "%");
        System.out.println("==========================");
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
        
        typeTextWithCursor("You face " + numEnemies + " enemies!", 50);
        
        for (int i = 1; i <= numEnemies && playerHP > 0; i++) {
            int enemyHP = random.nextInt(maxHP) + (maxHP / 2);
            typeTextWithCursor("\nEnemy " + i + " appears! (HP: " + enemyHP + ")", 50);
            
            while (enemyHP > 0 && playerHP > 0) {
                System.out.println("\nYour HP: " + playerHP + " | Enemy HP: " + enemyHP);
                System.out.println("1. Attack");
                System.out.println("2. Use Potion");
                System.out.println("3. View Inventory");
                System.out.println("4. Attempt to Flee");
                
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
                        if (random.nextInt(100) < 30) {
                            typeTextWithCursor("You successfully flee!", 50);
                            return;
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
            
            if (enemyHP <= 0) {
                int coinReward = random.nextInt(10) + 5;
                coins += coinReward;
                typeTextWithCursor("Enemy defeated! You find " + coinReward + " coins!", 50);
            }
        }
        
        if (playerHP > 0) {
            currentRoom++;
            typeTextWithCursor("Room cleared! Moving forward...", 50);
        }
    }

    static void bossFightWithTimer() {
        // Existing implementation, but with seed setting method
        setSeed(42L); // Optional: set seed for testing
    
        typeTextWithCursor("\nYou've reached the final chamber...", 50);
        typeTextWithCursor("The Shadow Wraith materializes before you!", 50);
    
        int bossHP = 200;
        int phase = 1;
    
        while (bossHP > 0 && playerHP > 0) {
            typeTextWithCursor("\nPhase " + phase + "!", 50);
            typeTextWithCursor("Your HP: " + playerHP + " | Shadow Wraith HP: " + bossHP, 50);
    
            typeTextWithCursor("The Shadow Wraith prepares to attack! Dodge LEFT or RIGHT!", 50);
            System.out.println();
            long startTime = System.currentTimeMillis();
            AtomicReference<String> playerDodge = new AtomicReference<>(null);
    
            // Create a separate thread for the timer display
            Thread timerThread = new Thread(() -> {
                try {
                    for (int i = 10; i > 0; i--) {
                        if (playerDodge.get() != null) break; // Stop if player has made a choice
                        System.out.print("\rTime remaining: " + i + " seconds"); // \r returns cursor to start of line
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
    
            timerThread.start();
    
            // Read player input with timeout
            while ((System.currentTimeMillis() - startTime) < 10000 && playerDodge.get() == null) {
                if (scanner.hasNextLine()) {
                    playerDodge.set(scanner.nextLine().toUpperCase());
                }
            }
    
            // Clear the timer line and move to the next line
            System.out.println();
    
            // Use random to determine attack direction
            String correctDodge = random.nextBoolean() ? "LEFT" : "RIGHT";
            System.out.println("BOSS ATTACK DIRECTION: " + correctDodge);
    
            if (playerDodge.get() == null) {
                typeTextWithCursor("Too slow! The Shadow Wraith's attack hits you directly!", 50);
                playerHP -= 40;
            } else if (playerDodge.get().equals(correctDodge)) {
                typeTextWithCursor("Perfect dodge! You can counterattack!", 50);
                int damage = calculatePlayerDamage() * 2;
                bossHP -= damage;
                typeTextWithCursor("You deal " + damage + " damage!", 50);
            } else {
                typeTextWithCursor("Wrong direction! The attack grazes you!", 50);
                playerHP -= 20;
            }
    
            if (bossHP > 0) {
                switch (phase) {
                    case 1 -> typeTextWithCursor("Shadow Wraith uses Dark Pulse!", 50);
                    case 2 -> typeTextWithCursor("Shadow Wraith uses Void Strike!", 50);
                    case 3 -> typeTextWithCursor("Shadow Wraith uses Death's Embrace!", 50);
                }
                playerHP -= (phase * 15); // Increase damage with phase
            }
    
            if (bossHP <= 150 && phase == 1) {
                phase = 2;
                typeTextWithCursor("The Shadow Wraith enters phase 2!", 50);
            } else if (bossHP <= 75 && phase == 2) {
                phase = 3;
                typeTextWithCursor("The Shadow Wraith enters its final phase!", 50);
            }
        }
    
        if (playerHP > 0) {
            hasRescuedElara = true;
            typeTextWithCursor("The Shadow Wraith dissolves into nothingness...", 50);
            typeTextWithCursor("You find Elara in a magical barrier that fades away!", 50);
        }
    }
    
    // Add this method to set the seed for testing
    static void setSeed(long seed) {
        random = new Random(seed);
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
                if (currentRoom <= 10) {
                    return new String[][] {
                        {"Small Potion", "5", "5"},
                        {"Medium Potion", "10", "10"}
                    };
                } else if (currentRoom <= 20) {
                    return new String[][] {
                        {"Medium Potion", "10", "10"},
                        {"Large Potion", "20", "20"}
                    };
                } else if (currentRoom <= 30) {
                    return new String[][] {
                        {"Large Potion", "20", "20"},
                        {"Mega Potion", "50", "30"}
                    };
                } else if (currentRoom <= 40) {
                    return new String[][] {
                        {"Mega Potion", "50", "30"},
                        {"Ultra Potion", "100", "50"}
                    };
                } else {
                    return new String[][] {
                        {"Ultra Potion", "100", "50"},
                        {"Supreme Potion", "200", "80"}
                    };
                }
            }
        
            static String[][] getAvailableWeapons() {
                if (currentRoom <= 10) {
                    return new String[][] {
                        {"Stick Sword", "5", "5"},
                        {"Leaf Sword", "7", "7"}
                    };
                } else if (currentRoom <= 20) {
                    return new String[][] {
                        {"Stone Sword", "14", "15"},
                        {"Dirt Sword", "10", "10"}
                    };
                } else if (currentRoom <= 30) {
                    return new String[][] {
                        {"Steel Sword", "18", "20"},
                        {"Iron Sword", "22", "25"}
                    };
                } else if (currentRoom <= 40) {
                    return new String[][] {
                        {"Diamond Sword", "30", "35"},
                        {"Obsidian Sword", "35", "40"}
                    };
                } else {
                    return new String[][] {
                        {"Dragon Sword", "40", "50"},
                        {"Shadow Sword", "50", "60"}
                    };
                }
            }
        
            static String[][] getAvailableArmor() {
                if (currentRoom <= 10) {
                    return new String[][] {
                        {"Stick Armor", "2", "5"},
                        {"Lead Armor", "4", "10"}
                    };
                } else if (currentRoom <= 20) {
                    return new String[][] {
                        {"Iron Armor", "6", "15"},
                        {"Bronze Armor", "8", "20"}
                    };
                } else if (currentRoom <= 30) {
                    return new String[][] {
                        {"Silver Armor", "10", "25"},
                        {"Gold Armor", "12", "30"}
                    };
                } else if (currentRoom <= 40) {
                    return new String[][] {
                        {"Platinum Armor", "15", "40"},
                        {"Titanium Armor", "18", "50"}
                    };
                } else {
                    return new String[][] {
                        {"Mythic Armor", "20", "60"},
                        {"Legendary Armor", "25", "80"}
                    };
                }
            }
        
            static void purchaseWeapon(String[] weapon) {
                String name = weapon[0];
                int damage = Integer.parseInt(weapon[1]);
                int cost = Integer.parseInt(weapon[2]);
        
                if (purchasedItems.contains(name)) {
                    System.out.println("You already own this weapon!");
                    return;
                }
        
                if (coins >= cost) {
                    coins -= cost;
                    weapons.add(name);
                    purchasedItems.add(name);
                    weaponDamage = damage;
                    equippedWeapon = name;
                    typeTextWithCursor("You purchased and equipped " + name + "!", 50);
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
                boolean isCrit = random.nextInt(100) < 15; // 15% crit chance
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
