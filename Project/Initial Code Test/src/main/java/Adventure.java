import java.util.Random;
import java.util.Scanner;

public class Adventure {
    public static int lifeChecker = 100; // Health starts at 100
    public int journeyProgress = 0;

    public Adventure() {
        // Start the player with full health
        lifeChecker = 100;
    }

    // Method to simulate a journey
    public void journey() {
        journeyProgress++;
        Random random = new Random();
        int event = random.nextInt(4); // Random event (0-3)

        System.out.println("\nYou continue your journey...");

        switch (event) {
            case 0:
                System.out.println("You encountered a peaceful meadow. You feel rejuvenated.");
                heal(10); // Heal the player
                break;
            case 1:
                System.out.println("You were attacked by wild animals! You lose health.");
                damage(20); // Player loses health
                break;
            case 2:
                System.out.println("You found some food and supplies. You gain some health.");
                heal(15); // Heal the player
                break;
            case 3:
                System.out.println("You fell into a trap! You lose significant health.");
                damage(30); // Player loses more health
                break;
        }

        System.out.println("Journey Progress: " + journeyProgress + " miles.");
        System.out.println("Your current health is: " + lifeChecker + " HP.");
    }

    // Method to heal the player
    public void heal(int amount) {
        lifeChecker = Math.min(lifeChecker + amount, 100); // Max health is 100
        System.out.println("You gained " + amount + " HP.");
    }

    // Method to damage the player
    public void damage(int amount) {
        lifeChecker = Math.max(lifeChecker - amount, 0); // Health can't go below 0
        System.out.println("You lost " + amount + " HP.");
        if (lifeChecker == 0) {
            System.out.println("You have no health left! Game over.");
        }
    }

    // Method to declare bankruptcy (end the game)
    public static void bankrupcy() {
        lifeChecker = 0;
        System.out.println("You've gone bankrupt. Your life level is now 0.");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Adventure adventure = new Adventure();
        boolean playing = true;

        System.out.println("Welcome to the Text Adventure Game!");
        System.out.println("Your current health is: " + lifeChecker + " HP.");

        while (playing) {
            System.out.println("\nChoose an action:");
            System.out.println("1. Continue your journey");
            System.out.println("2. Check health");
            System.out.println("3. Declare bankruptcy (End the game)");
            System.out.println("4. Exit game");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    if (lifeChecker > 0) {
                        adventure.journey();
                    } else {
                        System.out.println("You can't continue your journey with 0 health. Game over.");
                        playing = false;
                    }
                    break;
                case 2:
                    System.out.println("Your current health is: " + lifeChecker + " HP.");
                    break;
                case 3:
                    Adventure.bankrupcy();
                    playing = false; // End game after bankruptcy
                    break;
                case 4:
                    System.out.println("Thanks for playing! Goodbye.");
                    playing = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
        scanner.close();
    }
}
