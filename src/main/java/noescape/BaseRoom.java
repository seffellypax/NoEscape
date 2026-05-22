package noescape;

/**
 * OOP:
 *   Inheritance   — subclasses extend BaseRoom and inherit shared behaviour
 *   Polymorphism  — showClue(), showHint(), checkAnswer() behave differently
 *                   per subclass even when called through a Escapable reference
 */
public abstract class BaseRoom implements Escapable {
    private final String name;
    private boolean isLocked;

    protected final String puzzleQuestion;
    protected final String correctAnswer;
    protected final String clueText;
    protected final String hintText;

    protected String lastMessage = "";
    protected boolean isSolved = false;
    protected int attemptCount = 0;

    protected BaseRoom(String name, boolean isLocked, String puzzleQuestion, String correctAnswer, String clueText, String hintText) {
        this.name = name;
        this.isLocked = isLocked;
        this.puzzleQuestion = puzzleQuestion;
        this.correctAnswer = correctAnswer;
        this.clueText = clueText;
        this.hintText = hintText;
    }

    @Override
    public String getName() { 
        return name; 
    }
    @Override
    public boolean isLocked() { 
        return isLocked; 
    }
    @Override
    public void unlock() { 
        this.isLocked = false; 
    }
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
    public boolean isSolved() { 
        return isSolved; 
    }
    @Override
    public int getAttempts() { 
        return attemptCount; 
    }
    @Override
    public String getLastMessage() { 
        return lastMessage; 
    }
    @Override
    public String getPuzzle() { 
        return puzzleQuestion; 
    }

    @Override
    public abstract void showClue();
    @Override
    public abstract void showHint();
    @Override
    public abstract void checkAnswer(String playerAnswer);
}