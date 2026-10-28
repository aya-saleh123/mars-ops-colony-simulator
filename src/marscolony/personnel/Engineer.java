package marscolony.personnel;

import marscolony.Equipment.Equipment;
import marscolony.missions.Capability;

public class Engineer extends Human {
    public Engineer(String name, String idNumber) {
        super(name, idNumber, Capability.REPAIR);
    }

    public void repairEquipment(Equipment equipment) {//repair a single equipement and we wan't facility fe yom men el ayam
        equipment.fixEquipment();
    }
    
    @Override 
    public void assignMission() {
        if (isAvailable() && getFatigueLevel() != FatigueLevel.HIGH) {
            System.out.println("Assigning repair mission to Engineer: " + getName());
        } else {
            System.out.println("Engineer " + getName() + " is currently unavailable for missions.");
        }
    }

    public void repairOxygenLeak() {
        
    }
}
