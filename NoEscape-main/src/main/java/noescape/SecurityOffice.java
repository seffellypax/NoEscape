package noescape;
public class SecurityOffice extends Room {

    private static final String PUZZLE =
            "The keypad needs a 4-digit code.\n\n" +
            "'Two score and two' + the 7th Fibonacci number.\n\n" +
            "Enter the code:";

    private static final String CLUE =
            "Clue: The answer is a significant year.";

    private static final String HINT =
            "Hint: 7th Fibonacci = 13. So:  2008 + 13 = 2021.";

    private static final String ANSWER = "2021";

    // Calls the parent constructor — true means LOCKED
    public SecurityOffice() {
        super("Security Office", true);
    }

    @Override
    public String getPuzzle() {
        return PUZZLE;
    }

    @Override
    public String getClue() {
        return CLUE;
    }

    @Override
    public String getHint() {
        return HINT;
    }

    @Override
    public boolean checkAnswer(String answer) {
        return answer.trim().equalsIgnoreCase(ANSWER);
    }
}