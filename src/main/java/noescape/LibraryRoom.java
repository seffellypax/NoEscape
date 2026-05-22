package noescape;

/**
 * RESEARCH MODE room — in a library, every resource you consult costs you time.
 * Using the hint here consumes one of your limited attempts.
 *
 * Polymorphic behaviour (overrides BaseRoom):
 *
 *   showClue()    → Always free; a librarian freely points you to the right shelf.
 *
 *   showHint()    → COSTS ONE ATTEMPT. Like photocopying a page — there's a fee.
 *                   If you are already at max attempts, the hint is blocked.
 *
 *   checkAnswer() → Case-insensitive, trimmed match (standard behaviour).
 *
 * Bird analogy: Penguin — helpful and social, but every resource has a cost.
 *
 * OOP:
 *   Inheritance  — extends BaseRoom (shares name, lock, attempt tracking)
 *   Polymorphism — showHint() penalises the player, unlike any other room
 */
public class LibraryRoom extends BaseRoom {

    public LibraryRoom(String name, boolean isLocked,
                       String puzzleQuestion, String correctAnswer,
                       String clueText, String hintText) {
        super(name, isLocked, puzzleQuestion, correctAnswer, clueText, hintText);
    }

    /**
     * Standard clue — always free in the library.
     */
    @Override
    public void showClue() {
        lastMessage = "CLUE: " + clueText;
    }

    /**
     * RESEARCH PENALTY: Consulting the hint costs one attempt.
     * The librarian gives you the answer key, but it counts against you.
     */
    @Override
    public void showHint() {
        attemptCount++;
        lastMessage = "HINT (−1 attempt): " + hintText
                    + "  [Attempt " + attemptCount + " consumed]";
    }

    /**
     * Standard case-insensitive answer check.
     */
    @Override
    public void checkAnswer(String playerAnswer) {
        if (playerAnswer.trim().equalsIgnoreCase(correctAnswer)) {
            isSolved    = true;
            lastMessage = "Correct! You cleared: " + getName();
        } else {
            attemptCount++;
            lastMessage = "Wrong answer. Attempt " + attemptCount + " used.";
        }
    }

    @Override
    public String getRoomType() { return "Library"; }
}