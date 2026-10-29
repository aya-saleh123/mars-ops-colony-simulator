package marscolony.missions;
import marscolony.resources.ResourceType;
import marscolony.MarsColony.MarsColony;
import marscolony.conditions.EnvironmentCondition;
import marscolony.personnel.*;
import marscolony.facilities.Facilities;
import marscolony.facilities.FacilityStatus;
import marscolony.facilities.Habitat;
import marscolony.facilities.Laboratory;

import java.util.List;
import java.util.ArrayList;

public class Rescue extends Mission{
    private Agent inCrisisAgent;
    private String rescueLocation;
    private String safeLocation;

    public Rescue(String name, int missionDuration , Agent inCrisisAgent) {
        super(name, missionDuration);
        this.inCrisisAgent = inCrisisAgent;
        this.rescueLocation = inCrisisAgent.getLocation();
        this.setSafeLocation();
    }
    
    @Override
    public int getRequiredAmount(ResourceType type) {
         if(type==ResourceType.FUEL)
            return 10;
        else return 0;
    }

      @Override
    protected void Completed() {
        super.Completed();
        inCrisisAgent.setLocation(safeLocation);
        System.out.println(inCrisisAgent.getIdNumber() +" has been rescued and brought to "+ safeLocation);
     }


     @Override 
     public boolean checkRequiredForMission()
    {
        boolean humanStatus = false , robotStatus = false ;
        for(Human human : MarsColony.getHumans())
        {
                humanStatus = human.canPerform(Capability.FIREFIGHTING); 
                if(humanStatus)
                {
                    this.assignAgent(human);
                    break;
                }
        }
        for(RoboticUnit robot : MarsColony.getRobots() )
        {
                robotStatus = robot.canPerform(Capability.AERIAL);
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

    private void setSafeLocation()
    {
        int flag =0;
        for(Facilities facility : MarsColony.getFacilities())
            {
                if(facility instanceof Habitat)
                {
                    if(facility.getFacilityOperationalStatus() == FacilityStatus.OPERATIONAL)
                    {
                        this.safeLocation = facility.getLocationName();
                        flag = 1;
                        break;
                    }
                }
            }

            if(flag == 0)
            {
                this.safeLocation = "Safe Martian Surface";
            }
    }

    public String getSafeLocation()
    {
        return safeLocation;
    }


}
    


