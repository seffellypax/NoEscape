package noescape;

/**
 * TECHNICAL MODE room — the TSG server room demands precision.
 * Answers must match exactly, including letter casing, just like a real terminal command.
 *
 * Polymorphic behaviour (overrides BaseRoom):
 *
 *   showClue()    → Always available; explains what format the answer must be in.
 *
 *   showHint()    → Always available; gives a letter nudge.
 *
 *   checkAnswer() → EXACT / CASE-SENSITIVE match (no trim forgiveness).
 *                   "emr" ≠ "EMR". Type it right or it fails.
 *                   This forces the player to pay close attention to the puzzle wording.
 *
 * Bird analogy: Parrot — repeats exactly what it hears; precision is everything.
 *
 * OOP:
 *   Inheritance  — extends BaseRoom (shares name, lock, attempt tracking)
 *   Polymorphism — checkAnswer() uses strict exact matching, unlike all other rooms
 */
public class TsgRoom extends BaseRoom {

    public TsgRoom(String name, boolean isLocked,
                   String puzzleQuestion, String correctAnswer,
                   String clueText, String hintText) {
        super(name, isLocked, puzzleQuestion, correctAnswer, clueText, hintText);
    }

    /**
     * Standard clue — also reminds the player about the case-sensitivity rule.
     */
    @Override
    public void showClue() {
        lastMessage = "CLUE: " + clueText
                    + "  [TSG Terminal: answer is case-sensitive!]";
    }

    /**
     * Standard hint — gives a letter/format nudge.
     */
    @Override
    public void showHint() {
        lastMessage = "HINT: " + hintText;
    }

    /**
     * EXACT MATCH: The answer must match the stored value precisely —
     * same casing, same spacing. No forgiveness. Just like typing a terminal command.
     */
    @Override
    public void checkAnswer(String playerAnswer) {
        if (playerAnswer.equals(correctAnswer)) {
            isSolved    = true;
            lastMessage = "Correct! You cleared: " + getName();
        } else {
            attemptCount++;
            lastMessage = "Wrong answer (case-sensitive). Attempt " + attemptCount + " used. "
                        + "Check your capitalisation!";
        }
    }

    @Override
    public String getRoomType() { return "TSG"; }
}