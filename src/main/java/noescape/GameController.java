package noescape;

/**
 * OOP: Encapsulation — internal state hidden behind a clean public API.
 */
public class GameController {

    private String currentMessage;
    private boolean isLoopOverridden;

    public GameController() {
        this.currentMessage = " ";
        this.isLoopOverridden = false;
    }

    public void sendMessage(String message) {
        this.currentMessage = message;
    }

    public void resetGame() {
        this.isLoopOverridden = false;
        this.currentMessage = " ";
    }

    public void overrideLoop() {
        this.isLoopOverridden = true;
        this.currentMessage = "LOOP OVERRIDE: You are free.";
    }

    public String getMessage() { 
        return currentMessage; 
    }
    public boolean isLoopOverridden() { 
        return isLoopOverridden; 
    }
}