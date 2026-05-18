package noescape;
public interface RoomBehavior {

    String getName();                   // returns the room's name
    boolean isLocked();                  // checks if the room is locked
    void unlock();                    // unlocks the room
    boolean enter(Player player);        // returns true if the player can enter
    String getPuzzle();                 // returns the puzzle text
    String getClue();                   // returns the clue text
    String getHint();                   // returns the hint text
    boolean checkAnswer(String answer);  // returns true if the answer is correct
}