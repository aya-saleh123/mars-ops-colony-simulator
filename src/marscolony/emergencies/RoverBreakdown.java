package marscolony.emergencies;

import java.util.List;

import marscolony.facilities.Facilities;
import marscolony.personnel.Agent;
import marscolony.personnel.Engineer;
import marscolony.personnel.RepairRobot;
import marscolony.missions.Capability;
import marscolony.personnel.*;
import marscolony.MarsColony.MarsColony;
import marscolony.missions.MissionState;
import marscolony.missions.Exploration;
import marscolony.personnel.Human;
import marscolony.conditions.EnvironmentCondition;

public class RoverBreakdown extends Emergency {
    EmergencyStatus status = EmergencyStatus.IN_PROGRESS;
    Boolean availableEngineerFound = false;
    Boolean availableRepairRobotFound = false;
    ExplorationRover brokenRover;
    Exploration FailedMission;

    public RoverBreakdown(Facilities facility, Exploration FailedMission)
    {
        super(facility);
        this.brokenRover = FailedMission.getRover();
        this.FailedMission = FailedMission;
        BreakdownHappens();
    }

    public void BreakdownHappens() {
        System.out.println("Rover breakdown occurred during the exploration mission to " + FailedMission.getDestination());
        brokenRover.setOperational(false);
        brokenRover.setBatteryLevel(BatteryLevel.LOW);
        FailedMission.setState(MissionState.FAILED);
        brokenRover.setLocation("Martian Surface");
        System.out.println("The rover is now non-operational and located on the Martian surface. The exploration mission has failed.");
    }

    @Override 
    public void findResponders() {
        System.out.println("Searching for available Engineer or Repair Robot to respond to the rover breakdown emergency.");
        for (Agent a : MarsColony.getHumans()) {
            if (a.getClass() == Engineer.class) {
                if (((Engineer) a).canPerform(Capability.REPAIR)) {
                    Engineer engineer = (Engineer) a;
                    this.addResponder(engineer);
                    availableEngineerFound = true;
                    engineer.assignMission();
                    break;
                }
            }
        }
        for (Agent a : MarsColony.getRobots()) {
            if (a.getClass() == RepairRobot.class) {
                if (((RepairRobot) a).canPerform(Capability.REPAIR)) {
                    RepairRobot repairRobot = (RepairRobot) a;
                    this.addResponder(repairRobot);
                    repairRobot.setAvailable(false);
                    availableRepairRobotFound = true;
                    System.out.println("Repair Robot " + repairRobot.getIdNumber() + " has been assigned to respond to the rover breakdown emergency.");
                    break;
                }
            }
        }
        if (!availableEngineerFound && !availableRepairRobotFound) {
            status = EmergencyStatus.UNRESOLVED;
            System.out.println("No available Engineer or Repair Robot to respond to the oxygen leak emergency.");
            return;
        }
        System.out.println("Proceeding to respond to the rover breakdown emergency...");
        respondToEmergency();
    }

    public void respondToEmergency() {
        for (Agent a : getResponders()) {
            if (a.getClass() == Engineer.class) {
                Engineer engineer = (Engineer) a;
                engineer.assignMission();
                engineer.setAvailable(false);
                engineer.repairRover();
                System.out.println("Engineer " + engineer.getName() + " is repairing the rover.");
            }
            if (a.getClass() == RepairRobot.class) {
                RepairRobot repairRobot = (RepairRobot) a;
                repairRobot.assignMission(MarsColony.getEnvCondition());
                repairRobot.setAvailable(false);
                repairRobot.repairRover();
                System.out.println("Repair Robot " + repairRobot.getIdNumber() + " is repairing the rover.");
            }
        }
    }

}