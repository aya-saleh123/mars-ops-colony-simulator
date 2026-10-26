package marscolony.personnel;

import marscolony.missions.Capability;

public class RepairRobot extends RoboticUnit {
    public RepairRobot(String idNumber) {
        super(idNumber, Capability.REPAIR);
    }

    public void repairEquipment() {
        // Implementation for repairing equipment
    }

    @Override
    public void assignMission() {
        if (isAvailable() && getBatteryLevel() != BatteryLevel.LOW) {
            System.out.println("Assigning repair mission to Repair Robot " + getIdNumber());
        } else {
            System.out.println("Repair Robot " + getIdNumber() + " is currently unavailable for missions.");
        }
    }
}
