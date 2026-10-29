package marscolony.missions;
import java.util.List;
import marscolony.ArrayList;

import marscolony.Equipment.Equipment;
import marscolony.Equipment.EquipmentType;
import marscolony.MarsColony.MarsColony;
import marscolony.conditions.EnvironmentCondition;
import marscolony.resources.ResourceType;
import marscolony.personnel.*;
import marscolony.facilities.Facilities;
import marscolony.facilities.FacilityStatus;
import marscolony.facilities.Laboratory;
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

    public boolean checkRequiredForMission() // He says req scientific skills??????
    {
        boolean robotStatus = false , equipmentStatus = false ,labStatus = false  ;
        for(RoboticUnit robot : MarsColony.getRobots())
        {
            if(robot instanceof ExplorationRover)
            {
                robotStatus = robot.canPerform(Capability.SURFACE_TRAVEL); 
                if(robotStatus)
                {
                    this.agents.add(robot);
                    break;
                }
            }
            
        }
        for(Equipment equipment : MarsColony.getEquipments() )
        {
            if( equipment.getType() == EquipmentType.EXPLORATION_EQUIPMENT && equipment.isOperationalStatus())
            {
                equipmentStatus = true;
                break;
            }
        }

        
        if(robotStatus && equipmentStatus){
            return true;
        }
        else
        {
            releaseAgents();
            return false;
        }
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

