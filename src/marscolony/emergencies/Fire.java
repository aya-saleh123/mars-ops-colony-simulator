package marscolony.emergencies;
import java.util.ArrayList;
import java.util.List;
import marscolony.facilities.*;
import marscolony.personnel.*;
import marscolony.resources.ResourceType;
import marscolony.Equipment.*;
import marscolony.missions.*;
import marscolony.MarsColony.MarsColony;

public class Fire extends Emergency {
    EmergencyStatus status = EmergencyStatus.IN_PROGRESS;
    List<Human> patients = new ArrayList<>();
    MedicalEmergency medicalEmergency;
    boolean availableFirefighterFound = false;
    boolean availableMedicalOfficerFound = false;
    Habitat medicalHabitat;
    public Fire(Laboratory lab) {
        super(lab);
        lab.setOperationalStatus(FacilityStatus.UNAVAILABLE);
        int lost = lab.removeUpTo(ResourceType.OXYGEN, 50);
        System.out.println("Fire consumed " + lost + " oxygen in " + lab.getLocationName());
        medicalEmergency = new MedicalEmergency(lab, medicalHabitat, patients);
        damageEquipment(lab);

    }
    // Aya: dont forget to make the fire worsenHealth() of people in the fire
    @Override //Aya: this is an added method, please integrate it into this class
    public void findResponders() {
        for (Agent a : MarsColony.getHumans()) {
            if (a.getClass() == Firefighter.class) {
                if (((Firefighter) a).canPerform(Capability.FIREFIGHTING)) {
                    this.addResponder(a);
                    a.setAvailable(false);
                    availableFirefighterFound = true;
                    break;
                }
            }
            if (a.getClass() == MedicalOfficer.class) {
                if (((MedicalOfficer) a).canPerform(Capability.MEDICAL)) {
                    this.addResponder(a);
                    a.setAvailable(false);
                    availableMedicalOfficerFound = true;
                    break;
                }
            }
        }
        if (!availableFirefighterFound && !availableMedicalOfficerFound) {
            status = EmergencyStatus.UNRESOLVED;
            System.out.println("No available Firefighter or Medical Officer to respond to the fire emergency.");
            return;
        }
        respondToEmergency();
    }


    public void respondToEmergency() {
        medicalEmergency.respondToEmergency();
        for (Agent a : getResponders()) {
            if (a.isAvailable() && a.getClass() == Firefighter.class) {
                availableFirefighterFound = true;

                Firefighter firefighter = (Firefighter) a;
                firefighter.assignMission();
                firefighter.setAvailable(false);
                firefighter.extinguishFire();
            }
            if (a.isAvailable() && a.getClass() == MedicalOfficer.class) {
                availableMedicalOfficerFound = true;
                MedicalOfficer medicalOfficer = (MedicalOfficer) a;
                medicalOfficer.assignMission();
                medicalOfficer.setAvailable(false);
                medicalOfficer.medicalAssist(patients);

            }
        }
        if (!availableFirefighterFound && !availableMedicalOfficerFound) {
            status = EmergencyStatus.UNRESOLVED;
            System.out.println("No available Firefighter or Medical Officer to respond to the fire emergency.");
        }
        else {
            status = EmergencyStatus.RESOLVED;
            getFacility().setOperationalStatus(FacilityStatus.OPERATIONAL);
            System.out.println("Fire emergency resolved.");
        }

    }

    private void damageEquipment(Laboratory lab) {
        for (Equipment equipment : lab.getEquipments()) {
            equipment.damageEquipment();
        }
    }

}