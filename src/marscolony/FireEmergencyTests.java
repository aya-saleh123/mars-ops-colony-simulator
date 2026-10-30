package marscolony;
import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.List;

import marscolony.MarsColony.MarsColony;
import marscolony.emergencies.Fire;
import marscolony.emergencies.EmergencyStatus;
import marscolony.facilities.FacilityStatus;
import marscolony.facilities.Hospital;
import marscolony.facilities.Laboratory;
import marscolony.missions.Capability;
import marscolony.missions.FailureReason;
import marscolony.missions.MissionState;
import marscolony.missions.Research;
import marscolony.personnel.Firefighter;
import marscolony.personnel.HealthStatus;
import marscolony.personnel.Human;
import marscolony.personnel.MedicalOfficer;
import marscolony.personnel.Scientist;
import marscolony.resources.ResourceType;

/**
 * Manual integration tests for the current Fire -> Emergency -> Hospital flow.
 * Run from the project root after compiling src and this file.
 */
public class FireEmergencyTests {
    private static int passed;
    private static int failed;

    public static void main(String[] args) throws Exception {
        run("Successful fire: patients treated, transferred, resources consumed, facility destroyed", FireEmergencyTests::successfulFire);
        run("Fire with no patients: firefighter alone can resolve it", FireEmergencyTests::fireWithNoPatients);
        run("Low oxygen: fire consumes only what the lab has", FireEmergencyTests::lowOxygenDoesNotGoNegative);
        run("No firefighter: fire remains unresolved and patients stay injured", FireEmergencyTests::noFirefighter);
        run("No medical officer: patient fire remains unresolved", FireEmergencyTests::noMedicalOfficer);
        run("No hospital: patient fire remains unresolved", FireEmergencyTests::noHospital);
        run("Hospital unavailable: patient fire remains unresolved", FireEmergencyTests::hospitalUnavailable);
        run("Insufficient medical supplies: no treatment or resource consumption", FireEmergencyTests::insufficientMedicalSupplies);
        run("Stale facility-list entry: person not physically present is not made a patient", FireEmergencyTests::personNotPresent);
        run("Unhealthy scientist cannot start a research mission", FireEmergencyTests::unhealthyScientistMissionRejected);
        run("Critical patient's health is not downgraded by fire", FireEmergencyTests::criticalPatientNotDowngraded);

        System.out.println("\n================ FIRE TEST SUMMARY ================");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("===================================================");
        if (failed > 0) System.exit(1);
    }

    private static void successfulFire() throws Exception {
        resetColony();
        Laboratory lab = lab(100);
        Hospital hospital = hospital(5);
        Firefighter firefighter = new Firefighter("Faris", "FF-1", Capability.FIREFIGHTING);
        MedicalOfficer medic = new MedicalOfficer("Mona", "MO-1", Capability.MEDICAL);
        Scientist patient1 = new Scientist("Salma", "SC-1");
        Scientist patient2 = new Scientist("Omar", "SC-2");
        lab.addHumanAgentTofacility(patient1);
        lab.addHumanAgentTofacility(patient2);
        int oxygenBefore = lab.getResourceCount(ResourceType.OXYGEN);
        int suppliesBefore = hospital.getResourceCount(ResourceType.MEDICAL_SUPPLIES);

        printSnapshot("BEFORE FIRE", lab, hospital, patient1, patient2, firefighter, medic);
        Fire fire = new Fire(lab);
        printSnapshot("AFTER FIRE CREATED / BEFORE RESPONSE", lab, hospital, patient1, patient2, firefighter, medic);
        check(patient1.getHealthStatus() == HealthStatus.INJURED && patient2.getHealthStatus() == HealthStatus.INJURED,
                "all people physically present in lab become injured patients");
        check(!patient1.canPerform(Capability.SCIENCE) && !patient2.canPerform(Capability.SCIENCE),
                "injured patients cannot perform their science skill");
        check(lab.getResourceCount(ResourceType.OXYGEN) == oxygenBefore - 50,
                "fire consumes 50 oxygen (or all available if below 50)");
        check(lab.getFacilityOperationalStatus() == FacilityStatus.UNAVAILABLE,
                "lab becomes unavailable immediately");

        fire.findResponders();
        printSnapshot("AFTER RESPONSE", lab, hospital, patient1, patient2, firefighter, medic);
        check(fire.getStatus() == EmergencyStatus.RESOLVED, "fire resolves");
        check(fire.getReason() == FailureReason.NONE, "resolved fire has NONE reason");
        check(lab.getFacilityOperationalStatus() == FacilityStatus.DESTROYED, "burning lab is destroyed");
        check(lab.getEquipments().stream().noneMatch(e -> e.isOperationalStatus()),
                "all equipment in the burning lab is damaged");
        check(hospital.getResourceCount(ResourceType.MEDICAL_SUPPLIES) == suppliesBefore - 2,
                "one medical supply is consumed per patient");
        check(patient1.getHealthStatus() == HealthStatus.HEALTHY && patient2.getHealthStatus() == HealthStatus.HEALTHY,
                "patients are treated back to healthy");
        check(hospital.currentlyPresentAtTheFacility(patient1) && hospital.currentlyPresentAtTheFacility(patient2),
                "both patients are transferred to hospital location");
        check(!lab.getFacilityHumanAgents().contains(patient1) && !lab.getFacilityHumanAgents().contains(patient2),
                "patients are removed from burning lab roster");
        check(firefighter.isAvailable() && medic.isAvailable(), "responders are released after response");
        check(fire.getResponders().size() == 2, "firefighter and medical officer are recorded as responders");
    }

