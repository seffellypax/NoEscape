public class Player {
    private String name;
    private String course;
    private int progress;      // number of rooms cleared so far
    private int bonusSeconds;  // extra time added to the timer
    private int maxAttempts;   // max wrong attempts allowed per room
 
    public Player(String name, String course) {
        this.name = name;
        this.course = course;
        this.progress = 0;
 
        // Assign bonuses based on chosen course
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
 
    public void chooseCharacter(String course) {
        this.course = course;
    }
 
    // Getters
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

    // Setters
    public void setProgress(int progress) { 
        this.progress = progress; 
    }
    
    /** Reset player progress (useful for restarting the game). */
    public void reset(){
        this.progress = 0;
    }

}