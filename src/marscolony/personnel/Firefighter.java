package marscolony.personnel;

import marscolony.missions.Capability;

public class Firefighter extends Human {
    
    public Firefighter(String name, String idNumber, Capability skill) {
        super(name, idNumber, Capability.FIREFIGHTING);
    }

    public void extinguishFire() {
        // Implementation for extinguishing fire
    }

    @Override
    public void assignMission() {
        if (isAvailable() && getFatigueLevel() != FatigueLevel.HIGH) {
            System.out.println("Assigning firefighting mission to Firefighter: " + getName());
        } else {
            System.out.println("Firefighter " + getName() + " is currently unavailable for missions.");
        }
    }
}
