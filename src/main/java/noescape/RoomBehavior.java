package noescape;

/**
 * RoomBehavior Interface
 * Defines the contract every room must follow.
 *
 * OOP: Abstraction - defines WHAT a room does, not HOW.
 */
public interface RoomBehavior {

    // Identity
    String  getName();
    boolean isLocked();
    void    unlock();

    // Actions
    boolean enter(Player player);
    void    showPuzzle();
    void    showClue();
    void    showHint();
    void    checkAnswer(String answer);

    // State
    boolean isSolved();
    int     getAttempts();
    String  getLastMessage();
    String  getPuzzle();
    String  getRoomType();
}