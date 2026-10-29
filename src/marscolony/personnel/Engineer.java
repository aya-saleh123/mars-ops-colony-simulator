package marscolony.personnel;

import marscolony.Equipment.Equipment;
import marscolony.missions.Capability;

public class Engineer extends Human {
    public Engineer(String name, String idNumber) {
        super(name, idNumber, Capability.REPAIR);
    }

    public void repairEquipment(Equipment equipment) {//repair a single equipement and we wan't facility fe yom men el ayam
        this.incFatigueLevel();
        System.out.println("Engineer " + getName() + " is repairing equipment.");
        equipment.fixEquipment();
    }
    
    @Override 
    public void assignMission() {
        if (isAvailable() && getFatigueLevel() != FatigueLevel.HIGH) {
            System.out.println("Assigning mission to: " + getName());
            this.setAvailable(false);
        } else {
            System.out.println(getName() + " is currently unavailable for missions.");
        }
    }

    public void repairRover() {
        this.incFatigueLevel();
        System.out.println("Engineer " + getName() + " is repairing the rover.");
    }

    public void repairOxygenLeak() {
        this.incFatigueLevel();
        System.out.println("Engineer " + getName() + " is repairing the oxygen leak.");
    }
}
