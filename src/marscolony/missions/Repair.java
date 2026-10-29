package marscolony.missions;
import java.util.List;
import java.util.ArrayList;
import marscolony.resources.ResourceType;
import marscolony.MarsColony.MarsColony;
import marscolony.conditions.EnvironmentCondition;
import marscolony.facilities.Facilities;
import marscolony.facilities.FacilityStatus;
import marscolony.personnel.*;



public class Repair extends Mission {
    private final Facilities facilityToRepair;

    public Repair(String name, int missionDuration ,Facilities facilityToRepair) {
        super(name, missionDuration);
       this.facilityToRepair = facilityToRepair;
        setTarget(facilityToRepair);
    }

    
    public int getRequiredAmount(ResourceType type) {
         if(type==ResourceType.SPARE_PARTS)
            return 2;
        if(type==ResourceType.BATTERY_POWER)
            return 5;
         else
            return 0;

    }

    @Override
    protected void Completed() { 
        facilityToRepair.repairFacility();;
        System.out.println(facilityToRepair.getLocationName()+" is repaired");
     }

    @Override 
    public boolean checkRequiredForMission()
    {
        boolean humanStatus = false , robotStatus = false ;
        for(Human human : MarsColony.getHumans())
        {
            humanStatus = human.canPerform(Capability.REPAIR); 
            if(humanStatus)
            {
                this.assignAgent(human);
                break;
            }
        }
        for(RoboticUnit robot : MarsColony.getRobots() )
        {
            
            robotStatus = robot.canPerform(Capability.REPAIR);
            if(robotStatus)
            {
                this.assignAgent(robot);
                break;
            }
        }
        if(humanStatus && robotStatus){
            return true;
        }
        else
        {
            releaseAgents();
            return false;
        }
    }
}