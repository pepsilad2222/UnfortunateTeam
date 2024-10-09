import java.util.Scanner;

public class Adventure {
    private Scanner scanner;

    public Adventure() {
        scanner = new Scanner(System.in);
    }

    public void startAdventure() {
        System.out.println("Welcome to the Adventure!");
        System.out.println("You are a brave knight standing at a crossroad.");
        System.out.println("Do you want to go left or right?");
        System.out.println("1: Left");
        System.out.println("2: Right");

        int choice = getUserInput();

        if (choice == 1) {
            goLeft();
        } else if (choice == 2) {
            goRight();
        } else {
            System.out.println("Invalid choice.");
            startAdventure();
        }
    }

    private void goLeft() {
        System.out.println("You chose to go left. But this path is under construction. Please choose again.");
        startAdventure(); // Loop back to the start for now
    }

    private void goRight() {
        System.out.println("You chose to go right. You wrangle your way through a dense forest...");

        // Trigger PathDungeon
        System.out.print("Enter your knight's name: ");
        String knightName = scanner.nextLine();
        PathDungeon dungeonAdventure = new PathDungeon(knightName); 
        dungeonAdventure.startAdventure(); 
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
        Adventure adventureGame = new Adventure();
        adventureGame.startAdventure();
    }
}
