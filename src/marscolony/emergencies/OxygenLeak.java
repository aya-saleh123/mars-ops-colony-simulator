package marscolony.emergencies;

import java.util.List;

import marscolony.facilities.*;
import marscolony.personnel.*;
import marscolony.resources.*;
import marscolony.conditions.EnvironmentCondition;
import marscolony.missions.*;
import marscolony.MarsColony.MarsColony;

public class OxygenLeak extends Emergency {
    EmergencyStatus status = EmergencyStatus.IN_PROGRESS;
    Boolean availableEngineerFound = false;
    Boolean availableRepairRobotFound = false;

    public OxygenLeak(Facilities facility)
    {
        super(facility);
    }

    @Override 
    public void findResponders() {
        
        respondToEmergency();
    }

    @Override 
    public void respondToEmergency() {
        
    }

}