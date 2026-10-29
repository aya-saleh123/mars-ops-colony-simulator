package marscolony.personnel;
import marscolony.MarsColony.MarsColony;
import marscolony.conditions.EnvironmentCondition;
import marscolony.missions.Capability;

public abstract class RoboticUnit extends Agent{
    private BatteryLevel batteryLevel;
    private boolean isOperational;

    public RoboticUnit(String idNumber, Capability skill) {
        super(idNumber, skill);
        this.batteryLevel = BatteryLevel.HIGH;
        this.isOperational = true;
        MarsColony.addRoboticUnit(this);
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

    public void decBatteryLevel() {
        if (batteryLevel == BatteryLevel.HIGH) {
            batteryLevel = BatteryLevel.MEDIUM;
        } else if (batteryLevel == BatteryLevel.MEDIUM) {
            batteryLevel = BatteryLevel.LOW;
        }
    }

    public void rechargeBattery() {
        if (batteryLevel == BatteryLevel.HIGH) {
            System.out.println("The robot " + this.getIdNumber() + " is already at high battery level, no need to recharge.");
            return;
        }
        System.out.println("The robot " + this.getIdNumber() + " is recharging. Please wait...");
        incBatteryLevel();
        incBatteryLevel();
        System.out.println("The robot finished charging, it's at high battery level.");
    }

    public void incBatteryLevel() {
        if (batteryLevel == BatteryLevel.MEDIUM) {
            batteryLevel = BatteryLevel.HIGH;
        } else if (batteryLevel == BatteryLevel.LOW) {
            batteryLevel = BatteryLevel.MEDIUM;
        }
    }

    public abstract void assignMission(EnvironmentCondition condition);

    @Override 
    public boolean canPerform(Capability requiredSkill) {
        return isAvailable() && getSkill() == requiredSkill && isOperational() && getBatteryLevel() != BatteryLevel.LOW;
    }

    @Override 
    public void justFinished() {
        setAvailable(true);
        if (batteryLevel == BatteryLevel.HIGH) {
            setBatteryLevel(BatteryLevel.MEDIUM);
        } else if (batteryLevel == BatteryLevel.MEDIUM) {
            setBatteryLevel(BatteryLevel.LOW);
        }
    }
}
