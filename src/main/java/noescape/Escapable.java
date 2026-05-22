package noescape;

/**
 * Defines the contract every room in the escape game must fulfill.
 *
 * This is the "Flyable" equivalent of this game — it declares WHAT a room does,
 * not HOW. Each concrete room class implements these methods differently,
 * producing distinct gameplay behaviour through polymorphism.
 *
 * Analogy:
 *   Flyable          → Escapable
 *   Bird (abstract)  → BaseRoom (abstract)
 *   Eagle            → Classroom    (strict — clue locked behind failures)
 *   Penguin          → LibraryRoom  (research — hints cost an attempt)
 *   Parrot           → TsgRoom      (technical — case-sensitive answers)
 *   Owl              → SecurityOfficeRoom (lockdown — hint re-locks the room)
 *
 * OOP: Abstraction — specifies WHAT a room does, not HOW it does it.
 */
public interface Escapable {

    // --- Identity & access control ---
    String  getName();
    String  getRoomType();
    boolean isLocked();
    void    unlock();

    // --- Gameplay actions (each room overrides these differently) ---
    boolean enter(Player player);
    void    showPuzzle();

    /**
     * Reveals a clue. Implementations may impose conditions
     * (e.g. require a minimum number of failed attempts first).
     */
    void    showClue();

    /**
     * Reveals a hint. Implementations may apply a penalty
     * (e.g. consuming an attempt, or re-locking the room).
     */
    void    showHint();

    /**
     * Checks the player's answer. Implementations may apply
     * custom matching rules (e.g. case-sensitive comparison).
     */
    void    checkAnswer(String playerAnswer);

    // --- State queries ---
    boolean isSolved();
    int     getAttempts();
    String  getLastMessage();
    String  getPuzzle();
}