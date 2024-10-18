public class HealingPotion extends Item {
    public final int healingAmount;

    public HealingPotion(String name, int weight, int healingAmount) {
        super(name, weight);
        this.healingAmount = healingAmount;
    }

    public int getHealAmount() {
        return healingAmount;
    }
}
