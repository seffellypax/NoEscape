package noescape;

/**
 * OOP: Encapsulation — timer state is fully hidden; only elapsed/remaining
 *      time and lifecycle methods are exposed publicly.
 */
public class TimerSystem {
    private long startTimeMillis;
    private final int timeLimitSeconds;
    private boolean isRunning;

    public TimerSystem(int timeLimitSeconds) {
        this.timeLimitSeconds = timeLimitSeconds;
        this.isRunning = false;
    }

    public void start() {
        this.startTimeMillis = System.currentTimeMillis();
        this.isRunning = true;
    }

    public void stop() {
        this.isRunning = false;
    }

    public boolean hasTimeExpired() {
        return isRunning && getSecondsRemaining() <= 0;
    }

    public int getSecondsRemaining() {
        long elapsedSeconds = (System.currentTimeMillis() - startTimeMillis) / 1000;
        return (int) Math.max(0, timeLimitSeconds - elapsedSeconds);
    }

    public boolean isRunning() { return isRunning; }
}