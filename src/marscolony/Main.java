package marscolony;

import marscolony.MarsColony.MarsColony;
import marscolony.MarsColony.MarsSurface;
import marscolony.conditions.EnvironmentCondition;
import marscolony.Equipment.Equipment;
import marscolony.Equipment.EquipmentType;
import marscolony.emergencies.OxygenLeak;
import marscolony.emergencies.EmergencyStatus;
import marscolony.facilities.*;
import marscolony.missions.*;
import marscolony.personnel.*;
import marscolony.resources.ResourceType;

/**
 * Required Demonstration Scenarios from Lab 5, Section 12.
 * Run this class to execute the five labelled demonstrations.
 */
public class Main {
    private static final MarsColony colony = new MarsColony();

    private static void heading(int number, String title) {
        System.out.println("\n==================================================");
        System.out.println("SCENARIO " + number + ": " + title);
        System.out.println("==================================================");
    }

    private static void result(boolean passed, String detail) {
        System.out.println("TEST RESULT: " + (passed ? "SUCCEEDED" : "FAILED"));
        System.out.println("DETAIL: " + detail);
    }

    private static void missionStatus(String label, Mission mission) {
        System.out.println(label + " Mission state: " + mission.getState()
                + " | Failure reason: " + mission.getReasonOfFailure()
                + " | Elapsed: " + mission.getElapsed() + "/" + mission.getMissionDuration());
    }