    private static void fireWithNoPatients() throws Exception {
        resetColony();
        Laboratory lab = lab(80);
        Firefighter firefighter = new Firefighter("Faris", "FF-2", Capability.FIREFIGHTING);
        Fire fire = new Fire(lab);
        printSnapshot("NO-PATIENT FIRE BEFORE RESPONSE", lab, null, null, null, firefighter, null);
        fire.findResponders();
        printSnapshot("NO-PATIENT FIRE AFTER RESPONSE", lab, null, null, null, firefighter, null);
        check(fire.getStatus() == EmergencyStatus.RESOLVED, "fire resolves without medical officer/hospital when no patients");
        check(fire.getResponders().size() == 1 && fire.getResponders().contains(firefighter), "only firefighter is dispatched");
        check(lab.getFacilityOperationalStatus() == FacilityStatus.DESTROYED, "empty lab is still destroyed after fire");
    }

    private static void lowOxygenDoesNotGoNegative() throws Exception {
        resetColony();
        Laboratory lab = lab(20);
        Firefighter firefighter = new Firefighter("Faris", "FF-LOW-O2", Capability.FIREFIGHTING);
        Fire fire = new Fire(lab);
        printSnapshot("LOW OXYGEN AFTER FIRE STARTS", lab, null, null, null, firefighter, null);
        check(lab.getResourceCount(ResourceType.OXYGEN) == 0, "fire consumes the available 20 oxygen only");
        check(lab.getResourceCount(ResourceType.OXYGEN) >= 0, "oxygen never becomes negative");
        fire.findResponders();
        check(fire.getStatus() == EmergencyStatus.RESOLVED, "fire can still be handled when starting oxygen is below 50");
    }

    private static void noFirefighter() throws Exception {
        resetColony();
        Laboratory lab = lab(80);
        Hospital hospital = hospital(5);
        MedicalOfficer medic = new MedicalOfficer("Mona", "MO-2", Capability.MEDICAL);
        Scientist patient = new Scientist("Salma", "SC-3");
        lab.addHumanAgentTofacility(patient);
        Fire fire = new Fire(lab);
        fire.findResponders();
        printSnapshot("NO FIREFIGHTER", lab, hospital, patient, null, null, medic);
        check(fire.getStatus() == EmergencyStatus.UNRESOLVED, "fire is unresolved without firefighter");
        check(fire.getReason() == FailureReason.UNAVAILABLE_PERSONNEL, "failure reason is unavailable personnel");
        check(patient.getHealthStatus() == HealthStatus.INJURED, "patient remains injured");
        check(lab.currentlyPresentAtTheFacility(patient), "patient stays in burning lab when response fails");
        check(hospital.getResourceCount(ResourceType.MEDICAL_SUPPLIES) == 5, "medical supplies are not consumed");
        check(medic.isAvailable(), "medical officer is not dispatched if firefighter is missing");
    }

