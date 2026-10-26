import java.util.ArrayList;
import java.util.List;

public class Exploration extends Mission {
    private String destination;

    public Exploration(String name, int missionDuration, String destination) {
        super(name, missionDuration);
        this.destination = destination;
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
