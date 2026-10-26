package marscolony.missions;
import marscolony.conditions.EnvironmentCondition;
import marscolony.resources.ResourceType;
import java.util.List;
public class Research extends Mission {
    
     public Research(/*Facility laboratory*/ int duration) {
        super("Research", duration);
        //setTarget(laboratory);
    }

    @Override
    public int getRequiredAmount(ResourceType type) {
        if (type == ResourceType.WATER) { return 5; }
        if (type == ResourceType.BATTERY_POWER) { return 10; }
        return 0;
    }

    @Override
    public List<Capability> getRequiredCapabilities() {
         throw new UnsupportedOperationException("TODO");
    //until writing body
    }



     public String getEnvironmentProblem(EnvironmentCondition env) {
     
        if (env == EnvironmentCondition.COMMUNICATION_DISRUPTION) {
            return "Rescue needs communication but it is disrupted";
        }
        else
            return null;
    
    }
}
