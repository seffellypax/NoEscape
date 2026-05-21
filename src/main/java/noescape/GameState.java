package noescape;

/**
 * GameState Enum
 * Tracks which screen the game is currently showing.
 *
 * OOP: Encapsulation - state is a fixed set of constants.
 */
public enum GameState {
    ENTER_NAME,    // Player types their name
    CHOOSE_COURSE, // Player picks CS or Nursing
    SPLASH,        // Animated intro before game starts
    PLAYING,       // Active gameplay
    WIN,           // Player solved all rooms
    LOOP           // Time ran out
}