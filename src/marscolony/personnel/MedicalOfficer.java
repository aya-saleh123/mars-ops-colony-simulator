package marscolony.personnel;

public class MedicalOfficer extends Human {
    public MedicalOfficer(String name, String idNumber) {
        super(name, idNumber);
    }

    public void respondToEmergency() {
        // Implementation for responding to emergencies
    }

    @Override
    public void assignMission() {
        if (isAvailable() && getFatigueLevel() != FatigueLevel.HIGH) {
            System.out.println("Assigning medical mission to Medical Officer: " + getName());
        } else {
            System.out.println("Medical Officer " + getName() + " is currently unavailable for missions.");
        }
    }
    
}
