package marscolony.personnel;

import marscolony.missions.Capability;

public class Scientist extends Human {
    public Scientist(String name, String idNumber) {
        super(name, idNumber, Capability.SCIENCE);
    }

    public void conductResearch() {
        // Implementation for conducting research
    }

    @Override
    public void assignMission() {
        if (isAvailable() && getFatigueLevel() != FatigueLevel.HIGH) {
            System.out.println("Assigning research mission to Scientist: " + getName());
        } else {
            System.out.println("Scientist " + getName() + " is currently unavailable for missions.");
        }
    }
}
