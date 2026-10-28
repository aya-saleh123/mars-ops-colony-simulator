package marscolony.emergencies;
import java.util.ArrayList;
import java.util.List;
import marscolony.facilities.*;
import marscolony.personnel.*;
import marscolony.resources.ResourceType;
import marscolony.Equipment.*;

public class Fire extends Emergency{
    boolean unresolvedFire=false;
    List<Human>patients = new ArrayList<>();
    MedicalEmergency medicalEmergency;
public Fire(Laboratory lab, List<Agent> responders)
{
    super(lab, responders);
    lab.setOperationalStatus(false);
    lab.removeResource(ResourceType.OXYGEN, 50);
   medicalEmergency = new MedicalEmergency(lab, responders, patients);
    unresolvedFire=false;
    damageEquipment(lab);


}
 public void respondToEmergency() {
    boolean availableFirefighterFound = false;
    boolean availableMedicalOfficerFound = false;
    medicalEmergency.respondToEmergency();
    for(Agent a: getResponders())
   {
    if (a.isAvailable() && a.getClass() == Firefighter.class)
    {
        availableFirefighterFound = true;

      Firefighter firefighter = (Firefighter) a;
      firefighter.assignMission();
      firefighter.setAvailable(false);
      firefighter.extinguishFire();
    }
    if(a.isAvailable() && a.getClass()== MedicalOfficer.class)
    {
       availableMedicalOfficerFound = true;
      MedicalOfficer medicalOfficer = (MedicalOfficer) a;
      medicalOfficer.assignMission();
      medicalOfficer.setAvailable(false);
      medicalOfficer.medicalAssist(patients);
    
    }
  }
   if(!availableFirefighterFound && !availableMedicalOfficerFound)
    {
       unresolvedFire=true;
       System.out.println("No available Firefighter or Medical Officer to respond to the fire emergency.");
    }
    
 }

  private void damageEquipment(Laboratory lab) {
        for (Equipment equipment : lab.getEquipments()) {
            equipment.damageEquipment();
        }
    }


}