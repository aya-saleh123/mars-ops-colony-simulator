package marscolony.personnel;

import marscolony.conditions.EnvironmentCondition;
import marscolony.missions.Capability;

public class Drone extends RoboticUnit {
    public Drone(String idNumber) {
        super(idNumber, Capability.AERIAL);
    }

    public void performReconnaissance() {
        // Implementation for performing reconnaissance
    }

    @Override
    public void assignMission(EnvironmentCondition condition) {
        if (isAvailable() && getBatteryLevel() != BatteryLevel.LOW) {
            System.out.println("Assigning reconnaissance mission to Drone " + getIdNumber());
        } else if (condition == EnvironmentCondition.COMMUNICATION_DISRUPTION) {
            System.out.println("Drone " + getIdNumber() + 
            " is currently unavailable for missions due to a communication disruption.");
        } else {
            System.out.println("Drone " + getIdNumber() + " is currently unavailable for missions.");
        }
    }
    
}
