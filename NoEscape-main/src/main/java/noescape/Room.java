package noescape;
public abstract class Room implements RoomBehavior {

    // Private fields — accessed only through getters/setters
    private String  name;
    private boolean locked;

    // Constructor called by each child class via super()
    public Room(String name, boolean locked) {
        this.name = name;
        this.locked = locked;
    }

    // ── Shared methods (inherited by all child classes) ──────

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    @Override
    public void unlock() {
        this.locked = false;
    }

    @Override
    public boolean enter(Player player) {
        if (locked) {
            return false;   // GUI will handle the locked message
        }
        return true;
    }

    // ── Abstract methods (each child class defines its own) ──

    @Override 
    public abstract String  getPuzzle();
    @Override 
    public abstract String  getClue();
    @Override 
    public abstract String  getHint();
    @Override 
    public abstract boolean checkAnswer(String answer);
}