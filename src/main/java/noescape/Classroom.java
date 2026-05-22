package noescape;

/**
 * STRICT MODE room — models a classroom where the teacher withholds help
 * until the student has genuinely struggled.
 *
 * Polymorphic behaviour (overrides BaseRoom):
 *
 *   showClue()    → BLOCKED until the player has failed at least twice.
 *                   A professor doesn't just give away the answer — you have
 *                   to show you tried first.
 *
 *   showHint()    → Always available; gives a small letter-based nudge.
 *
 *   checkAnswer() → Case-insensitive, trimmed match (standard behaviour).
 *
 * Bird analogy: Eagle — powerful but demands effort before rewarding it.
 *
 * OOP:
 *   Inheritance  — extends BaseRoom (shares name, lock, attempt tracking)
 *   Polymorphism — showClue() behaves differently than every other room
 */
public class Classroom extends BaseRoom {

    private static final int ATTEMPTS_REQUIRED_FOR_CLUE = 2;

    public Classroom(String name, boolean isLocked,
                     String puzzleQuestion, String correctAnswer,
                     String clueText, String hintText) {
        super(name, isLocked, puzzleQuestion, correctAnswer, clueText, hintText);
    }

    /**
     * STRICT: Clue is only revealed after the player has failed at least twice.
     * Models a classroom rule — the teacher won't help until you've tried.
     */
    @Override
    public void showClue() {
        if (attemptCount < ATTEMPTS_REQUIRED_FOR_CLUE) {
            int attemptsNeeded = ATTEMPTS_REQUIRED_FOR_CLUE - attemptCount;
            lastMessage = "The professor says: Try harder first! "
                        + "Fail " + attemptsNeeded + " more time(s) to unlock the clue.";
        } else {
            lastMessage = "CLUE: " + clueText;
        }
    }

    /**
     * Standard hint — always available, gives a letter nudge.
     */
    @Override
    public void showHint() {
        lastMessage = "HINT: " + hintText;
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
    public String getRoomType() { return "Classroom"; }
}