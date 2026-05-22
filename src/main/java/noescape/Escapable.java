package noescape;

/**
 * OOP: Abstraction — specifies WHAT a room does, not HOW it does it.
 */
public interface Escapable {
    String getName();
    String getRoomType();
    boolean isLocked();
    void unlock();
    boolean enter(Player player);
    void showPuzzle();
    void showClue();
    void showHint();
    void checkAnswer(String playerAnswer);
    boolean isSolved();
    int getAttempts();
    String getLastMessage();
    String getPuzzle();
}