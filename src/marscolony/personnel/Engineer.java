package marscolony.personnel;

public class Engineer extends Human {
    public Engineer(String name, String idNumber) {
        super(name, idNumber);
    }

    public void repairEquipment() {
        // Implementation for repairing equipment
    }
    
    @Override 
    public void assignMission() {
        if (isAvailable() && getFatigueLevel() != FatigueLevel.HIGH) {
            System.out.println("Assigning repair mission to Engineer: " + getName());
        } else {
            System.out.println("Engineer " + getName() + " is currently unavailable for missions.");
        }
    }
}
