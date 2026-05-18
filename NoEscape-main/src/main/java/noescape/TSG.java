package noescape;
public class TSG extends Room {

    private static final String PUZZLE =
            "Find the next number in the sequence:\n\n" +
            "   2,  6,  12,  20,  30,  ?\n\n" +
            "Enter your answer:";

    private static final String CLUE =
            "Clue: Look at the difference between each number.";

    private static final String HINT =
            "Hint: Differences go +4, +6, +8, +10, +12  ...";

    private static final String ANSWER = "42";

    // Calls the parent constructor — true means LOCKED
    public TSG() {
        super("TSG Lab", true);
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