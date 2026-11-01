package marscolony.personnel;
import marscolony.Equipment.Equipment;
import marscolony.Equipment.EquipmentType;
import marscolony.conditions.EnvironmentCondition;
import marscolony.missions.Capability;

public class ExplorationRover extends RoboticUnit {
    private Equipment equipment;
    public ExplorationRover(String idNumber) {
        super(idNumber, Capability.SURFACE_TRAVEL);
        equipment = new Equipment(EquipmentType.EXPLORATION_EQUIPMENT);
    }

    public void exploreTerrain() {
        // Implementation for exploring terrain
    }
    
    @Override
    public void assignMission(EnvironmentCondition condition) {
         if (condition == EnvironmentCondition.DUST_STORM) {
            System.out.println("Exploration Rover " + getIdNumber() + 
            " is currently unavailable for missions due to a dust storm.");
        } 
       else  if(isAvailable() && getBatteryLevel() != BatteryLevel.LOW) {
            System.out.println("Assigning exploration mission to Exploration Rover " + getIdNumber());
            setAvailable(false);  }

            else {
            System.out.println("Exploration Rover " + getIdNumber() + " is currently unavailable for missions.");
        }
    }
}
