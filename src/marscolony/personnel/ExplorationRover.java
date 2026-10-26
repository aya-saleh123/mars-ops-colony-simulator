package marscolony.personnel;

public class ExplorationRover extends RoboticUnit {
    public ExplorationRover(String idNumber) {
        super(idNumber);
    }

    public void exploreTerrain() {
        // Implementation for exploring terrain
    }
    
    @Override
    public void assignMission() {
        if(isAvailable() && getBatteryLevel() != BatteryLevel.LOW) {
            System.out.println("Assigning exploration mission to Exploration Rover " + getIdNumber());
        } else {
            System.out.println("Exploration Rover " + getIdNumber() + " is currently unavailable for missions.");
        }
    }
}
