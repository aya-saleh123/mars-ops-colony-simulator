package marscolony.emergencies;

import marscolony.MarsColony.MarsColony;
import marscolony.facilities.Facilities;
import marscolony.missions.Capability;
import marscolony.missions.Exploration;
import marscolony.missions.FailureReason;
import marscolony.personnel.Agent;
import marscolony.personnel.BatteryLevel;
import marscolony.personnel.Engineer;
import marscolony.personnel.ExplorationRover;
import marscolony.personnel.RepairRobot;
import marscolony.resources.ResourceType;

public class RoverBreakdown extends Emergency {
    private static final int SPARE_PARTS_COST = 5;

    private final ExplorationRover brokenRover;
    private final Exploration failedMission;
    private Engineer engineer;
    private RepairRobot repairRobot;

    public RoverBreakdown(Facilities facility, Exploration failedMission) {
        super(facility);
        if (failedMission == null || failedMission.getRover() == null) {
            throw new IllegalArgumentException("A rover breakdown requires an exploration mission with a rover");
        }
        this.brokenRover = failedMission.getRover();
        this.failedMission = failedMission;

        breakdownHappens();
    }

    @Override
    public String getLocation() {
        return "Martian Surface";
    }

    private void breakdownHappens() {
        System.out.println("Rover breakdown occurred during the exploration mission to " + failedMission.getDestination() + ".");

        brokenRover.setOperational(false);
        brokenRover.setBatteryLevel(BatteryLevel.LOW);
        failedMission.fail(FailureReason.EQUIPMENT_FAILURE);

        if (brokenRover.isAvailable()) {
            brokenRover.setAvailable(false);
        }

        brokenRover.setLocation("Martian Surface");

        System.out.println("The rover remains on the Martian surface; the exploration mission has failed.");
    }

    @Override
    public void findResponders() {
        engineer = null;
        repairRobot = null;

        for (Agent agent : MarsColony.getHumans()) {
            if (agent.canPerform(Capability.REPAIR)) {
                engineer = (Engineer) agent;
                break;
            }
        }

        for (Agent agent : MarsColony.getRobots()) {
            if (agent.canPerform(Capability.REPAIR)) {
                repairRobot = (RepairRobot) agent;
                break;
            }
        }

        if (engineer == null && repairRobot == null) {
            setReason(FailureReason.UNAVAILABLE_PERSONNEL);
            setStatus(EmergencyStatus.UNRESOLVED);
            return;
        }

        if (!MarsColony.deductResourceFromFacilities(ResourceType.SPARE_PARTS, SPARE_PARTS_COST)) {
            setReason(FailureReason.INSUFFICIENT_RESOURCES);
            setStatus(EmergencyStatus.UNRESOLVED);
            return;
        }

        if (engineer != null) {
            dispatchResponder(engineer);
        }

        if (repairRobot != null) {
            dispatchResponder(repairRobot);
        }

        respondToEmergency();
    }

    @Override
    public void respondToEmergency() {
        if (engineer == null && repairRobot == null) {
            setReason(FailureReason.UNAVAILABLE_PERSONNEL);
            setStatus(EmergencyStatus.UNRESOLVED);
            return;
        }

        if (engineer != null) {
            engineer.repairRover();
        }

        if (repairRobot != null) {
            repairRobot.repairRover();
        }

        brokenRover.setOperational(true);
        brokenRover.setBatteryLevel(BatteryLevel.HIGH);
        brokenRover.setLocation("Base");

        if (!brokenRover.isAvailable()) {
            brokenRover.setAvailable(true);
        }

        setStatus(EmergencyStatus.RESOLVED);
        setReason(FailureReason.NONE);

        System.out.println("Rover breakdown resolved. The rover has been repaired and returned to Base.");
        releaseResponders();
    }
}