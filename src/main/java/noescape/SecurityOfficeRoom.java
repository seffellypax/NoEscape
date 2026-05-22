package noescape;

/**
 * OOP:
 *   Inheritance  — extends BaseRoom (shares name, lock, attempt tracking)
 *   Polymorphism — showHint() has a side-effect (re-locking) unique to this room
 */
public class SecurityOfficeRoom extends BaseRoom {
    private boolean hintPenaltyTriggered = false;

    public SecurityOfficeRoom(String name, boolean isLocked, String puzzleQuestion, String correctAnswer, String clueText, String hintText) {
        super(name, isLocked, puzzleQuestion, correctAnswer, clueText, hintText);
    }

    @Override
    public void showClue() {
        lastMessage = "CLUE (Security Feed): " + clueText;
    }

    @Override
    public void showHint() {
        hintPenaltyTriggered = true;
        unlock();
        lastMessage = "⚠ SECURITY ALERT! Hint request flagged. " + "Lockdown initiated — room re-locked! " + "HINT: " + hintText;
    }

    @Override
    public boolean isLocked() {
        return hintPenaltyTriggered || super.isLocked();
    }

    @Override
    public void unlock() {
        hintPenaltyTriggered = false;
        super.unlock();
    }

    @Override
    public void checkAnswer(String playerAnswer) {
        if (playerAnswer.trim().equalsIgnoreCase(correctAnswer)) {
            isSolved = true;
            lastMessage = "Correct! You cleared: " + getName();
        } else {
            attemptCount++;
            lastMessage = "Wrong answer. Attempt " + attemptCount + " used.";
        }
    }

    @Override
    public String getRoomType() { 
        return "Security Office"; 
    }
}