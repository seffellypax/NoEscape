package noescape;

/**
 * Location
 * Represents a physical campus area containing a room.
 *
 * OOP: Encapsulation - wraps a room with building metadata.
 */
public class Location {

    private final String       buildingName;
    private final RoomBehavior room;

    public Location(String buildingName, RoomBehavior room) {
        this.buildingName = buildingName;
        this.room         = room;
    }

    public String       getBuildingName() { return buildingName; }
    public RoomBehavior getRoom()         { return room;         }

    @Override
    public String toString() {
        return buildingName + " - " + room.getName();
    }
}