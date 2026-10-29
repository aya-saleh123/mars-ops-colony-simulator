package marscolony.emergencies;

import java.util.ArrayList;
import java.util.List;

import marscolony.MarsColony.MarsColony;
import marscolony.Equipment.Equipment;
import marscolony.facilities.Facilities;
import marscolony.facilities.FacilityStatus;
import marscolony.facilities.Habitat;
import marscolony.facilities.Laboratory;
import marscolony.missions.Capability;
import marscolony.missions.FailureReason;
import marscolony.personnel.Agent;
import marscolony.personnel.FatigueLevel;
import marscolony.personnel.Firefighter;
import marscolony.personnel.Human;
import marscolony.personnel.MedicalOfficer;
import marscolony.resources.ResourceType;

public class Fire extends Emergency {
    private static final int OXYGEN_LOSS = 50;

    private final List<Human> patients = new ArrayList<>();
    private final Habitat medicalHabitat;
    private Firefighter firefighter;
    private MedicalOfficer medicalOfficer;

    //delwa2ti e7na el fire fiha medical emergency
    //fa t3ali b3d ma ta3meli el medical emergency
    //erga3i w 7oti 7agat medical emergency hena

    //dont forget to create subclass of habitat called hospital

    public Fire(Laboratory lab, List<Human> patients) {
        super(lab);
        if (patients != null) {
            this.patients.addAll(patients);
        }
        this.medicalHabitat = findOperationalHabitat();
        lab.setOperationalStatus(FacilityStatus.UNAVAILABLE);
        int lost = lab.removeUpTo(ResourceType.OXYGEN, OXYGEN_LOSS);
        System.out.println("Fire consumed " + lost + " oxygen in " + lab.getLocationName());
        damageEquipment(lab);
    }

    @Override
    public void findResponders() {
        firefighter = null;
        medicalOfficer = null;
        for (Agent agent : MarsColony.getHumans()) {
            if (firefighter == null && agent.canPerform(Capability.FIREFIGHTING)) {
                firefighter = (Firefighter) agent;
            }
            if (!patients.isEmpty() && medicalOfficer == null && agent.canPerform(Capability.MEDICAL)) {
                medicalOfficer = (MedicalOfficer) agent;
            }
        }
        if (firefighter == null) {
            setReason(FailureReason.UNAVAILABLE_PERSONNEL);
            setStatus(EmergencyStatus.UNRESOLVED);
            return;
        }
        if (!patients.isEmpty()) {
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

            if (medicalHabitat.getResourceCount(ResourceType.MEDICAL_SUPPLIES) < patients.size()) {
                setReason(FailureReason.INSUFFICIENT_RESOURCES);
                setStatus(EmergencyStatus.UNRESOLVED);
                return;
            }
        }

        dispatchResponder(firefighter);

        if (medicalOfficer != null) {
            dispatchResponder(medicalOfficer);
        }

        respondToEmergency();
    }

    @Override
    public void respondToEmergency() {
        if (firefighter == null) {
            setReason(FailureReason.UNAVAILABLE_PERSONNEL);
            setStatus(EmergencyStatus.UNRESOLVED);
            return;
        }

        if (!patients.isEmpty() && medicalOfficer == null) {
            setReason(FailureReason.UNAVAILABLE_PERSONNEL);
            setStatus(EmergencyStatus.UNRESOLVED);
            return;
        }

        firefighter.extinguishFire();
        firefighter.justFinished();

        if (!patients.isEmpty()) {
            if (medicalHabitat == null || medicalHabitat.getFacilityOperationalStatus() != FacilityStatus.OPERATIONAL) {
                setReason(FailureReason.UNAVAILABLE_FACILITIES);
                setStatus(EmergencyStatus.UNRESOLVED);
                return;
            }
            else if (medicalHabitat.getResourceCount(ResourceType.MEDICAL_SUPPLIES) < patients.size()) {
                setReason(FailureReason.INSUFFICIENT_RESOURCES);
                setStatus(EmergencyStatus.UNRESOLVED);
                return;
            }

            medicalHabitat.removeResource(ResourceType.MEDICAL_SUPPLIES,patients.size());

            for (Human patient : patients) {
                patient.decFatigueLevel();
                medicalOfficer.medicalAssist(patient);
            }
        }

        medicalOfficer.justFinished();
        getFacility().setOperationalStatus(FacilityStatus.DESTROYED);
        setStatus(EmergencyStatus.RESOLVED);
        setReason(FailureReason.NONE);
        System.out.println("Fire emergency resolved at " + getLocation() + "But the facility is destroyed and is in need of repair.");
        releaseResponders();
    }

    private Habitat findOperationalHabitat() {
        for (Facilities facility : MarsColony.getFacilities()) {
            if (facility instanceof Habitat && facility.getFacilityOperationalStatus() == FacilityStatus.OPERATIONAL) {
                return (Habitat) facility;
            }
        }
        return null;
    }

    private void damageEquipment(Laboratory lab) {
        for (Equipment equipment : lab.getEquipments()) {
            equipment.damageEquipment();
        }
    }
}