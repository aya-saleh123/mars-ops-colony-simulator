package marscolony.personnel;
import java.util.ArrayList;
import java.util.List;
import marscolony.missions.Capability;

public class MedicalOfficer extends Human {

    List<Human> patients=new ArrayList<>();
    public MedicalOfficer(String name, String idNumber, Capability skill) {
        super(name, idNumber, Capability.MEDICAL);
    }

    public void medicalAssist(List<Human> patients) {
        // Implementation for assisting with medical emergencies
        System.out.println("Medical Officer " + getName() + " is providing medical assistance.");
        for (Human patient : patients) {
            incHealthStat(patient);
        }
    }

    public void incHealthStat(Human patient) {
       System.out.println("Medical Officer " + getName() + " is increasing the health status of " + patient.getName());
       if(patient.getFatigueLevel() == FatigueLevel.LOW) {
           patient.setFatigueLevel(FatigueLevel.MEDIUM);
       } else if(patient.getFatigueLevel() == FatigueLevel.MEDIUM) {
           patient.setFatigueLevel(FatigueLevel.HIGH);
       } else {
          setHealthy(true);
       }
        
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
