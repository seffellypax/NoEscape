package noescape;
public class Player {

    private String name;
    private String course;
    private int progress;      // number of rooms cleared
    private int bonusSeconds;  // extra time added to the timer
    private int maxAttempts;   // max wrong attempts allowed per room

    public Player(String name, String course) {
        this.name = name;
        this.course = course;
        this.progress = 0;

        // Assign bonuses based on chosen course
        if (course.contains("Computer Science")) {
            this.bonusSeconds = 20;
            this.maxAttempts = 3;
        } else if (course.contains("Nursing")) {
            this.bonusSeconds = 0;
            this.maxAttempts = 5;
        } else {
            this.bonusSeconds = 0;
            this.maxAttempts = 3;
        }
    }

    // Called when the player selects their course on the setup screen
    public void chooseCharacter(String course) {
        this.course = course;
    }

    // Resets progress (used when restarting the game)
    public void reset() {
        this.progress = 0;
    }

    //Getters
    public String getName() { 
        return name; 
    }
    public String getCourse() { 
        return course; 
    }
    public int getProgress() { 
        return progress; 
    }
    public int getBonusSeconds() { 
        return bonusSeconds; 
    }
    public int getMaxAttempts() { 
        return maxAttempts; 
    }

    //Setters 
    public void setProgress(int progress) { 
        this.progress = progress; 
    }
}