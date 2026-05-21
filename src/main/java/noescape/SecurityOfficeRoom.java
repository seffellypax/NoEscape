package noescape;

/**
 * SecurityOfficeRoom
 * Implements RoomBehavior directly.
 *
 * OOP:
 *   Encapsulation - all fields are private
 *   Abstraction   - implements RoomBehavior
 */
public class SecurityOfficeRoom implements RoomBehavior {

    private String  name;
    private boolean locked;
    private String  puzzle;
    private String  answer;
    private String  clue;
    private String  hint;
    private String  lastMessage = "";
    private boolean solved      = false;
    private int     attempts    = 0;

    public SecurityOfficeRoom(String name, boolean locked,
                        String puzzle, String answer,
                        String clue,   String hint) {
        this.name   = name;
        this.locked = locked;
        this.puzzle = puzzle;
        this.answer = answer;
        this.clue   = clue;
        this.hint   = hint;
    }

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
            lastMessage = "Room is locked! Solve the previous room first.";
            return false;
        }
        lastMessage = "You entered: " + name;
        return true;
    }

    @Override
    public void showPuzzle() {
        lastMessage = "PUZZLE: " + puzzle;
    }

    @Override
    public void showClue() {
        lastMessage = "CLUE: " + clue;
    }

    @Override
    public void showHint() {
        lastMessage = "HINT: " + hint;
    }

    @Override
    public void checkAnswer(String userAnswer) {
        if (userAnswer.trim().equalsIgnoreCase(answer)) {
            solved      = true;
            lastMessage = "Correct! You cleared: " + name;
        } else {
            attempts++;
            lastMessage = "Wrong answer. Attempt " + attempts + " used.";
        }
    }

    @Override public String  getRoomType()    { return "Security Office";    }
    @Override public boolean isSolved()       { return solved;      }
    @Override public int     getAttempts()    { return attempts;    }
    @Override public String  getLastMessage() { return lastMessage; }
    @Override public String  getPuzzle()      { return puzzle;      }
}