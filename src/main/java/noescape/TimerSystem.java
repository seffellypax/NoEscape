package noescape;

/**
 * TimerSystem
 * Tracks the countdown timer for the game.
 * When time reaches zero, the loop resets.
 *
 * OOP: Encapsulation - timer state is hidden, only public methods exposed.
 */
public class TimerSystem {

    private long    startTime;
    private int     timeLimit;
    private boolean running;

    public TimerSystem(int timeLimitInSeconds) {
        this.timeLimit = timeLimitInSeconds;
        this.running   = false;
    }

    // Start the countdown
    public void start() {
        this.startTime = System.currentTimeMillis();
        this.running   = true;
    }

    // Stop the countdown
    public void stop() {
        this.running = false;
    }

    // Returns true if time has run out
    public boolean hasTimeExpired() {
        return running && getSecondsRemaining() <= 0;
    }

    // How many seconds are left
    public int getSecondsRemaining() {
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        return (int) Math.max(0, timeLimit - elapsed);
    }

    public boolean isRunning() {
        return running;
    }
}