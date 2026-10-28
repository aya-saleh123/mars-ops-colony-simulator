package marscolony.emergencies;
import java.util.List;
import marscolony.facilities.Facilities;
import marscolony.facilities.FacilityStatus;
import marscolony.personnel.*;

public class MedicalEmergency extends Emergency{
List<Human> patients;
 boolean  unresolvedFire=false;
public MedicalEmergency(Facilities facility, List<Agent> responders, List<Human> patients)
{
    super(facility, responders);
    unresolvedFire=false;
    this.patients = patients;
}
 public void respondToEmergency() {
       boolean availableMedicalOfficerFound = false;
       boolean availableOperationalFacilityFound = false;
       boolean availableMedicalSuppliesFound = false;  
    for(Agent a: getResponders())
   {
    
    if(a.isAvailable() && a.getClass()== MedicalOfficer.class && getFacility().getFacilityOperationalStatus() == FacilityStatus.OPERATIONAL /*&& medicalsupplies available */)
    {
       availableMedicalOfficerFound = true;
       availableOperationalFacilityFound = true;
       availableMedicalSuppliesFound = true; 
      MedicalOfficer medicalOfficer = (MedicalOfficer) a;
      medicalOfficer.assignMission();
      medicalOfficer.setAvailable(false);
      medicalOfficer.medicalAssist(patients);
    
    }
    }
   if(!availableMedicalOfficerFound )
    {
       unresolvedFire=true;
       System.out.println("No available Medical Officer to respond to the medical emergency.");
    }
    else if(!availableOperationalFacilityFound)
    {
       unresolvedFire=true;
       System.out.println("No available operational facility to respond to the medical emergency.");
    }
    else if(!availableMedicalSuppliesFound)
    {
       unresolvedFire=true;
       System.out.println("No available medical supplies to respond to the medical emergency.");
    }

}
}