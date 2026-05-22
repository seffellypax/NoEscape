package noescape;

/**
 * OOP:
 *   Inheritance  — extends BaseRoom (shares name, lock, attempt tracking)
 *   Polymorphism — showClue() behaves differently than every other room
 */
public class Classroom extends BaseRoom {
    private static final int ATTEMPTS_REQUIRED_FOR_CLUE = 2;

    public Classroom(String name, boolean isLocked, String puzzleQuestion, String correctAnswer, String clueText, String hintText) {
        super(name, isLocked, puzzleQuestion, correctAnswer, clueText, hintText);
    }

    @Override
    public void showClue() {
        if (attemptCount < ATTEMPTS_REQUIRED_FOR_CLUE) {
            int attemptsNeeded = ATTEMPTS_REQUIRED_FOR_CLUE - attemptCount;
            lastMessage = "The professor says: Try harder first! " + "Fail " + attemptsNeeded + " more time(s) to unlock the clue.";
        } else {
            lastMessage = "CLUE: " + clueText;
        }
    }

    @Override
    public void showHint() {
        lastMessage = "HINT: " + hintText;
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
    public String getRoomType() { return "Classroom"; }
}