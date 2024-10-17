import java.util.*;

public class Inventory {
    public final List<Item> items; // List of all items in the inventory
    public final int weightLimit; // Maximum weight limit for the inventory
    public int currentWeight; // Current weight of items in the inventory
    public final List<Weapon> weapons; // List of weapons in the inventory
    public final List<HealingPotion> healingPotions; // List of healing potions in the inventory

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
            return false;  // Make sure this block prevents adding the item if weight exceeds the limit
        }
        

        // Add the item to the appropriate list
        if (item instanceof Weapon) {
            weapons.add((Weapon) item);
        } else if (item instanceof HealingPotion) {
            healingPotions.add((HealingPotion) item);
        }

        // Add the item to the main inventory list and adjust the weight
        items.add(item);
        currentWeight += item.getWeight();
        return true;
    }

    public int getInventoryWeight() {
        return currentWeight; // Return the current weight of items in the inventory
    }

    public void removeItem(Item item) {
        if (items.remove(item)) {
            currentWeight -= item.getWeight();
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
        return items.contains(item);
    }

    public boolean addItem(Item item) {
        if (this.currentWeight + item.getWeight() > this.weightLimit) {
            return false; // Prevent adding if weight limit exceeded
        }
        items.add(item);
        currentWeight += item.getWeight();
        return true;
    }
}
