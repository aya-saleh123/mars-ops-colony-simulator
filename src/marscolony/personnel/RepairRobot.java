package marscolony.personnel;

import marscolony.conditions.EnvironmentCondition;
import marscolony.missions.Capability;

public class RepairRobot extends RoboticUnit {
    public RepairRobot(String idNumber) {
        super(idNumber, Capability.REPAIR);
    }

    public void repairEquipment() {
        // Implementation for repairing equipment
    }

    

    public void repairRover() {
        decBatteryLevel();
        System.out.println("Repair Robot " + getIdNumber() + " is repairing the rover.");
    }

    @Override
    public void assignMission(EnvironmentCondition condition) {
        if (isAvailable() && getBatteryLevel() != BatteryLevel.LOW) {
            System.out.println("Assigning repair mission to Repair Robot " + getIdNumber());
            this.setAvailable(false);
        } else {
            System.out.println("Repair Robot " + getIdNumber() + " is currently unavailable for missions.");
        }
    }
}
