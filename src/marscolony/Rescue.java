import java.security.DrbgParameters.Capability;
import java.util.List;

public class Rescue extends Mission{
   // private Agent rescuedAgent;
   // private Facility targetLocation;

    public Rescue(String name, int missionDuration, Agent rescuedAgent, Facility targetLocation) {
        super(name, missionDuration);
    //    this.rescuedAgent = rescuedAgent;
     //   this.targetLocation = targetLocation;
            setTarget(targetLocation);
    }
    
    @Override
    public int getRequiredAmount(ResourceType type) {}

     @Override
    public List<Capability> getRequiredCapabilities() {}

     @Override
    public void getEnvironmentProblem(EnvironmentCondition env) {
        if (env == EnvironmentCondition.DUST_STORM) {
            System.out.println("Surface operations are not allowed during a dust storm");
        }
       else if (env == EnvironmentCondition.COMMUNICATION_DISRUPTION) {
            System.out.println( "Rescue needs communication but it is disrupted");
        }
    
    }
      @Override
    protected void Completed() { }


}
    


