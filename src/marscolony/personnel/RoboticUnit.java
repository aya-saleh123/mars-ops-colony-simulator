package marscolony.personnel;

import marscolony.missions.Capability;

public abstract class RoboticUnit {
    private String idNumber;
    private BatteryLevel batteryLevel;
    private String location;
    private boolean isAvailable;
    private boolean isOperational;
    private Capability skill;

    public RoboticUnit(String idNumber) {
        if (idNumber == null || idNumber.isEmpty()) {
            throw new IllegalArgumentException("ID number cannot be null or empty.");
        }
        this.idNumber = idNumber;
        this.batteryLevel = BatteryLevel.HIGH;
        this.location = "Base";
        this.isAvailable = true;
        this.isOperational = true;
        this.skill = null;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public BatteryLevel getBatteryLevel() {
        return batteryLevel;
    }

    public String getLocation() {
        return location;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public boolean isOperational() {
        return isOperational;
    }

    public Capability getSkill() {
        return skill;
    }

    public void setBatteryLevel(BatteryLevel batteryLevel) {
        if (batteryLevel == null) {
            throw new IllegalArgumentException("Battery level cannot be null.");
        }
        this.batteryLevel = batteryLevel;
    }

    public void setLocation(String location) {
        if (location == null || location.isEmpty()) {
            throw new IllegalArgumentException("Location cannot be null or empty.");
        }
        this.location = location;
    }

    public void setAvailable(boolean available) {
        if (!available && !isAvailable) {
            throw new IllegalStateException("Already unavailable. Cannot set available to false again.");
        }
        if (available && isAvailable) {
            throw new IllegalStateException("Already available. Cannot set available to true again.");
        }
        this.isAvailable = available;
    }

    public void setOperational(boolean operational) {
        if (!operational && !isOperational) {
            throw new IllegalStateException("Already inoperable. Cannot set operational to false again.");
        }
        if (operational && isOperational) {
            throw new IllegalStateException("Already operational. Cannot set operational to true again.");
        }
        this.isOperational = operational;
    }

    public void setSkill(Capability skill) {
        if (skill == null) {
            throw new IllegalArgumentException("Skill cannot be null.");
        }
        this.skill = skill;
    }

    public abstract void assignMission();
}
