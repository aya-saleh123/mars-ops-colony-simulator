package marscolony.emergencies;

import java.util.List;

import marscolony.facilities.*;
import marscolony.personnel.*;
import marscolony.resources.*;
import marscolony.conditions.EnvironmentCondition;
import marscolony.missions.*;

public class OxygenLeak extends Emergency {
    EmergencyStatus status = EmergencyStatus.IN_PROGRESS;
    Boolean availableEngineerFound = false;
    Boolean availableRepairRobotFound = false;

    public OxygenLeak(Facilities facility, List<Agent> responders)// do not forget agent
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

    @Override 
    public void respondToEmergency() {
        
    }

}