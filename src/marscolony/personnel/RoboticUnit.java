package marscolony.personnel;

import marscolony.missions.Capability;

public abstract class RoboticUnit extends Agent{
    private BatteryLevel batteryLevel;
    private boolean isOperational;

    public RoboticUnit(String idNumber, Capability skill) {
        super(idNumber, skill);
        this.batteryLevel = BatteryLevel.HIGH;
        this.isOperational = true;
    }

    public BatteryLevel getBatteryLevel() {
        return batteryLevel;
    }

    public boolean isOperational() {
        return isOperational;
    }

    public void setBatteryLevel(BatteryLevel batteryLevel) {
        if (batteryLevel == null) {
            throw new IllegalArgumentException("Battery level cannot be null.");
        }
        this.batteryLevel = batteryLevel;
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

    public abstract void assignMission();
}
