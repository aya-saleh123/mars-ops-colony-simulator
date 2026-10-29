package marscolony.emergencies;

import java.util.ArrayList;
import java.util.List;
import marscolony.MarsColony.MarsColony;
import marscolony.facilities.FacilityStatus;
import marscolony.facilities.Facilities;
import marscolony.facilities.Habitat;
import marscolony.missions.Capability;
import marscolony.missions.FailureReason;
import marscolony.personnel.Agent;
import marscolony.personnel.FatigueLevel;
import marscolony.personnel.Human;
import marscolony.personnel.MedicalOfficer;
import marscolony.resources.ResourceType;

public class MedicalEmergency extends Emergency {
   private final List<Human> patients;
   private final Habitat medicalHabitat;
   private MedicalOfficer medicalOfficer;

   public MedicalEmergency(Facilities incidentFacility, Habitat medicalHabitat, List<Human> patients) {
      super(incidentFacility);
      this.medicalHabitat = medicalHabitat;
      if (patients == null) {
         this.patients = new ArrayList<>();
      }
      else {
         this.patients = new ArrayList<>(patients);
      }
   }

   @Override
   public void findResponders() {
      medicalOfficer = null;

      if (patients.isEmpty()) {
         setReason(FailureReason.NONE);
         setStatus(EmergencyStatus.RESOLVED);
         return;
      }

      for (Agent agent : MarsColony.getHumans()) {
         if (agent.canPerform(Capability.MEDICAL)) {
            medicalOfficer = (MedicalOfficer) agent;
            break;
         }
      }

      if (medicalOfficer == null) {
         setReason(FailureReason.UNAVAILABLE_PERSONNEL);
         setStatus(EmergencyStatus.UNRESOLVED);
         return;
      }

      if (medicalHabitat == null || medicalHabitat.getFacilityOperationalStatus() != FacilityStatus.OPERATIONAL) {
         setReason(FailureReason.UNAVAILABLE_FACILITIES);
         setStatus(EmergencyStatus.UNRESOLVED);
         return;
      }

      int supplyCost = patients.size();

      if (medicalHabitat.getResourceCount(ResourceType.MEDICAL_SUPPLIES) < supplyCost) {
         setReason(FailureReason.INSUFFICIENT_RESOURCES);
         setStatus(EmergencyStatus.UNRESOLVED);
         return;
      }

      dispatchResponder(medicalOfficer);
      respondToEmergency();
   }

   @Override
   public void respondToEmergency() {
     if (medicalOfficer == null || medicalOfficer.canPerform(Capability.MEDICAL)) {
         setReason(FailureReason.UNAVAILABLE_PERSONNEL);
         setStatus(EmergencyStatus.UNRESOLVED);
         return;
      }
      if (medicalHabitat == null || medicalHabitat.getFacilityOperationalStatus() != FacilityStatus.OPERATIONAL) {
         setReason(FailureReason.UNAVAILABLE_FACILITIES);
         setStatus(EmergencyStatus.UNRESOLVED);
         return;
      }

      int supplyCost = patients.size();

      if (medicalHabitat.getResourceCount(ResourceType.MEDICAL_SUPPLIES) < supplyCost) {
         setReason(FailureReason.INSUFFICIENT_RESOURCES);
         setStatus(EmergencyStatus.UNRESOLVED);
         return;
      }
      medicalHabitat.removeResource(ResourceType.MEDICAL_SUPPLIES, supplyCost);

      for (Human patient : patients) {
         patient.decFatigueLevel();
         medicalOfficer.medicalAssist(patient);
      }

      setStatus(EmergencyStatus.RESOLVED);
      setReason(FailureReason.NONE);

      System.out.println("Medical emergency resolved. Medical supplies used: " + supplyCost);
      releaseResponders();
   }
}