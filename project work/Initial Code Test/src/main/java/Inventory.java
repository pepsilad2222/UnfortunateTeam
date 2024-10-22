import java.util.*;

public class Inventory {
    private final List<Item> items; // List of all items in the inventory
    private final int weightLimit; // Maximum weight limit for the inventory
    private int currentWeight; // Current weight of items in the inventory
    public final List<Weapon> weapons; // List of weapons in the inventory
    private final List<HealingPotion> healingPotions; // List of healing potions in the inventory

    public Inventory(int weightLimit) {
        this.items = new ArrayList<>();
        this.weapons = new ArrayList<>();
        this.healingPotions = new ArrayList<>();
        this.weightLimit = weightLimit;
        this.currentWeight = 0;
    }

    public boolean addToInventory(Item item) {
        // Check if adding the item exceeds the weight limit
        if (currentWeight + item.getWeight() > weightLimit) {
            System.out.println("Cannot add " + item.getName() + ". Exceeds weight limit.");
            return false;  // Prevent adding the item if weight exceeds the limit
        }
    
        // Add the item to the main inventory list and adjust the weight
        items.add(item); // Add to the main inventory list
        currentWeight += item.getWeight(); // Update the current weight
    
        // Add the item to the appropriate list based on its type
        if (item instanceof Weapon) {
            weapons.add((Weapon) item); // Add to weapons list
        } else if (item instanceof HealingPotion) {
            healingPotions.add((HealingPotion) item); // Add to potions list
            System.out.println("Healing potion added to inventory. Total potions: " + healingPotions.size());
        } else {
            System.out.println("Item type not recognized, item not added: " + item.getName());
        }
    
        return true; // Indicate success
    }
    

    public int getInventoryWeight() {
        return currentWeight; // Return the current weight of items in the inventory
    }

    public void removeItem(Item item) {
        if (items.remove(item)) {
            currentWeight -= item.getWeight();
            
            // Remove from specific lists if the item is a Weapon or HealingPotion
            if (item instanceof Weapon) {
                weapons.remove(item); // Ensure we are removing the correct weapon
            } else if (item instanceof HealingPotion) {
                healingPotions.remove(item); // Ensure we are removing the correct potion
            }
            
            System.out.println(item.getName() + " removed from your inventory.");
        } else {
            System.out.println(item.getName() + " is not in your inventory.");
        }
    }

    public void showInventory() {
        if (items.isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            System.out.println("Inventory:");
            for (Item item : items) {
                System.out.println("- " + item.getName() + " (Weight: " + item.getWeight() + ")");
            }
        }
    }

    public boolean contains(Item item) {
        return items.contains(item); // Check if the inventory contains a specific item
    }

    public boolean isEmpty() {
        return items.isEmpty(); // Check if the items list is empty
    }

    public void resetInventory() {
        items.clear(); // Clear all items
        weapons.clear(); // Clear all weapons
        healingPotions.clear(); // Clear all healing potions
        currentWeight = 0; // Reset the current weight
    }

    public List<HealingPotion> getHealingPotions() {
        return new ArrayList<>(healingPotions); // Return a copy of the list
    }
    
}
