package marscolony.missions;
import java.util.List;
import java.util.ArrayList;
import marscolony.resources.ResourceType;
import marscolony.conditions.EnvironmentCondition;
import marscolony.facilities.Facilities;



public class Repair extends Mission {
    private final Facilities facilityToRepair;

    public Repair(String name, int missionDuration ,Facilities facilityToRepair) {
        super(name, missionDuration);
       this.facilityToRepair = facilityToRepair;
        setTarget(facilityToRepair);
    }

    
    public int getRequiredAmount(ResourceType type) {
         if(type==ResourceType.SPARE_PARTS)
            return 2;
        if(type==ResourceType.BATTERY_POWER)
            return 5;
         else
            return 0;

    }
    
     public  List<Capability> getRequiredCapabilities() {
         List<Capability> capRepair=new ArrayList<>();
         capRepair.add(Capability.REPAIR);
         capRepair.add(Capability.REPAIR);
         return capRepair;
     }

    @Override
    protected void Completed() { 
        facilityToRepair.setOperationalStatus(true);
        System.out.println(facilityToRepair.getLocationName()+" is repaired");
     }

      @Override
    public String getEnvironmentProblem(EnvironmentCondition env) {
        if (env == EnvironmentCondition.DUST_STORM) {
            return "Surface operations are not allowed during a dust storm";
        }
      
    else return null;
    }
}