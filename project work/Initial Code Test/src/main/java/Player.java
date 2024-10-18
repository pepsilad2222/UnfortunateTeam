public class Player {
    private int health = 100; // Starting health

    public void enterRoom(Room room) {
        health = room.applyHealthChange(health); // Apply the health change from the room
    }

    public int getHealth() {
        return health;
    }

    // Add other player methods as needed
}
