package marscolony.emergencies;

import java.util.List;

import marscolony.facilities.Facilities;
import marscolony.missions.Capability;
import marscolony.personnel.*;
import marscolony.MarsColony.MarsColony;
import marscolony.missions.MissionState;
import marscolony.missions.Exploration;
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
                    availableRepairRobotFound = true;
                    repairRobot.assignMission(MarsColony.getEnvCondition());
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
                engineer.repairRover();
            }
            if (a.getClass() == RepairRobot.class) {
                RepairRobot repairRobot = (RepairRobot) a;
                repairRobot.repairRover();
            }
        }
    }

    public void roverRepaired() {
        //to Yara: minus spare parts here and make sure to add validation for if you don't have enough spare parts
        for (Agent a : getResponders()) {
            a.setAvailable(true);
        }
        status = EmergencyStatus.RESOLVED;
        brokenRover.setAvailable(true);
        brokenRover.setBatteryLevel(BatteryLevel.HIGH);
        brokenRover.setLocation("Base");
    }

}