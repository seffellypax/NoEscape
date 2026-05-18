package noescape;

public class Library extends Room {

    private static final String PUZZLE =
        "Decode the Caesar cipher (shift 3):\n\n" +
        "   HVFDSH  WKH  URRP\n\n" +
        "What does it say? (no spaces)";

    private static final String CLUE =
        "Clue: Every letter is shifted 3 places forward in the alphabet.";

    private static final String HINT =
        "Hint: H = E,  V = S,  F = C ...";

    private static final String ANSWER = "ESCAPETHEROOM";

    public Library() {
        super("Library", false);
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