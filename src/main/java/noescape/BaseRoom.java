package noescape;

/**
 * Abstract base class providing shared state and default behaviour for all rooms.
 *
 * This is the "Bird" of the game — it holds the fields and logic that every
 * room shares (name, lock, puzzle text, attempt tracking, solved flag, last
 * message). Concrete subclasses inherit all of this and then OVERRIDE only
 * the methods that give their room its unique personality.
 *
 * Shared (inherited, not overridden):
 *   getName(), isLocked(), unlock(), showPuzzle(), isSolved(),
 *   getAttempts(), getLastMessage(), getPuzzle(), enter()
 *
 * Polymorphic (each subclass overrides differently):
 *   showClue()     — when / how the clue is revealed
 *   showHint()     — cost or side-effect of using a hint
 *   checkAnswer()  — matching rules (case-insensitive, exact, etc.)
 *
 * OOP:
 *   Inheritance   — subclasses extend BaseRoom and inherit shared behaviour
 *   Polymorphism  — showClue(), showHint(), checkAnswer() behave differently
 *                   per subclass even when called through a Escapable reference
 */
public abstract class BaseRoom implements Escapable {

    // -------------------------------------------------------------------------
    // Shared state (every room has these)
    // -------------------------------------------------------------------------
    private final String name;
    private       boolean isLocked;

    protected final String puzzleQuestion;
    protected final String correctAnswer;
    protected final String clueText;
    protected final String hintText;

    protected String  lastMessage  = "";
    protected boolean isSolved     = false;
    protected int     attemptCount = 0;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------
    protected BaseRoom(String name, boolean isLocked,
                       String puzzleQuestion, String correctAnswer,
                       String clueText, String hintText) {
        this.name           = name;
        this.isLocked       = isLocked;
        this.puzzleQuestion = puzzleQuestion;
        this.correctAnswer  = correctAnswer;
        this.clueText       = clueText;
        this.hintText       = hintText;
    }

    // -------------------------------------------------------------------------
    // Shared implementations (inherited by all subclasses unchanged)
    // -------------------------------------------------------------------------

    @Override
    public String getName() { return name; }

    @Override
    public boolean isLocked() { return isLocked; }

    @Override
    public void unlock() { this.isLocked = false; }

    @Override
    public boolean enter(Player player) {
        if (isLocked) {
            lastMessage = "Room is locked! Solve the previous room first.";
            return false;
        }
        lastMessage = "You entered: " + name;
        return true;
    }

    @Override
    public void showPuzzle() {
        lastMessage = "PUZZLE: " + puzzleQuestion;
    }

    @Override
    public boolean isSolved()       { return isSolved; }

    @Override
    public int     getAttempts()    { return attemptCount; }

    @Override
    public String  getLastMessage() { return lastMessage; }

    @Override
    public String  getPuzzle()      { return puzzleQuestion; }

    // -------------------------------------------------------------------------
    // Polymorphic methods — each subclass MUST override these
    // -------------------------------------------------------------------------

    /** Reveals a clue. Subclasses decide when and under what conditions. */
    @Override
    public abstract void showClue();

    /** Reveals a hint. Subclasses may apply a gameplay penalty. */
    @Override
    public abstract void showHint();

    /**
     * Evaluates the player's answer. Subclasses define the matching rules
     * (case-insensitive, exact, trimmed, etc.).
     */
    @Override
    public abstract void checkAnswer(String playerAnswer);
}