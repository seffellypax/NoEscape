package noescape;

/**
 * OOP: Encapsulation — building metadata is wrapped alongside a room reference.
 */
public class Location {
    private final String buildingName;
    private final Escapable room;

    public Location(String buildingName, Escapable room) {
        this.buildingName = buildingName;
        this.room = room;
    }

    public String getBuildingName() { 
        return buildingName; 
    }
    public Escapable getRoom() { 
        return room; 
    }

    @Override
    public String toString() {
        return buildingName + " - " + room.getName();
    }
}