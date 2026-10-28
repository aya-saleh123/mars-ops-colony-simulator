package marscolony.missions;
import java.util.List;
import marscolony.conditions.EnvironmentCondition;
import marscolony.resources.ResourceType;
import marscolony.personnel.*;
import marscolony.facilities.Facilities;

public class Exploration extends Mission {
    private String destination;
    private ExplorationRover rover;
    private MissionState state;

    public Exploration(String name, int missionDuration, String destination, ExplorationRover rover) {
        super(name, missionDuration);
        this.destination = destination;
        this.rover = rover;
        this.state = MissionState.PENDING;
    }

    public ExplorationRover getRover() {
        return rover;
    }

    public void setState(MissionState state) {
        this.state = state;
    }
   
   @Override
   public int getRequiredAmount(ResourceType type) {
    throw new UnsupportedOperationException("TODO");
    //until writing body
   }

     @Override
    public List<Capability> getRequiredCapabilities() {
     throw new UnsupportedOperationException("TODO");
    //until writing body

    }
    
    @Override 
    public  String getEnvironmentProblem(EnvironmentCondition env) {
        if(env==EnvironmentCondition.DUST_STORM)
            return "Surface operations are not allowed during a dust storm";
        else
          return null;
    }

    public String getDestination() {
        return destination;
    }


       

    
      
}