    private static void noMedicalOfficer() throws Exception {
        resetColony();
        Laboratory lab = lab(80);
        Hospital hospital = hospital(5);
        Firefighter firefighter = new Firefighter("Faris", "FF-3", Capability.FIREFIGHTING);
        Scientist patient = new Scientist("Salma", "SC-4");
        lab.addHumanAgentTofacility(patient);
        Fire fire = new Fire(lab);
        fire.findResponders();
        printSnapshot("NO MEDICAL OFFICER", lab, hospital, patient, null, firefighter, null);
        check(fire.getStatus() == EmergencyStatus.UNRESOLVED, "patient fire is unresolved without medical officer");
        check(fire.getReason() == FailureReason.UNAVAILABLE_PERSONNEL, "reason is unavailable personnel");
        check(firefighter.isAvailable(), "firefighter is not dispatched when full response cannot be assembled");
        check(patient.getHealthStatus() == HealthStatus.INJURED && lab.currentlyPresentAtTheFacility(patient),
                "patient remains injured at the lab");
        check(hospital.getResourceCount(ResourceType.MEDICAL_SUPPLIES) == 5, "no medical supplies consumed");
    }

    private static void noHospital() throws Exception {
        resetColony();
        Laboratory lab = lab(80);
        Firefighter firefighter = new Firefighter("Faris", "FF-4", Capability.FIREFIGHTING);
        MedicalOfficer medic = new MedicalOfficer("Mona", "MO-4", Capability.MEDICAL);
        Scientist patient = new Scientist("Salma", "SC-5");
        lab.addHumanAgentTofacility(patient);
        Fire fire = new Fire(lab);
        fire.findResponders();
        printSnapshot("NO HOSPITAL", lab, null, patient, null, firefighter, medic);
        check(fire.getStatus() == EmergencyStatus.UNRESOLVED && fire.getReason() == FailureReason.UNAVAILABLE_FACILITIES,
                "patient fire fails because no operational hospital exists");
        check(firefighter.isAvailable() && medic.isAvailable(), "responders are not dispatched without hospital");
        check(patient.getHealthStatus() == HealthStatus.INJURED && lab.currentlyPresentAtTheFacility(patient),
                "patient stays injured at lab");
    }

    private static void hospitalUnavailable() throws Exception {
        resetColony();
        Laboratory lab = lab(80);
        Hospital hospital = hospital(5);
        hospital.setOperationalStatus(FacilityStatus.UNAVAILABLE);
        Firefighter firefighter = new Firefighter("Faris", "FF-5", Capability.FIREFIGHTING);
        MedicalOfficer medic = new MedicalOfficer("Mona", "MO-5", Capability.MEDICAL);
        Scientist patient = new Scientist("Salma", "SC-6");
        lab.addHumanAgentTofacility(patient);
        Fire fire = new Fire(lab);
        fire.findResponders();
        printSnapshot("HOSPITAL UNAVAILABLE", lab, hospital, patient, null, firefighter, medic);
        check(fire.getStatus() == EmergencyStatus.UNRESOLVED && fire.getReason() == FailureReason.UNAVAILABLE_FACILITIES,
                "unavailable hospital prevents patient fire resolution");
        check(hospital.getResourceCount(ResourceType.MEDICAL_SUPPLIES) == 5, "hospital supplies remain unchanged");
        check(firefighter.isAvailable() && medic.isAvailable(), "responders remain available");
    }

