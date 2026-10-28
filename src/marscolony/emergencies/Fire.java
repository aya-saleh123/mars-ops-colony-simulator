package marscolony.emergencies;

import java.util.ArrayList;
import java.util.List;
import marscolony.facilities.*;
import marscolony.personnel.*;
import marscolony.resources.ResourceType;
import marscolony.Equipment.*;
import marscolony.missions.*;

public class Fire extends Emergency {
    EmergencyStatus status = EmergencyStatus.IN_PROGRESS;
    List<Human> patients = new ArrayList<>();
    MedicalEmergency medicalEmergency;
    boolean availableFirefighterFound = false;
    boolean availableMedicalOfficerFound = false;

    public Fire(Laboratory lab, List<Agent> responders) {
        super(lab, responders);
        lab.setOperationalStatus(FacilityStatus.UNAVAILABLE);
        int lost = lab.removeUpTo(ResourceType.OXYGEN, 50);
        System.out.println("Fire consumed " + lost + " oxygen in " + lab.getLocationName());
        medicalEmergency = new MedicalEmergency(lab, responders, patients);
        damageEquipment(lab);

    }

    @Override //Aya: this is an added method, please integrate it into this class
    public void findResponders() {
        for (Agent a : getResponders()) {
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

    }

    private void damageEquipment(Laboratory lab) {
        for (Equipment equipment : lab.getEquipments()) {
            equipment.damageEquipment();
        }
    }

}