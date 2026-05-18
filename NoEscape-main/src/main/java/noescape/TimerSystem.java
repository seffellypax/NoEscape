package noescape;
public class TimerSystem {
 
    private long startTime;
    private int timeLimit;  // total seconds allowed
    private boolean running;
 
    public TimerSystem(int timeLimitInSeconds) {
        this.timeLimit = timeLimitInSeconds;
        this.running   = false;
    }
 
    public void start() {
        this.startTime = System.currentTimeMillis();
        this.running   = true;
    }
 
    public boolean hasTimeExpired() {
        return running && getSecondsRemaining() <= 0;
    }
 
    public int getSecondsRemaining() {
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        return (int) Math.max(0, timeLimit - elapsed);
    }
 
    public boolean isRunning() {
         return running; 
    }
}