    private static void insufficientMedicalSupplies() throws Exception {
        resetColony();
        Laboratory lab = lab(80);
        Hospital hospital = hospital(1);
        Firefighter firefighter = new Firefighter("Faris", "FF-6", Capability.FIREFIGHTING);
        MedicalOfficer medic = new MedicalOfficer("Mona", "MO-6", Capability.MEDICAL);
        Scientist patient1 = new Scientist("Salma", "SC-7");
        Scientist patient2 = new Scientist("Omar", "SC-8");
        lab.addHumanAgentTofacility(patient1);
        lab.addHumanAgentTofacility(patient2);
        Fire fire = new Fire(lab);
        fire.findResponders();
        printSnapshot("INSUFFICIENT MEDICAL SUPPLIES", lab, hospital, patient1, patient2, firefighter, medic);
        check(fire.getStatus() == EmergencyStatus.UNRESOLVED && fire.getReason() == FailureReason.INSUFFICIENT_RESOURCES,
                "not enough supplies leaves fire unresolved");
        check(hospital.getResourceCount(ResourceType.MEDICAL_SUPPLIES) == 1, "no partial medical-supply deduction occurs");
        check(patient1.getHealthStatus() == HealthStatus.INJURED && patient2.getHealthStatus() == HealthStatus.INJURED,
                "neither patient is treated");
        check(lab.currentlyPresentAtTheFacility(patient1) && lab.currentlyPresentAtTheFacility(patient2),
                "both patients remain at lab");
        check(firefighter.isAvailable() && medic.isAvailable(), "responders are not dispatched");
    }

    private static void personNotPresent() throws Exception {
        resetColony();
        Laboratory lab = lab(80);
        Firefighter firefighter = new Firefighter("Faris", "FF-7", Capability.FIREFIGHTING);
        Scientist person = new Scientist("Salma", "SC-9");
        lab.addHumanAgentTofacility(person);
        person.setLocation("Habitat Elsewhere"); // deliberately inconsistent/stale facility roster entry
        Fire fire = new Fire(lab);
        printSnapshot("PERSON LISTED BUT NOT PRESENT", lab, null, person, null, firefighter, null);
        check(person.getHealthStatus() == HealthStatus.HEALTHY, "person not physically present is not marked injured");
        fire.findResponders();
        printSnapshot("AFTER FIRE WITH NO PHYSICALLY PRESENT PATIENTS", lab, null, person, null, firefighter, null);
        check(fire.getStatus() == EmergencyStatus.RESOLVED, "fire does not require medical response for absent person");
        check(person.getHealthStatus() == HealthStatus.HEALTHY, "absent person's health remains healthy");
        check("Habitat Elsewhere".equals(person.getLocation()), "absent person's location is not changed by patient transfer");
    }

    private static void unhealthyScientistMissionRejected() throws Exception {
        resetColony();
        Laboratory burningLab = lab(100);
        Laboratory researchLab = lab(100);
        Hospital hospital = hospital(5);
        Firefighter firefighter = new Firefighter("Faris", "FF-8", Capability.FIREFIGHTING);
        MedicalOfficer medic = new MedicalOfficer("Mona", "MO-8", Capability.MEDICAL);
        Scientist scientist = new Scientist("Salma", "SC-10");
        burningLab.addHumanAgentTofacility(scientist);
        Fire fire = new Fire(burningLab); // marks Salma injured before any responder is dispatched

        System.out.println("\n--- MISSION CHECK WHILE SCIENTIST IS INJURED ---");
        printHuman("Scientist before mission", scientist);
        System.out.println("Research target lab=" + researchLab.getLocationName() + " status=" + researchLab.getFacilityOperationalStatus());
        Research research = new Research("Research while scientist injured", 2);
        boolean accepted = new MarsColony().startMission(research);
        System.out.println("Mission accepted=" + accepted + " state=" + research.getState()
                + " reason=" + research.getReasonOfFailure() + " assignedAgents=" + research.getAgents().size());
        check(!accepted, "research mission is rejected while only scientist is injured");
        check(research.getState() == MissionState.FAILED, "rejected research mission state is FAILED");
        check(research.getReasonOfFailure() == FailureReason.UNSUITABLE_AGENT,
                "mission rejection reason identifies unsuitable/unhealthy agent");
        check(research.getAgents().isEmpty(), "injured scientist is not assigned to mission");
        check(MarsColony.activeMission == null, "rejected mission does not become active");

        fire.findResponders();
        printSnapshot("AFTER FIRE RESPONSE FOLLOWING MISSION REJECTION", burningLab, hospital, scientist, null, firefighter, medic);
        check(fire.getStatus() == EmergencyStatus.RESOLVED && scientist.getHealthStatus() == HealthStatus.HEALTHY,
                "fire response treats scientist after the mission was rejected");
        check(hospital.currentlyPresentAtTheFacility(scientist), "treated scientist is moved to hospital");
    }

