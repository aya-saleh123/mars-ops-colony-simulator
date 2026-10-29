package marscolony.personnel;
import java.util.ArrayList;
import java.util.List;
import marscolony.missions.Capability;

public class MedicalOfficer extends Human {

    List<Human> patients=new ArrayList<>();
    public MedicalOfficer(String name, String idNumber, Capability skill) {
        super(name, idNumber, Capability.MEDICAL);
    }

    public void medicalAssist(Human patient) {
        System.out.println("Medical Officer " + getName() + " is providing medical assistance.");
        incHealthStat(patient);
    }

    private void incHealthStat(Human patient) {
       System.out.println("Medical Officer " + getName() + " is treating " + patient.getName());
       patient.improveHealth();
       incFatigueLevel(); 
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
