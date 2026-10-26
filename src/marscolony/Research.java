import java.util.ArrayList;
import java.util.List;
public class Research extends Mission {
    
     public Research(Facility laboratory, int duration) {
        super("Research", duration);
        setTarget(laboratory);
    }

    @Override
    public int getRequiredAmount(ResourceType type) {
        if (type == ResourceType.WATER) { return 5; }
        if (type == ResourceType.BATTERY_POWER) { return 10; }
        return 0;
    }

    @Override
    public List<Capability> getRequiredCapabilities() {
        
    }



     public String EnvironmentProblem(EnvironmentCondition env) {
     
        if (env == EnvironmentCondition.COMMUNICATION_DISRUPTION) {
            return "Rescue needs communication but it is disrupted";
        }
        else
            return null;
    
    }
}
