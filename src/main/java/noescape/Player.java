package noescape;

/**
 * OOP: Encapsulation — all fields are private; state is exposed through getters/setters only.
 */
public class Player {
    private final String name;
    private final String course;
    private int solvedRoomCount;
    private final int bonusSeconds;
    private final int maxAttemptsPerRoom;

    public Player(String name, String course) {
        this.name = name;
        this.course = course;
        this.solvedRoomCount = 0;

        if (course.contains("Computer Science")) {
            this.bonusSeconds = 20;
            this.maxAttemptsPerRoom = 3;
        } else if (course.contains("Nursing")) {
            this.bonusSeconds = 0;
            this.maxAttemptsPerRoom = 5;
        } else {
            this.bonusSeconds = 0;
            this.maxAttemptsPerRoom = 3;
        }
    }

    public String getName() { 
        return name; 
    }
    public String getCourse() { 
        return course;
    }
    public int getProgress() { 
        return solvedRoomCount; 
    }
    public int getBonusSeconds() { 
        return bonusSeconds; 
    }
    public int getMaxAttempts() { 
        return maxAttemptsPerRoom; 
    }
    public void setProgress(int solvedRoomCount) {
        this.solvedRoomCount = solvedRoomCount;
    }
    public void reset() {
        this.solvedRoomCount = 0;
    }
}