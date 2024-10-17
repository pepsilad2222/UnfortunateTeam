public class Weapon extends Item {
    public final int damage;

    public Weapon(String name, int weight, int damage) {
        super(name, weight);
        this.damage = damage;
    }

    public int getAttackPower() {
        return damage; // Return the damage value
    }
}
