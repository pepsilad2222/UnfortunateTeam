public class Player {
    private int health = 100; // Starting health

    public void enterRoom(Room room) {
        health = room.applyHealthChange(health);
        // Ensure health does not go below zero
        if (health < 0) {
            health = 0; // Set health to 0 if it goes negative
        }
    }
    

    public int getHealth() {
        return health;
    }

    public void useHealingPotion(HealingPotion potion) {
        health += potion.getHealAmount(); // Increase health by potion's healing amount
        if (health > 100) { // Assuming 100 is the max health
            health = 100; // Cap health at maximum
        }
        System.out.println("Used " + potion.getName() + ". Health is now: " + health);
    }

    // Add the receiveDamage method
    public void receiveDamage(int damage) {
        health -= damage; // Decrease health by damage amount
        if (health < 0) { // Ensure health doesn't go below zero
            health = 0;
        }
        System.out.println("Received " + damage + " damage. Health is now: " + health);
    }
    
    // Add other player methods as needed
}
