public interface IRoom {
    String name();
    boolean isLocked();
    boolean enter();
    void showPuzzle();
    void showClue();
    void showHint();
    void checkAnswer(String answer);
}
