package noescape;

/**
 * LOCKDOWN MODE room — the Security Office takes no chances.
 * Requesting a hint triggers a security lockdown: the room locks itself again
 * and must be re-unlocked by solving the previous room... which is already solved.
 * In practice this means the player must call {@code unlock()} again themselves
 * (Game.java re-unlocks it automatically with a warning message).
 *
 * Polymorphic behaviour (overrides BaseRoom):
 *
 *   showClue()    → Always available; security cameras give you a partial view.
 *
 *   showHint()    → TRIGGERS LOCKDOWN. The room locks itself.
 *                   The player is warned. Game.java detects the re-locked state
 *                   and immediately re-unlocks it, but the feedback message
 *                   communicates the dramatic penalty.
 *
 *   checkAnswer() → Case-insensitive, trimmed match (standard behaviour).
 *
 * Bird analogy: Owl — wise and watchful; any suspicious move triggers an alarm.
 *
 * OOP:
 *   Inheritance  — extends BaseRoom (shares name, lock, attempt tracking)
 *   Polymorphism — showHint() has a side-effect (re-locking) unique to this room
 */
public class SecurityOfficeRoom extends BaseRoom {

    private boolean hintPenaltyTriggered = false;

    public SecurityOfficeRoom(String name, boolean isLocked,
                              String puzzleQuestion, String correctAnswer,
                              String clueText, String hintText) {
        super(name, isLocked, puzzleQuestion, correctAnswer, clueText, hintText);
    }

    /**
     * Standard clue — security cameras show you part of the answer.
     */
    @Override
    public void showClue() {
        lastMessage = "CLUE (Security Feed): " + clueText;
    }

    /**
     * LOCKDOWN PENALTY: Requesting a hint triggers a security alarm.
     * The room re-locks itself. Game.java will immediately re-unlock it
     * so gameplay continues, but the player feels the consequence.
     */
    @Override
    public void showHint() {
        hintPenaltyTriggered = true;
        unlock(); // reset first so we can re-lock cleanly
        // Re-lock by overriding the inherited field via a flag read in isLocked()
        // We achieve the lock effect by overriding isLocked() below.
        lastMessage = "⚠ SECURITY ALERT! Hint request flagged. "
                    + "Lockdown initiated — room re-locked! "
                    + "HINT: " + hintText;
    }

    /**
     * Returns {@code true} while the lockdown flag is active.
     * Game.java's normal unlock flow will call {@code unlock()} to clear it.
     */
    @Override
    public boolean isLocked() {
        return hintPenaltyTriggered || super.isLocked();
    }

    /**
     * Clears both the inherited lock and the lockdown penalty flag.
     */
    @Override
    public void unlock() {
        hintPenaltyTriggered = false;
        super.unlock();
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
    public String getRoomType() { return "Security Office"; }
}