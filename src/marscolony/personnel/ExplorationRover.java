package marscolony.personnel;
import marscolony.conditions.EnvironmentCondition;
import marscolony.missions.Capability;

public class ExplorationRover extends RoboticUnit {
    public ExplorationRover(String idNumber) {
        super(idNumber, Capability.SURFACE_TRAVEL);
    }

    public void exploreTerrain() {
        // Implementation for exploring terrain
    }
    
    @Override
    public void assignMission(EnvironmentCondition condition) {
        if(isAvailable() && getBatteryLevel() != BatteryLevel.LOW) {
            System.out.println("Assigning exploration mission to Exploration Rover " + getIdNumber());
        } else if (condition == EnvironmentCondition.DUST_STORM) {
            System.out.println("Exploration Rover " + getIdNumber() + 
            " is currently unavailable for missions due to a dust storm.");
        } else {
            System.out.println("Exploration Rover " + getIdNumber() + " is currently unavailable for missions.");
        }
    }
}
