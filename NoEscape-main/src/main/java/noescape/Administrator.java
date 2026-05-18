package noescape;
public class Administrator {

    private String  message;
    private boolean isOverridden;

    public Administrator() {
        this.message = " ";
        this.isOverridden = false;
    }

    // Post a message to be shown in the game log
    public void sendMessage(String message) {
        this.message = message;
    }

    // Trigger a game reset (called by the restart button)
    public void resetGame() {
        this.isOverridden = false;
    }

    public String  getMessage(){ 
        return message; 
    }
    public boolean isOverridden(){ 
        return isOverridden; 
    }
}