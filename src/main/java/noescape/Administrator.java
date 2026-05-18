public class Administrator {
 
    private String message;
    private boolean isOverridden;
 
    public Administrator() {
        this.message = " ";
        this.isOverridden = false;
    }
 
    // Send a message that appears in the game log
    public void sendMessage(String msg) {
        this.message = msg;
    }
 
    // Reset the game state
    public void resetGame() {
        this.isOverridden = false;
    }
 
    public String getMessage() {
         return message; 
    }
    public boolean isOverridden() { 
        return isOverridden; 
    }
}