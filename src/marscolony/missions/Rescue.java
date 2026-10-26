package marscolony.missions;
import marscolony.resources.*;
import marscolony.conditions.*;
import java.util.List;

public class Rescue extends Mission{
   // private Agent rescuedAgent;
   // private Facility targetLocation;

    public Rescue(String name, int missionDuration /*  Agent rescuedAgent, Facility targetLocation*/) {
        super(name, missionDuration);
    //    this.rescuedAgent = rescuedAgent;
     //   this.targetLocation = targetLocation;
           // setTarget(targetLocation);
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
    protected void Completed() { }


}
    


