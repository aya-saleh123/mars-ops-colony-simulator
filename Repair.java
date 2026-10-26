import java.util.ArrayList;
import java.util.List;

public class Repair extends Mission {
    //private final Facility facilityToRepair;

  

    public Repair(String name, int missionDuration, Facility facilityToRepair) {
        super(name, missionDuration);
       // this.facilityToRepair = facilityToRepair;
        setTarget(facilityToRepair);
    }



    @Override
    public int getRequiredAmount(ResourceType type) {
        return 0;
    }



    @Override
     public  List<Capability> getRequiredCapabilities() {
        List<Capability> list = new ArrayList<>();
        list.add(Capability.REPAIR);
        list.add(Capability.REPAIR);
        return list;
    }

    @Override
    protected void Completed() {  }
}

