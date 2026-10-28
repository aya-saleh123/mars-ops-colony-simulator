package marscolony.emergencies;

import java.util.List;

import marscolony.facilities.Facilities;
import marscolony.personnel.Agent;
import marscolony.personnel.Engineer;
import marscolony.personnel.RepairRobot;
import marscolony.missions.Capability;

public class RoverBreakdown extends Emergency {
    EmergencyStatus status = EmergencyStatus.IN_PROGRESS;
    Boolean availableEngineerFound = false;
    Boolean availableRepairRobotFound = false;

    public RoverBreakdown(Facilities facility, List<Agent> responders)// Do not forget agent
    {
        super(facility, responders);
    }

    @Override 
    public void findResponders() {
        for (Agent a : getResponders()) {
            if (a.getClass() == Engineer.class) {
                if (((Engineer) a).canPerform(Capability.REPAIR)) {
                    this.addResponder(a);
                    a.setAvailable(false);
                    availableEngineerFound = true;
                    break;
                }
            }
            if (a.getClass() == RepairRobot.class) {
                if (((RepairRobot) a).canPerform(Capability.REPAIR)) {
                    this.addResponder(a);
                    a.setAvailable(false);
                    availableRepairRobotFound = true;
                    break;
                }
            }
        }
        if (!availableEngineerFound && !availableRepairRobotFound) {
            status = EmergencyStatus.UNRESOLVED;
            System.out.println("No available Engineer or Repair Robot to respond to the oxygen leak emergency.");
            return;
        }
        respondToEmergency();
    }

    public void respondToEmergency() {

    }

}