package noescape;

/**
 * Controller
 * The mysterious presence that controls the loop.
 * Sends messages to the player and overrides the loop on win.
 *
 * OOP: Encapsulation - private fields, public methods only.
 */
public class Controller {

    private String  message;
    private boolean isOverridden;

    public Controller() {
        this.message      = " ";
        this.isOverridden = false;
    }

    // Send a message shown in the game
    public void sendMessage(String msg) {
        this.message = msg;
    }

    // Reset state when game restarts
    public void resetGame() {
        this.isOverridden = false;
        this.message      = " ";
    }

    // Called when the player escapes the loop
    public void overrideLoop() {
        this.isOverridden = true;
        this.message      = "LOOP OVERRIDE: You are free.";
    }

    public String  getMessage()   { return message;      }
    public boolean isOverridden() { return isOverridden; }
}