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
    public void repairOxygenLeak() {
        decBatteryLevel();
        System.out.println("Repair Robot " + getIdNumber() + " is repairing the oxygen leak.");
    }
    

    public void repairRover() {
        decBatteryLevel();
        System.out.println("Repair Robot " + getIdNumber() + " is repairing the rover.");
    }
}
