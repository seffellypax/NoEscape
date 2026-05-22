package noescape;

/**
 * Countdown timer backed by wall-clock time rather than tick counts,
 * making it immune to Swing timer drift.
 *
 * OOP: Encapsulation — timer state is fully hidden; only elapsed/remaining
 *      time and lifecycle methods are exposed publicly.
 */
public class TimerSystem {

    private long    startTimeMillis;
    private final int timeLimitSeconds;
    private boolean isRunning;

    public TimerSystem(int timeLimitSeconds) {
        this.timeLimitSeconds = timeLimitSeconds;
        this.isRunning        = false;
    }

    /** Starts the countdown from the current wall-clock time. */
    public void start() {
        this.startTimeMillis = System.currentTimeMillis();
        this.isRunning       = true;
    }

    /** Stops the countdown. Subsequent calls to {@link #hasTimeExpired()} return {@code false}. */
    public void stop() {
        this.isRunning = false;
    }

    /** Returns {@code true} when the timer is running and no seconds remain. */
    public boolean hasTimeExpired() {
        return isRunning && getSecondsRemaining() <= 0;
    }

    /** Returns the number of whole seconds remaining, clamped to zero. */
    public int getSecondsRemaining() {
        long elapsedSeconds = (System.currentTimeMillis() - startTimeMillis) / 1000;
        return (int) Math.max(0, timeLimitSeconds - elapsedSeconds);
    }

    public boolean isRunning() { return isRunning; }
}