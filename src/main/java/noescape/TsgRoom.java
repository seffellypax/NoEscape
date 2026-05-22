package noescape;

/**
 * OOP:
 *   Inheritance  — extends BaseRoom (shares name, lock, attempt tracking)
 *   Polymorphism — checkAnswer() uses strict exact matching, unlike all other rooms
 */
public class TsgRoom extends BaseRoom {
    public TsgRoom(String name, boolean isLocked, String puzzleQuestion, String correctAnswer, String clueText, String hintText) {
        super(name, isLocked, puzzleQuestion, correctAnswer, clueText, hintText);
    }

    @Override
    public void showClue() {
        lastMessage = "CLUE: " + clueText + "  [TSG Terminal: answer is case-sensitive!]";
    }

    @Override
    public void showHint() {
        lastMessage = "HINT: " + hintText;
    }

    @Override
    public void checkAnswer(String playerAnswer) {
        if (playerAnswer.equals(correctAnswer)) {
            isSolved = true;
            lastMessage = "Correct! You cleared: " + getName();
        } else {
            attemptCount++;
            lastMessage = "Wrong answer (case-sensitive). Attempt " + attemptCount + " used. " + "Check your capitalisation!";
        }
    }

    @Override
    public String getRoomType() { 
        return "TSG"; 
    }
}