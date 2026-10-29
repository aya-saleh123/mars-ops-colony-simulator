package marscolony.missions;
import marscolony.resources.ResourceType;
import marscolony.MarsColony.MarsColony;
import marscolony.conditions.EnvironmentCondition;
import marscolony.personnel.*;
import marscolony.facilities.Facilities;
import java.util.List;
import java.util.ArrayList;

public class Rescue extends Mission{
    private Agent rescuedAgent;
    private Facilities targetLocation;

    public Rescue(String name, int missionDuration , Agent rescuedAgent, Facilities targetLocation) {
        super(name, missionDuration);
        this.rescuedAgent = rescuedAgent;
        this.targetLocation = targetLocation;
        setTarget(targetLocation);
    }
    
    @Override
    public int getRequiredAmount(ResourceType type) {
         if(type==ResourceType.FUEL)
            return 10;
        else return 0;
    }

    @Override
    public List<Capability> getRequiredCapabilities() {
     List<Capability> capRescue=new ArrayList<>();
         capRescue.add(Capability.SURFACE_TRAVEL);
         capRescue.add(Capability.MEDICAL);
         return capRescue;
    }

     @Override
    public String getEnvironmentProblem(EnvironmentCondition env) {
        if (env == EnvironmentCondition.DUST_STORM) {
            return "Surface operations are not allowed during a dust storm";
        }
       else if (env == EnvironmentCondition.COMMUNICATION_DISRUPTION) {
            return "Rescue needs communication but it is disrupted";
        }
        else
            return null;
    
    }
      @Override
    protected void Completed() {
      rescuedAgent.setLocation(targetLocation.getLocationName());
      System.out.println(rescuedAgent.getIdNumber()+" rescued and brought to "+ targetLocation.getLocationName());

     }


     @Override 
     public boolean checkRequiredForMission()
    {
        boolean humanStatus = false , robotStatus = false ;
        for(Human human : MarsColony.getHumans())
        {
            if(human instanceof Firefighter)
            {
                humanStatus = human.canPerform(Capability.FIREFIGHTING); 
                if(humanStatus)
                {
                    this.assignAgent(human);
                    break;
                }
            }
        }
        for(RoboticUnit robot : MarsColony.getRobots() )
        {
            if(robot instanceof Drone)
            {
                robotStatus = robot.canPerform(Capability.AERIAL);
                if(robotStatus)
                {
                    this.assignAgent(robot);
                    break;
                }
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
    


