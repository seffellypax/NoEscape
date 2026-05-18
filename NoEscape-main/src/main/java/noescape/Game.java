package noescape;
public class Game {

    // ── Has-a Relationships ──────────────────────
    private Player player;
    private TimerSystem timerSystem;
    private Administrator admin;
    private Room[] rooms;        // Polymorphism: stored as Room[]

    // ── Game State ───────────────────────────────
    private int currentRoomIndex;
    private int wrongAttempts;
    private boolean gameOver;
    private boolean gameWon;

    // Base timer: 5 minutes (course bonus is added on top)
    private static final int BASE_TIME_SECONDS = 300;

    // ── Constructor ──────────────────────────────
    public Game(Player player) {
        this.player = player;
        this.admin = new Administrator();

        // Polymorphism: Library, TSG, SecurityOffice stored as Room[]
        this.rooms = new Room[] {
            new Library(),           // Room 1 — always unlocked
            new TSG(),               // Room 2 — locked until Room 1 solved
            new SecurityOffice()     // Room 3 — locked until Room 2 solved
        };

        resetState();
    }

    // ── startGame() ──────────────────────────────
    // Sets up the timer and sends the welcome message.
    // Called once when the game begins.
    public void startGame() {
        int totalTime = BASE_TIME_SECONDS + player.getBonusSeconds();
        this.timerSystem = new TimerSystem(totalTime);
        this.timerSystem.start();

        admin.sendMessage("Welcome, " + player.getName() + "! Good luck.");
    }

    // ── submitAnswer() ───────────────────────────
    // Called by the GUI when the player presses Submit.
    // Returns true if the answer was correct.
    public boolean submitAnswer(String answer) {
        Room currentRoom = getCurrentRoom();

        if (currentRoom.checkAnswer(answer)) {
            // Correct — advance to the next room
            player.setProgress(currentRoomIndex + 1);
            wrongAttempts = 0;
            currentRoomIndex++;

            // Unlock the next room if one exists
            if (currentRoomIndex < rooms.length) {
                rooms[currentRoomIndex].unlock();
            }

            // Check if all rooms are cleared
            if (currentRoomIndex >= rooms.length) {
                gameWon = true;
            }

            return true;

        } else {
            // Wrong — use up one attempt
            wrongAttempts++;
            if (wrongAttempts >= player.getMaxAttempts()) {
                gameOver = true;
            }
            return false;
        }
    }

    // ── checkTimer() ─────────────────────────────
    // Called by the GUI every second to check if time is up.
    public void checkTimer() {
        if (timerSystem.hasTimeExpired()) {
            gameOver = true;
        }
    }

    // ── resetState() ─────────────────────────────
    // Resets all game state back to the beginning.
    // Used on construction and when the player restarts.
    public void resetState() {
        this.currentRoomIndex = 0;
        this.wrongAttempts = 0;
        this.gameOver = false;
        this.gameWon = false;
        this.player.reset();
    }

    // ── Convenience Getters ──────────────────────

    // Returns the room the player is currently in
    public Room getCurrentRoom() {
        return rooms[currentRoomIndex];
    }
    // Returns how many attempts the player has left in this room
    public int getAttemptsLeft() {
        return player.getMaxAttempts() - wrongAttempts;
    }
    // Returns the total time (base + bonus) for display
    public int getTotalTime() {
        return BASE_TIME_SECONDS + player.getBonusSeconds();
    }

    // ── Getters (read by the GUI) ─────────────────
    public Player getPlayer() { 
        return player; 
    }
    public TimerSystem getTimerSystem() { 
        return timerSystem; 
    }
    public Administrator getAdmin() { 
        return admin; 
    }
    public Room[] getRooms() { 
        return rooms; 
    }
    public int getCurrentRoomIndex() { 
        return currentRoomIndex; 
    }
    public int getWrongAttempts() { 
        return wrongAttempts; 
    }
    public boolean isGameOver() { 
        return gameOver; 
    }
    public boolean isGameWon() { 
        return gameWon; 
    }
}