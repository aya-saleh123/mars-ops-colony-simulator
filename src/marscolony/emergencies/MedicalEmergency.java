package marscolony.emergencies;

import java.util.List;
import marscolony.facilities.Facilities;
import marscolony.facilities.FacilityStatus;
import marscolony.personnel.*;
import marscolony.resources.ResourceType;
import marscolony.missions.*;
import marscolony.MarsColony.MarsColony;

public class MedicalEmergency extends Emergency {
   List<Human> patients;
   boolean availableMedicalOfficerFound = false;
   boolean availableOperationalFacilityFound = false;
   boolean availableMedicalSuppliesFound = false;
   EmergencyStatus status = EmergencyStatus.IN_PROGRESS;

   public MedicalEmergency(Facilities facility, List<Human> patients) {
      super(facility);
      this.patients = patients;
   }

   @Override 
    public void findResponders() {
        for (Agent a : MarsColony.getAgents()) {
            if (a.getClass() == MedicalOfficer.class) {
                if (((MedicalOfficer) a).canPerform(Capability.MEDICAL)) {
                    this.addResponder(a);
                    a.setAvailable(false);
                    availableMedicalOfficerFound = true;
                    break;
                }
            }
        }
        if (!availableMedicalOfficerFound) {
            status = EmergencyStatus.UNRESOLVED;
            System.out.println("No available Medical Officer to respond to the medical emergency.");
            return;
        }
        respondToEmergency();
   }

   public void respondToEmergency() {
      for (Agent a : getResponders()) {

         if (a.isAvailable() && a.getClass() == MedicalOfficer.class && getFacility()
               .getFacilityOperationalStatus() == FacilityStatus.OPERATIONAL /* && medicalsupplies available */) {
            availableMedicalOfficerFound = true;
            availableOperationalFacilityFound = true;
            availableMedicalSuppliesFound = true;
            MedicalOfficer medicalOfficer = (MedicalOfficer) a;
            medicalOfficer.assignMission();
            medicalOfficer.setAvailable(false);
            medicalOfficer.medicalAssist(patients);
            
         }
      }

      if (!availableMedicalOfficerFound) {
         status = EmergencyStatus.UNRESOLVED;
         System.out.println("No available Medical Officer to respond to the medical emergency.");
      } else if (!availableOperationalFacilityFound) {
         status = EmergencyStatus.UNRESOLVED;
         System.out.println("No available operational facility to respond to the medical emergency.");
      } else if (!availableMedicalSuppliesFound) {
         status = EmergencyStatus.UNRESOLVED;
         System.out.println("No available medical supplies to respond to the medical emergency.");
      }
      status = EmergencyStatus.RESOLVED; //check later if this is correct, maybe we need to check if the patients are healthy or not
   }
}