    private static void scenario1SuccessfulExploration() {
        heading(1, "Successful Exploration");
        try {
            Habitat base = new Habitat(700, 100, 100, 100, 100, 100, 20, 20);
            Scientist scientist = new Scientist("Lina", "S-01");
            ExplorationRover rover = new ExplorationRover("R-01");
            MarsColony.addEquipement(new Equipment(EquipmentType.EXPLORATION_EQUIPMENT));
            Exploration mission = new Exploration("Explore Jezero Crater", 2, MarsSurface.JEZERO_CRATER);

            System.out.println("BEFORE: condition=" + MarsColony.getEnvCondition()
                    + ", active mission=" + (MarsColony.activeMission == null ? "none" : MarsColony.activeMission.getName()));
            missionStatus("BEFORE:", mission);
            System.out.println("BEFORE: scientist available=" + scientist.isAvailable()
                    + ", rover available=" + rover.isAvailable()
                    + ", rover battery=" + rover.getBatteryLevel());
            System.out.println("BEFORE: colony fuel=" + MarsColony.getTotalResourceCount(ResourceType.FUEL));

            boolean accepted = colony.startMission(mission);
            if (accepted) MarsColony.advanceTime(2);

            System.out.println("AFTER: active mission=" + (MarsColony.activeMission == null ? "none" : MarsColony.activeMission.getName()));
            missionStatus("AFTER:", mission);
            System.out.println("AFTER: scientist available=" + scientist.isAvailable()
                    + ", fatigue=" + scientist.getFatigueLevel()
                    + ", rover available=" + rover.isAvailable()
                    + ", rover battery=" + rover.getBatteryLevel());
            System.out.println("AFTER: colony fuel=" + MarsColony.getTotalResourceCount(ResourceType.FUEL));

            result(accepted && mission.getState() == MissionState.COMPLETED
                    && scientist.isAvailable() && rover.isAvailable(),
                    "Exploration was accepted, completed, and both agents were released.");
        } catch (Exception e) {
            result(false, "Exception in Scenario 1: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    private static void scenario2MissionRejection() {
        heading(2, "Mission Rejection - Unsuitable Agent");
        try {
            Engineer engineer = new Engineer("Omar", "E-02");
            // Make the only scientist unavailable so the available engineer is unsuitable for exploration.
            Human scientist = null;
            for (Human human : MarsColony.getHumans()) {
                if (human instanceof Scientist) { scientist = human; break; }
            }
            if (scientist != null && scientist.isAvailable()) scientist.setAvailable(false);

            Exploration mission = new Exploration("Rejected Exploration", 2, MarsSurface.GALE_CRATER);
            System.out.println("BEFORE: mission state=" + mission.getState()
                    + ", active mission=" + (MarsColony.activeMission == null ? "none" : MarsColony.activeMission.getName()));
            System.out.println("BEFORE: engineer available=" + engineer.isAvailable()
                    + ", engineer capability=" + engineer.getSkill()
                    + ", available scientist=" + (scientist != null && scientist.isAvailable()));

            boolean accepted = colony.startMission(mission);

            System.out.println("AFTER: mission state=" + mission.getState()
                    + ", failure reason=" + mission.getReasonOfFailure()
                    + ", active mission=" + (MarsColony.activeMission == null ? "none" : MarsColony.activeMission.getName()));
            System.out.println("AFTER: engineer available=" + engineer.isAvailable());
            if (scientist != null && !scientist.isAvailable()) scientist.setAvailable(true);

            result(!accepted && mission.getState() == MissionState.FAILED
                    && mission.getReasonOfFailure() == FailureReason.UNSUITABLE_AGENT
                    && MarsColony.activeMission == null && engineer.isAvailable(),
                    "The unsuitable submission was rejected immediately, recorded a reason, and never became active.");
        } catch (Exception e) {
            result(false, "Exception in Scenario 2: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    private static void scenario3ResourceShortage() {
        heading(3, "Resource Shortage - No Partial Consumption");
        try {
            Laboratory lab = new Laboratory(500, 50, 50, 50, 50, 50, 20, 20);
            // Research requires 5 water and 10 battery power. Leave less than 5 water colony-wide.
            for (Facilities f : MarsColony.getFacilities()) {
                if (f.getFacilityOperationalStatus() == FacilityStatus.OPERATIONAL) {
                    int water = f.getResourceCount(ResourceType.WATER);
                    if (water > 0) f.removeResource(ResourceType.WATER, water);
                }
            }
            // A valid scientist and scientific equipment ensure the rejection is specifically resource-related.
            MarsColony.addEquipement(new Equipment(EquipmentType.SCIENTIFIC_EQUIPMENT));
            Research mission = new Research("Water-Short Research", 2);
            int waterBefore = MarsColony.getTotalResourceCount(ResourceType.WATER);
            int batteryBefore = MarsColony.getTotalResourceCount(ResourceType.BATTERY_POWER);

            System.out.println("BEFORE: mission state=" + mission.getState()
                    + ", water=" + waterBefore + "/5 required"
                    + ", battery=" + batteryBefore + "/10 required"
                    + ", active mission=" + (MarsColony.activeMission == null ? "none" : MarsColony.activeMission.getName()));

            boolean accepted = colony.startMission(mission);
            int waterAfter = MarsColony.getTotalResourceCount(ResourceType.WATER);
            int batteryAfter = MarsColony.getTotalResourceCount(ResourceType.BATTERY_POWER);

            System.out.println("AFTER: mission state=" + mission.getState()
                    + ", failure reason=" + mission.getReasonOfFailure()
                    + ", water=" + waterAfter + ", battery=" + batteryAfter
                    + ", active mission=" + (MarsColony.activeMission == null ? "none" : MarsColony.activeMission.getName()));

            result(!accepted && mission.getState() == MissionState.FAILED
                    && mission.getReasonOfFailure() == FailureReason.INSUFFICIENT_RESOURCES
                    && waterBefore == waterAfter && batteryBefore == batteryAfter
                    && waterAfter >= 0 && batteryAfter >= 0,
                    "The mission was rejected for insufficient water; no resources were consumed and none became negative.");
        } catch (Exception e) {
            result(false, "Exception in Scenario 3: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    private static void scenario4EmergencyHandling() {
        heading(4, "Emergency Handling - Oxygen Leak");
        try {
            Habitat affectedHabitat = new Habitat(300, 100, 20, 20, 20, 20, 10, 5);
            RepairRobot repairRobot = new RepairRobot("RR-04");
            int oxygenBefore = affectedHabitat.getResourceCount(ResourceType.OXYGEN);
            int sparePartsBefore = MarsColony.getTotalResourceCount(ResourceType.SPARE_PARTS);

            System.out.println("BEFORE: habitat=" + affectedHabitat.getLocationName()
                    + ", status=" + affectedHabitat.getFacilityOperationalStatus()
                    + ", oxygen=" + oxygenBefore
                    + ", colony spare parts=" + sparePartsBefore);
            System.out.println("BEFORE: repair robot available=" + repairRobot.isAvailable());

            OxygenLeak leak = new OxygenLeak(affectedHabitat);
            leak.triggerEmergency();
            leak.findResponders();

            System.out.println("AFTER: emergency status=" + leak.getStatus()
                    + ", failure reason=" + leak.getReason());
            System.out.println("AFTER: habitat status=" + affectedHabitat.getFacilityOperationalStatus()
                    + ", oxygen=" + affectedHabitat.getResourceCount(ResourceType.OXYGEN)
                    + ", colony spare parts=" + MarsColony.getTotalResourceCount(ResourceType.SPARE_PARTS));
            System.out.println("AFTER: repair robot available=" + repairRobot.isAvailable()
                    + ", responders=" + leak.getResponders().size());

            result(leak.getStatus() == EmergencyStatus.RESOLVED
                    && affectedHabitat.getFacilityOperationalStatus() == FacilityStatus.OPERATIONAL
                    && repairRobot.isAvailable()
                    && leak.getReason() == FailureReason.NONE,
                    "The oxygen leak was repaired, the habitat returned to operational status, and responders were released.");
        } catch (Exception e) {
            result(false, "Exception in Scenario 4: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    private static void scenario5EnvironmentalEffect() {
        heading(5, "Environmental Effect - Dust Storm Blocks Exploration");
        try {
            MarsColony.currentCondition = EnvironmentCondition.DUST_STORM;
            Exploration mission = new Exploration("Dust-Storm Exploration", 2, MarsSurface.OLYMPUS_MONS);

            System.out.println("BEFORE: environment=" + MarsColony.getEnvCondition()
                    + ", mission state=" + mission.getState()
                    + ", active mission=" + (MarsColony.activeMission == null ? "none" : MarsColony.activeMission.getName()));

            boolean accepted = colony.startMission(mission);

            System.out.println("AFTER: environment=" + MarsColony.getEnvCondition()
                    + ", mission state=" + mission.getState()
                    + ", failure reason=" + mission.getReasonOfFailure()
                    + ", active mission=" + (MarsColony.activeMission == null ? "none" : MarsColony.activeMission.getName()));

            result(!accepted && mission.getState() == MissionState.FAILED
                    && mission.getReasonOfFailure() == FailureReason.ENVIRONMENTAL_CONDITIONS
                    && MarsColony.activeMission == null,
                    "The dust storm prevented the exploration mission from starting and recorded the environmental failure.");
        } catch (Exception e) {
            result(false, "Exception in Scenario 5: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    public static void main(String[] args) {
        System.out.println("MARS-OPS LAB 5 - SECTION 12 REQUIRED DEMONSTRATION TESTS");
        try {
            scenario1SuccessfulExploration();
            scenario2MissionRejection();
            scenario3ResourceShortage();
            scenario4EmergencyHandling();
            scenario5EnvironmentalEffect();
        } catch (Throwable t) {
            System.out.println("FATAL TEST RUNNER ERROR: " + t.getClass().getSimpleName() + ": " + t.getMessage());
            t.printStackTrace(System.out);
        }
        System.out.println("\n================ TEST RUN FINISHED ================");
    }
}
