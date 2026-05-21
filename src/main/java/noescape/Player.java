package noescape;

/**
 * Player
 * Represents the student trapped in the campus loop.
 *
 * OOP: Encapsulation - all fields are private with getters and setters.
 */
public class Player {

    private String name;
    private String course;
    private int    progress;
    private int    bonusSeconds;
    private int    maxAttempts;

    public Player(String name, String course) {
        this.name     = name;
        this.course   = course;
        this.progress = 0;

        // Assign bonuses based on course
        if (course.contains("Computer Science")) {
            this.bonusSeconds = 20;
            this.maxAttempts  = 3;
        } else if (course.contains("Nursing")) {
            this.bonusSeconds = 0;
            this.maxAttempts  = 5;
        } else {
            this.bonusSeconds = 0;
            this.maxAttempts  = 3;
        }
    }

    // Getters
    public String getName()         { return name;         }
    public String getCourse()       { return course;       }
    public int    getProgress()     { return progress;     }
    public int    getBonusSeconds() { return bonusSeconds; }
    public int    getMaxAttempts()  { return maxAttempts;  }

    // Setters
    public void setProgress(int progress) { this.progress = progress; }

    // Reset progress for restart
    public void reset() {
        this.progress = 0;
    }
}