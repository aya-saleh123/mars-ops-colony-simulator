package marscolony.missions;
import java.util.List;
import java.util.ArrayList;
import marscolony.conditions.EnvironmentCondition;
import marscolony.resources.ResourceType;
import marscolony.personnel.*;
import marscolony.missions.FailureReason;


public class Exploration extends Mission {
    private String destination;

    public Exploration(String name, int missionDuration, String destination) {
        super(name, missionDuration);
        this.destination = destination;
    }

    public ExplorationRover getRover() {
       for(Agent a:getAgents()){
        if(a instanceof ExplorationRover)
            return (ExplorationRover) a;
       }
       return null;
    }
   
   @Override
   public int getRequiredAmount(ResourceType type) {
    if(type==ResourceType.FUEL)
        return 10;
    else 
        return 0;
   }

     @Override
    public List<Capability> getRequiredCapabilities() {
     List<Capability> list=new ArrayList<>();
     list.add(Capability.SURFACE_TRAVEL);
     list.add(Capability.SCIENCE);
     return list;
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

    @Override
    protected void Completed(){
        System.out.println("Region "+ destination +" explored");
    } 

    @Override 
    public void acceptMission(){
        super.acceptMission();
        for(Agent a: getAgents())
            a.setLocation("Martian Surface");
    }

      
}

