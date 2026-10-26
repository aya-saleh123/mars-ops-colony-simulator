import java.security.DrbgParameters.Capability;
import java.util.List;

public class Exploration extends Mission {
    private String destination;

    public Exploration(String name, int missionDuration, String destination) {
        super(name, missionDuration);
        this.destination = destination;
    }
   
   @Override
    public int getRequiredAmount(ResourceType type) {}

     @Override
    public List<Capability> getRequiredCapabilities() {}
    
    @Override 
    public void getEnvironmentProblem(EnvironmentCondition env) {
        if(env==EnvironmentCondition.DUST_STORM)
            System.out.println("Surface operations are not allowed during a dust storm");

    }

    public String getDestination() {
        return destination;
    }


       

    
      
}