    private static void criticalPatientNotDowngraded() throws Exception {
        resetColony();
        Laboratory lab = lab(80);
        Firefighter firefighter = new Firefighter("Faris", "FF-9", Capability.FIREFIGHTING);
        Scientist critical = new Scientist("Salma", "SC-11");
        lab.addHumanAgentTofacility(critical);
        critical.setHealthStatus(HealthStatus.CRITICAL);
        Fire fire = new Fire(lab);
        printSnapshot("CRITICAL PATIENT AFTER FIRE CONSTRUCTION", lab, null, critical, null, firefighter, null);
        check(critical.getHealthStatus() == HealthStatus.CRITICAL,
                "fire preserves critical severity instead of overwriting it with INJURED");
        // Do not resolve: this case isolates the health transition performed by the Fire constructor.
    }

    private static Laboratory lab(int oxygen) {
        return new Laboratory(1000, oxygen, 100, 100, 100, 100, 100, 100);
    }

    private static Hospital hospital(int medicalSupplies) {
        return new Hospital(1000, 100, 100, 100, 100, 100, medicalSupplies, 100);
    }

    private static void printSnapshot(String heading, Laboratory lab, Hospital hospital,
                                      Human p1, Human p2, Firefighter firefighter, MedicalOfficer medic) {
        System.out.println("\n--- " + heading + " ---");
        if (lab != null) {
            System.out.println("LAB " + lab.getLocationName() + " | status=" + lab.getFacilityOperationalStatus()
                    + " | oxygen=" + lab.getResourceCount(ResourceType.OXYGEN)
                    + " | peopleList=" + names(lab.getFacilityHumanAgents()));
        }
        if (hospital != null) {
            System.out.println("HOSPITAL " + hospital.getLocationName() + " | status=" + hospital.getFacilityOperationalStatus()
                    + " | medicalSupplies=" + hospital.getResourceCount(ResourceType.MEDICAL_SUPPLIES));
        }
        if (p1 != null) printHuman("PERSON 1", p1);
        if (p2 != null) printHuman("PERSON 2", p2);
        if (firefighter != null) printHuman("FIREFIGHTER", firefighter);
        if (medic != null) printHuman("MEDICAL OFFICER", medic);
    }

    private static void printHuman(String label, Human human) {
        System.out.println(label + " " + human.getName() + " | health=" + human.getHealthStatus()
                + " | available=" + human.isAvailable() + " | location=" + human.getLocation()
                + " | skill=" + human.getSkill() + " | canPerformOwnSkill=" + human.canPerform(human.getSkill()));
    }

    private static String names(List<Human> humans) {
        List<String> result = new ArrayList<>();
        for (Human human : humans) result.add(human.getName() + "(" + human.getLocation() + ")");
        return result.toString();
    }

    private static void resetColony() throws Exception {
        setStaticField("crewMembers", new ArrayList<Human>());
        setStaticField("facilities", new ArrayList<>());
        setStaticField("robots", new ArrayList<>());
        setStaticField("equipments", new ArrayList<>());
        setStaticField("missions", new ArrayList<>());
        MarsColony.activeMission = null;
    }

    private static void setStaticField(String name, Object value) throws Exception {
        Field field = MarsColony.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(null, value);
    }

    private interface TestBody { void run() throws Exception; }

    private static void run(String name, TestBody body) {
        System.out.println("\n\n===================================================");
        System.out.println("TEST: " + name);
        System.out.println("===================================================");
        try {
            body.run();
            System.out.println("RESULT: PASS");
            passed++;
        } catch (Throwable ex) {
            System.out.println("RESULT: FAIL - " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
            ex.printStackTrace(System.out);
            failed++;
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
        System.out.println("  [OK] " + message);
    }
}
