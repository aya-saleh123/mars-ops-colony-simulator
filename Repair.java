import java.util.ArrayList;
import java.util.List;

public class Repair extends Mission {
    //private final Facility facilityToRepair;

  

    public Repair(String name, int missionDuration /*Facility facilityToRepair*/) {
        super(name, missionDuration);
       // this.facilityToRepair = facilityToRepair;
       // setTarget(facilityToRepair);
    }



    
    public int getRequiredAmount(ResourceType type) {
         throw new UnsupportedOperationException("TODO");
    //until writing body
    }
    



    
     public  List<Capability> getRequiredCapabilities() {
         throw new UnsupportedOperationException("TODO");
    //until writing body
     }

    @Override
    protected void Completed() {  }

      @Override
    public String getEnvironmentProblem(EnvironmentCondition env) {
        if (env == EnvironmentCondition.DUST_STORM) {
            return "Surface operations are not allowed during a dust storm";
        }
      
    else return null;
    }
}

