public abstract class Room implements IRoom {
    private String  name;
    private boolean locked;

    public Room(String name, boolean locked) {
        this.name   = name;
        this.locked = locked;
    }
}
