package marscolony.personnel;

public class Drone extends RoboticUnit {
    public Drone(String idNumber) {
        super(idNumber);
    }

    public void performReconnaissance() {
        // Implementation for performing reconnaissance
    }

    @Override
    public void assignMission() {
        if (isAvailable() && getBatteryLevel() != BatteryLevel.LOW) {
            System.out.println("Assigning reconnaissance mission to Drone " + getIdNumber());
        } else {
            System.out.println("Drone " + getIdNumber() + " is currently unavailable for missions.");
        }
    }
}
