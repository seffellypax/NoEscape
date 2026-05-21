package noescape;

public abstract class Room implements RoomBehavior {
    private String name;
    private boolean locked;

    public Room(String name, boolean locked) {
        this.name = name;
        this.locked = locked;
    }
    //access the room's name
    @Override
    public String getName() {
        return name;
    }
    //for locked status of the room
    @Override
    public boolean isLocked() {
        return this.locked;
    }
    @Override
    public abstract boolean enter(Player player);
    
    @Override
    public abstract void showPuzzle();

    @Override
    public abstract void showClue();

    @Override
    public abstract void showHint();

    @Override
    public abstract void checkAnswer(String answer);
}
